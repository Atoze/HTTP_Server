package jp.co.topgate.atoze.web;

import java.io.IOException;
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

    User() {

    }

    private void readLastId(List list) throws IOException {
        int index = list.size() - 1;
        String last = list.get(index).toString();
        String[] data = last.split(",", 0);
        while (!isNumber(data[0])) {
            //list.remove(index);
            index = index - 1;
            last = list.get(index).toString();
            data = last.split(",", 0);
        }
        this.id = Integer.parseInt(data[0]) + 1;
        //this.id = Integer.parseInt(data[0]) + 1;
        System.out.println(last);
    }

    private boolean isNumber(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public int getNewId(List list) throws IOException {
        readLastId(list);
        return id;
    }

    public String getName() {
        return name;
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
