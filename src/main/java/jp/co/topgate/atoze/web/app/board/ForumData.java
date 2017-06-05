package jp.co.topgate.atoze.web.app.board;

import java.io.Serializable;

/**
 * Forumのデータクラスです.
 */
public class ForumData implements Serializable {
    private int id;
    private String encoder;
    private String name;
    private String title;
    private String text;
    private String password;
    private String date;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setEncoder(String encoder) {
        this.encoder = encoder;
    }

    public String getEncoder() {
        return encoder;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }
}