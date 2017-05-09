package jp.co.topgate.atoze.web;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by atoze on 2017/05/09.
 */
public class ForumData {
    private int id;
    private List list = new ArrayList<String>();
    static final String CSV_FILEPATH = "./src/main/resources/program/";
    static final String CSV_FILENAME = "save.csv";

    ForumData() throws IOException {
        //CSVParse csv = new CSVParse();
        this.list = checkData(readCSV(new File(CSV_FILEPATH,CSV_FILENAME)));
    }

    public void addList(String data) {
        this.list.add(data);
    }

    public List getData() {
        return this.list;
    }

    public void saveData(String text, File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream output = new FileOutputStream(file, true);
        PrintWriter writer = new PrintWriter(output, true);
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
    private List checkData(List list) {
        return checkData(list, 0, list.size() - 1);
    }

    private List checkData(List list, int start, int end) {
        int index = start;
        String[] data = list.get(index).toString().split(",");
        while (!isNumber(data[0]) && index <= end) {
            list.remove(index);
            index++;
            data = list.get(index).toString().split(",");
        }
        System.out.println(list.get(index).toString());
        return list;
    }
    private int readLastId(List list) throws IOException {
        int index = list.size() - 1;
        String last = list.get(index).toString();
        String[] data = last.split(",", 0);
        return Integer.parseInt(data[0]) + 1;
    }
    public int getNewId(List list) throws IOException {
        return readLastId(list);
    }
    public String getDate() {
        Date date = new Date();
        return date.toString();
    }
    private List readCSV(File file) throws IOException {
        List<String> list = new ArrayList<String>();
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line = br.readLine();
        while (line != null) {
            list.add(line);
            line = br.readLine();
        }
        return list;
    }
}
