package jp.co.topgate.atoze.web.app.forum;

import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CSVFileClassとForumAppHandlerClassの互いのデータを、
 * それぞれ最適な形に変換します.
 */

class ForumData {
    private final List<String[]> data;
    private final CSVFile reader = new CSVFile();

    ForumData(File file) throws IOException {
        data = checkData(reader.readCSV(file));
    }

    /**
     * リストデータを返します.
     */
    List<String[]> getData() {
        return data;
    }

    /**
     * 既存のデータに追加する形で、データをCSVファイルに保存します.
     *
     * @param data 追加するデータ
     * @param file 保存先ファイル
     * @throws IOException 書き込みエラー
     */
    void saveData(String data, File file) throws IOException {
        reader.writeData(data, file, true);
    }

    /**
     * 保存されているデータを破棄し、入力されたデータでCSVファイルを上書きします.
     *
     * @param data 追加するデータ
     * @param file 上書きする保存先ファイル
     * @throws IOException 書き込みエラー
     */

    void overWriteData(List<String[]> data, File file) throws IOException {
        StringBuffer sb = new StringBuffer();
        for (String[] text : data) {
            for (int i = 1; i < text.length; i++) {
                String str = text[i];
                sb.append(str).append(",");
            }
        }
        reader.writeData(sb.toString(), file, false);
    }

    /**
     * 文字列が数字であるか判別します.
     *
     * @param num  文字列
     * @return 文字列が数値であるか否か
     */
    @Contract(pure = true, value = "null -> false")
    static boolean isNumber(String num) {
        try {
            Integer.parseInt(num);
            return true;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    /**
     * 文字列が数字であるか判別します.
     *
     * @param list
     * @param start
     * @param end
     * @return 整列されたデータ
     */
    @Contract(pure = true)
    private static List<String[]> checkData(List<String[]> list, int start, int end) throws UnsupportedEncodingException {
        if (list.size() == 0) {
            return list;
        }
        if (start > end) {
            start ^= end;
            end ^= start;
            start ^= end;
        }
        if (end >= list.size()) {
            end = list.size() - 1;
        }
        for (int i = start; i <= end; i++) {
            if (!isNumber(getParameter(list, i, "ID"))) {
                list.remove(i);
                if (list.size() <= 0) {
                    return list;
                }
                end = end - 1;
                i = i - 1;
            }
        }
        return list;
    }

    @Contract(pure = true)
    private static List<String[]> checkData(List<String[]> list) throws UnsupportedEncodingException {
        return checkData(list, 0, list.size() - 1);
    }

    static String getParameter(List<String[]> list, int id, String key) throws UnsupportedEncodingException {
        String[] datas = list.get(id);
        if (datas.length <= 0) {
            return null;
        }

        String[] name = datas[0].split(":", 2);
        Map<String, String> data = new HashMap<>();
        if (name.length >= 2) {
            data.put(name[0], name[1]);
        } else {
            data.put("ID", name[0]);
        }

        for (int i = 1; i < datas.length; i++) {
            String[] values = datas[i].split(":", 2);
            if (values.length == 2) {
                data.put(values[0], values[1]);
            } else if (values.length == 1) {
                data.put(String.valueOf(i), values[0]);
            }
        }
        return data.getOrDefault(key.toUpperCase(), "");
    }

    static String getParameter(String[] line, String key) throws UnsupportedEncodingException {
        String[] datas = line;
        Map<String, String> data = new HashMap<>();
        String[] name = datas[0].split(":", 2);
        if (name.length >= 2) {
            data.put(name[0], name[1]);
        } else {
            data.put("ID", name[0]);
        }

        for (int i = 1; i < datas.length; i++) {
            String[] values = datas[i].split(":", 2);
            if (values.length == 2) {
                data.put(values[0], values[1]);
            } else if (values.length == 1) {
                data.put(String.valueOf(i), values[0]);
            }
        }
        return data.getOrDefault(key.toUpperCase(), "");
    }
}
