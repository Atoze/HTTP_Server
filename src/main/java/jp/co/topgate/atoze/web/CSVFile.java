package jp.co.topgate.atoze.web;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CSVファイルの読み書きを行います.
 *
 * @author atoze
 */
public class CSVFile {
    private boolean endsLineFeed = false;
    private final static String lineFeed = System.getProperty("line.separator");

    /**
     * CSVファイルに書き込んで保存します.
     * 指定されたファイルがない場合は、新しく作成します.
     *
     * @param text
     * @param file
     * @author atoze
     */
    public void writeData(String text, File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream output = new FileOutputStream(file, true);
        PrintWriter writer = new PrintWriter(output, true);
        if (!endsLineFeed) {
            text = lineFeed + text;
        }
        writer.print(text);
        writer.close();
        output.close();
    }

    public List<String[]> readCSVWithParse(File file, int elementNum) throws IOException {
        List<String[]> list = new ArrayList<>();
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(file));
        String line = readLine(br);
        if (line == null) {
            return list;
        }
        while (line != null) {
            String[] data = line.split(",", elementNum);
            if (data.length == elementNum)
                list.add(data);
            line = readLine(br);
        }
        String[] last = list.get(list.size() - 1);
        if (last.length > elementNum && last[elementNum - 1].endsWith(lineFeed)) {
            endsLineFeed = true;
        }
        return list;
    }

    public List<String[]> readCSVWithParse(File file) throws IOException {
        List<String[]> list = new ArrayList<>();
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(file));
        String line = readLine(br);
        if (line == null) {
            return list;
        }
        while (line != null) {
            String[] data = line.split(",");
            list.add(data);
            line = readLine(br);
        }
        String[] last = list.get(list.size() - 1);
        if (last.length > 0 && last[last.length - 1].endsWith(lineFeed)) {
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
