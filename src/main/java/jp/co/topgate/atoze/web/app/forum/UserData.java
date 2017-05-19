package jp.co.topgate.atoze.web.app.forum;

import java.io.*;
import java.util.*;

/**
 * Created by atoze on 2017/05/08.
 */
class UserData {
    private int id;
    private String name;
    private String title;
    private String text;
    private static final String CSV_FILEPATH = "./src/main/resources/program/board/";
    private static final String CSV_FILENAME = "user.csv";
    private CSVFile reader = new CSVFile();
    private boolean endsLineFeed = false;
    List<String[]> list = new ArrayList<>();

    UserData() throws IOException {
        File file = new File(CSV_FILEPATH, CSV_FILENAME);
        list = reader.readCSV(file);
    }

    public List<String[]> getData() throws UnsupportedEncodingException {
        return checkData(list);
    }

    public void saveData(String text) throws IOException {
        reader.writeData(text, new File(CSV_FILEPATH, CSV_FILENAME), false);
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    private List<String[]> checkData(List<String[]> list) throws UnsupportedEncodingException {
        return checkData(list, 0, list.size() - 1);
    }

    private List<String[]> checkData(List<String[]> list, int start, int end) throws UnsupportedEncodingException {
        if (list.size() <= 0) {
            return new ArrayList<>();
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
            getParameter(list, i, "id");
            if (!isNumber(getParameter(list, i, "id"))) {
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

    public boolean isNumber(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String getParameter(List<String[]> list, int id, String key) throws UnsupportedEncodingException {
        String[] datas = list.get(id);
        Map<String, String> data = new HashMap<>();
        String[] name = datas[0].split(":", 2);
        if (name.length >= 2) {
            data.put(name[0], name[1]);
        } else {
            data.put("id", name[0]);
        }

        for (int i = 1; i < datas.length; i++) {
            String[] values = datas[i].split(":", 2);
            if (values.length >= 2) {
                data.put(values[0], values[1]);
            }
        }
        return data.getOrDefault(key, null);
    }

    public String getParameter(int id, String key) throws UnsupportedEncodingException {
        String[] datas = list.get(id);
        Map<String, String> data = new HashMap<>();
        String[] name = datas[0].split(":", 2);
        if (name.length >= 2) {
            data.put(name[0], name[1]);
        } else {
            data.put("id", name[0]);
        }

        for (int i = 1; i < datas.length; i++) {
            String[] values = datas[i].split(":", 2);
            if (values.length >= 2) {
                data.put(values[0], values[1]);
            }
        }
        return data.getOrDefault(key, null);
    }
}
