package jp.co.topgate.atoze.web.app.board;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * データをForumData形式に変換し操作します.
 */
public class DataHandler {
    private List<ForumData> data = new ArrayList<>();
    private final CSVFile reader = new CSVFile();
    private final File file;

    private final static String KEY_VALUE_SEPARATOR = ":";

    DataHandler(File file) throws IOException {
        this.file = file;
        data = readListData(reader.readCSV(file));
    }

    /**
     * CSVFileClassから読み込まれたデータをForumData形式に変換します.
     *
     * @param csvData CSVデータ
     * @return 変換されたリストデータ
     */
    List<ForumData> readListData(List<String[]> csvData) {
        if (csvData.size() == 0) {
            return new ArrayList<>();
        }

        List<ForumData> readData = new ArrayList<>();
        Set<Integer> existIDs = new HashSet<>();
        for (int currentRow = 0; currentRow < csvData.size(); currentRow++) {
            ForumData data = new ForumData();
            String[] line = csvData.get(currentRow);

            for (int currentColumn = 0; currentColumn < line.length; currentColumn++) {
                String[] value = line[currentColumn].split(KEY_VALUE_SEPARATOR, 2);
                //String key = line[0];
                data = insertValueToData(data, currentColumn, value[value.length - 1]);
            }
            if (data.getId() >= 0 && !existIDs.contains(data.getId())) {
                readData.add(data);
                existIDs.add(data.getId());
            }
        }
        return readData;
    }

    /**
     * 指定のキーインデックスに対応する値を挿入して返します。
     *
     * @param data     挿入する前のデータ
     * @param keyIndex 挿入する値のキー番号
     * @param value    挿入する値
     * @return 挿入済みデータ
     */
    static ForumData insertValueToData(ForumData data, int keyIndex, String value) {
        ForumDataKey key = ForumDataKey.getKeyByIndex(keyIndex);
        switch (key) {
            case ID:
                int id;
                try {
                    id = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    id = -1;
                }
                data.setId(id);
                break;

            case ENCODER:
                data.setEncoder(value);
                break;

            case NAME:
                data.setName(value);
                break;

            case TITLE:
                data.setTitle(value);
                break;

            case TEXT:
                data.setText(value);
                break;

            case PASSWORD:
                data.setPassword(value);
                break;

            case DATE:
                data.setDate(value);
                break;

            case ICON:
                break;

            default:
                break;
        }
        return data;
    }

    /**
     * ForumData形式のデータをString配列に変換します.
     */
    private static String[] getAllValue(ForumData data) {
        String[] values = new String[ForumDataKey.size()];

        for (int i = 0; i < values.length; i++) {
            ForumDataKey key = ForumDataKey.getKeyByIndex(i);
            String value = "";
            switch (key) {
                case ID:
                    value = Integer.toString(data.getId());
                    break;

                case ENCODER:
                    value = data.getEncoder();
                    break;

                case NAME:
                    value = data.getName();
                    break;

                case TITLE:
                    value = data.getTitle();
                    break;

                case TEXT:
                    value = data.getText();
                    break;

                case PASSWORD:
                    value = data.getPassword();
                    break;

                case DATE:
                    value = data.getDate();
                    break;

                case ICON:
                    break;

                default:
                    break;
            }
            values[i] = value;
        }
        return values;
    }

    /**
     * CSVファイルからForumData形式に変換されたデータを返します.
     *
     * @return 変換されたリストデータ
     */
    public List<ForumData> getData() {
        return data;
    }

    /**
     * ForumData形式のデータをCSVに追加保存します.
     */
    public void save(ForumData data) throws IOException {
        reader.writeData(getAllValue(data), file, true);
    }

    /**
     * ForumData形式のデータの集合をCSVに上書き保存します.
     */
    public void overWrite(List<ForumData> data) throws IOException {
        List<String[]> saveData = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            saveData.add(getAllValue(data.get(i)));
        }
        reader.writeData(saveData, file, false);
    }
}
