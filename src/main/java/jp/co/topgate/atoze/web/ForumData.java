package jp.co.topgate.atoze.web;

import java.io.*;
import java.util.*;

/**
 * Created by atoze on 2017/05/09.
 */

public class ForumData {
    private List<String> list = new ArrayList<>();
    private static final String CSV_FILEPATH = "./src/main/resources/program/";
    private static final String CSV_FILENAME = "save.csv";
    private CSVReader reader = new CSVReader();

    ForumData() throws IOException {
        if (reader.readCSV(new File(CSV_FILEPATH, CSV_FILENAME)) != null) {
            this.list = checkData(reader.readCSV(new File(CSV_FILEPATH, CSV_FILENAME)));
        }
    }

    public void addList(String data) {
        this.list.add(data);
    }

    public List<String> getData() {
        return this.list;
    }

    public void saveData(String text, File file) throws IOException {
        reader.saveData(text, file);
    }

    public void saveData(List<String> text, File file) throws IOException {
        reader.saveData(text, file);
    }

    private boolean isNumber(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private List<String> checkData(List<String> list) throws UnsupportedEncodingException {
        return checkData(list, 0, list.size() - 1);
    }

    private List<String> checkData(List<String> list, int start, int end) throws UnsupportedEncodingException {
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
                    list.add("0");
                    return list;
                }
                end = end - 1;
                i = i - 1;
            }
        }
        return list;
    }

    public int getNewId(List<String> list) throws IOException {
        if(list.size()==0){
            return 0;
        }
        return Integer.parseInt(getParameter(list, list.size() - 1, "id")) + 1;
    }

    public String getDate() {
        Date date = new Date();
        return date.toString();
    }

    public String getParameter(List<String> list, int id, String key) throws UnsupportedEncodingException {
        String[] datas = list.get(id).split(",");
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
