package jp.co.topgate.atoze.web;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by atoze on 2017/05/08.
 */
public class User {
    private int id;
    private String name;
    private String title;
    private String text;
    private static final String CSV_FILEPATH = "./src/main/resources/program/";
    private static final String CSV_FILENAME = "user.csv";
    private boolean endsLineFeed = false;
    List<String> userList = new ArrayList<>();

    User() throws IOException {
        File file = new File(CSV_FILEPATH, CSV_FILENAME);
        readUserData(file);
    }

    public void saveUserData(File file, String text) throws IOException {
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

    public List<String> readUserData(File file) throws IOException {
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
        //userList = list;
        return list;
    }

    public void findUser(){

    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        Date date = new Date();
        return date.toString();
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
