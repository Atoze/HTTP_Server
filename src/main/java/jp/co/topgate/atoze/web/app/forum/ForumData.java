package jp.co.topgate.atoze.web.app.forum;

import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * CSVFileClassとForumAppHandlerClassの互いのデータを、
 * それぞれ最適な形に変換します.
 */

public class ForumData {
    private final List<String[]> data;
    private final CSVFile reader = new CSVFile();

    public ForumData(File file) throws IOException {
        data = checkData(reader.readCSV(file));
    }

    /**
     * リストデータを返します.
     */
    public List<String[]> getData() {
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
            for (int i = 0; i < text.length - 1; i++) {
                String str = text[i];
                sb.append(str).append(",");
            }
            sb.append(text[text.length - 1]);
        }
        reader.writeData(sb.toString(), file, false);
    }

    /**
     * 文字列が数字であるか判別します.
     *
     * @param num 文字列
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
     * データからID値が最初に挿入されていないもの,ID値が重複しているものを削除します.
     *
     * @param data  確認するリストデータ
     * @param start
     * @param end
     * @return 整列されたデータ
     */
    @Contract(pure = true)
    private static List<String[]> checkData(List<String[]> data, int start, int end) {
        if (data.size() == 0) {
            return data;
        }
        if (start > end) {
            start ^= end;
            end ^= start;
            start ^= end;
        }
        if (end >= data.size()) {
            end = data.size() - 1;
        }
        Set<String> existsID = new HashSet<>();
        for (int i = start; i <= end; i++) {
            String currentID = getParameter(data, i, ForumDataPattern.ID.getKey());
            if (!isNumber(currentID) || data.isEmpty() || existsID.contains(currentID)) {
                data.remove(i);
                if (data.size() <= 0) {
                    return data;
                }
                end = end - 1;
                i = i - 1;
            }
            existsID.add(currentID);
        }
        return data;
    }

    @Contract(pure = true)
    private static List<String[]> checkData(List<String[]> data) {
        return checkData(data, 0, data.size() - 1);
    }

    /**
     * #checkDataで作成されたリストに保管されているデータから,指定した属性の値を取り出します.
     *
     * @param list データ
     * @param id   リストの番号
     * @param key  属性
     * @return 指定した属性に対応した値
     */

    @Contract(value = "null, _, _ -> null;")
    static String getParameter(List<String[]> list, int id, String key) {
        String[] line = list.get(id);
        if (line.length <= 0) {
            return null;
        }
        return getParameter(line, key);
    }

    static String getParameter(String[] line, String key) {
        Map<String, String> data = new HashMap<>();
        String[] name = line[0].split(":", 2);
        if (name.length >= 2) {
            data.put(name[0], name[1]);
        } else {
            data.put(ForumDataPattern.ID.getKey(), name[0]);
        }

        for (int i = 1; i < line.length; i++) {
            String[] values = line[i].split(":", 2);
            if (values.length == 2) {
                data.put(values[0], values[1]);
            } else if (values.length == 1) {
                data.put(String.valueOf(i), values[0]);
            }
        }
        String encode = data.getOrDefault(ForumDataPattern.ENCODER.getKey(), "UTF-8");
        encode = checkEncode(encode);
        String parameter = data.getOrDefault(key.toUpperCase(), "");

        if (key.equals(ForumDataPattern.ENCODER.getKey())) {
            parameter = data.getOrDefault(key.toUpperCase(), "UTF-8");
        }
        try {
            return URLDecoder.decode(parameter, encode);
        } catch (UnsupportedEncodingException e) {
            return parameter;
        }
    }

    private static String checkEncode(String encode) {
        try {
            URLDecoder.decode("", encode);
            return encode;
        } catch (UnsupportedEncodingException e) {
            return "UTF-8";
        }
    }
}
