package jp.co.topgate.atoze.web;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by atoze on 2017/05/08.
 */
public class User{
    private int id;
    private String name;
    private String title;
    private String text;
    private static final String CSV_FILEPATH = "./src/main/resources/program/";
    private static final String CSV_FILENAME = "user.csv";
    private CSVReader reader = new CSVReader();
    private boolean endsLineFeed = false;
    List<String> userList = new ArrayList<>();

    User() throws IOException {
        File file = new File(CSV_FILEPATH, CSV_FILENAME);
        userList = reader.readCSV(file);
    }

    public void findUser() {

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
}
