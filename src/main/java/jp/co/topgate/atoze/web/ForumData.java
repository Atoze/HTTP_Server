package jp.co.topgate.atoze.web;

import java.io.*;
import java.util.*;

/**
 * Created by atoze on 2017/05/09.
 */

public class ForumData {
    private List<String[]> list = new ArrayList<>();
    private CSVFile reader = new CSVFile();

    ForumData(File file) throws IOException {
        if (reader.readCSVWithParse(file) != null) {
            this.list = checkData(reader.readCSVWithParse(file));
        }
    }

    public List<String[]> getData() {
        return this.list;
    }

    public void saveData(String text, File file) throws IOException {
        reader.writeData(text, file);
    }

    public void saveData(List<String[]> list, File file) throws IOException {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            String[] text = list.get(i);
            for (String str : text) {
                sb.append(str).append(",");
            }
        }
        saveData(sb.toString(), file);
    }

    public boolean isNumber(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private List<String[]> checkData(List<String[]> list) throws UnsupportedEncodingException {
        return checkData(list, 0, list.size() - 1);
    }

    private List<String[]> checkData(List<String[]> list, int start, int end) throws UnsupportedEncodingException {
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

    public int getNewId(List<String[]> list) throws IOException {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return Integer.parseInt(getParameter(list, list.size() - 1, "ID")) + 1;
    }

    public String getDate() {
        Date date = new Date();
        return date.toString();
    }

    public static String getParameter(List<String[]> list, int id, String key) throws UnsupportedEncodingException {
        String[] datas = list.get(id);
        if(datas.length<=0){
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

    public static String getParameter(String[] line, String key) throws UnsupportedEncodingException {
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
