package jp.co.topgate.atoze.web;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by atoze on 2017/05/10.
 */
public class CSVReader {
    private boolean endsLineFeed = false;

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

    public void saveData(List<String> text, File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream output = new FileOutputStream(file);
        PrintWriter writer = new PrintWriter(output, true);

        for (int i = 0; i <= text.size() - 1; i++) {
            writer.println(text.get(i));
        }
        //writer.println(text);
        writer.close();
        output.close();
    }

    public List<String> readCSV(File file) throws IOException {
        List<String> list = new ArrayList<>();
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(file));
        String line = readLine(br);
        if (line == null) {
            return null;
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

    public List<String> readCSV(File file, int start, int end) throws IOException {
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
