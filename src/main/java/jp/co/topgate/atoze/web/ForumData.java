package jp.co.topgate.atoze.web;

import java.io.*;
import java.util.*;

/**
 * Created by atoze on 2017/05/09.
 */

public class ForumData {
    private List<String> list = new ArrayList<String>();
    private static final String CSV_FILEPATH = "./src/main/resources/program/";
    private static final String CSV_FILENAME = "save.csv";
    private boolean endsLineFeed = false;

    ForumData() throws IOException {
        this.list = checkData(readCSV(new File(CSV_FILEPATH, CSV_FILENAME)));
    }

    public void addList(String data) {
        this.list.add(data);
    }

    public List<String> getData() {
        return this.list;
    }

    public void saveData(String text, File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream output = new FileOutputStream(file, true);
        PrintWriter writer = new PrintWriter(output, true);
        if (!endsLineFeed) {
            text = System.getProperty("line.separator") + text;
        }
        writer.println(text);
        writer.close();
        output.close();
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
            getCsvData(list, i, "id");
            //String[] data = list.get(i).split(",", 0);
            if (!isNumber(getCsvData(list, i, "id"))) {
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

    private int readLastId(List<String> list) throws IOException {
        int index = list.size() - 1;
        System.out.println(Integer.parseInt(getCsvData(list, index, "id")));
        return Integer.parseInt(getCsvData(list, index, "id"));
        /*
        String last = list.get(index);
        String[] data = last.split(",", 0);
        return Integer.parseInt(data[0]);
        */
    }

    public int getNewId(List<String> list) throws IOException {
        return readLastId(list) + 1;
    }

    public String getDate() {
        Date date = new Date();
        return date.toString();
    }

    public String getCsvData(List<String> list, int id, String key) throws UnsupportedEncodingException {
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

    private List<String> readCSV(File file) throws IOException {
        List<String> list = new ArrayList<>();
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(file));
        String line = readLine(br);
        if (line == null) {
            list.add("0");
        }
        while (line != null) {
            list.add(line);
            line = readLine(br);
        }
        if (list.get(list.size() - 1).endsWith(System.getProperty("line.separator"))) {
            endsLineFeed = true;
        }
        return list;
    }

    private List<String> readCSV(File file, int start, int end) throws IOException {
        List<String> list = new ArrayList<>();
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(file));
        String line = readLine(br);
        if (line == null) {
            list.add("0");
            return list;
        }
        for (int i = start; i <= end && line != null; i++) {
            list.add(line);
            line = readLine(br);
        }
        if (list.get(list.size() - 1).endsWith(System.getProperty("line.separator"))) {
            endsLineFeed = true;
        }
        return list;
    }

    private String readLine(InputStream input) throws IOException {
        int num = 0;
        StringBuffer sb = new StringBuffer();
        try {
            while ((num = input.read()) >= 0) {
                sb.append((char) num);
                switch ((char) num) {
                    case '\r':
                    case '\n':
                        return sb.toString();
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (sb.length() == 0) {
            return null;
        } else {
            return sb.toString();
        }
    }
}
