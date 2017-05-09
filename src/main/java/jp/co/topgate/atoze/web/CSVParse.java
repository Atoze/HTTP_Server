package jp.co.topgate.atoze.web;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by atoze on 2017/05/09.
 */
public class CSVParse {
    File file;

    CSVParse(){
    }

    public List readCSV(File file) throws IOException {
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

    public void saveAllData(List data, File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream output = new FileOutputStream(file, true);
        PrintWriter writer = new PrintWriter(output, true);
        writer.println(data);
        writer.close();
        output.close();
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

    private boolean isNumber(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
