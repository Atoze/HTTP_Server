package jp.co.topgate.atoze.web.app.forum;

import jp.co.topgate.atoze.web.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by atoze on 2017/05/02.
 */
public class ForumAppHandler extends HTTPHandler {
    private static final String CSV_FILEPATH = "./src/main/resources/program/board/";
    private static final String CSV_FILENAME = "save.csv";

    private ForumData forumData;

    private final static List<String> key = Arrays.asList(
            "ID",
            "NAME",
            "TITLE",
            "TEXT",
            "PASSWORD",
            "DATE",
            "ICON"
    );

    enum KEY {
        ID(0, "id"),
        NAME(1, "name"),
        TITLE(2, "title"),
        TEXT(3, "text"),
        PASSWORD(4, "password"),
        DATE(5, "date"),
        ICON(6, "icon");

        public final int id;
        public final String key;

        KEY(int id, String key) {
            this.id = id;
            this.key = key;
        }

        public int getId() {
            return id;
        }

        public String getKey() {
            return key;
        }
    }

    private List<String[]> mainData = new ArrayList<>();
    private String html;


    public ForumAppHandler() throws IOException {
        forumData = new ForumData(new File(CSV_FILEPATH, CSV_FILENAME));
        mainData = forumData.getData();
    }

    public void handle() throws IOException {
        String method = request.getMethod();
        switch (method) {
            case "GET":
                getHandler();
                break;
            case "POST":
                postHandler();
                break;
        }
        System.out.println(request.getHost());
        setHTML(new ForumHTML(request.getHost()).indexHTML(mainData));
    }

    void getHandler() {
        GETThread();
    }

    void postHandler() throws IOException {
        if (request.getFilePath().endsWith("search")) {
            findThread(request.getParameter("search"));

            return;
        }
        if (request.getParameter("_method").equals("DELETE")) {
            if (forumData.isNumber(request.getParameter("threadID"))) {
                int id = Integer.parseInt(request.getParameter("threadID"));
                if (id <= mainData.size() - 1) {
                    deleteThread(id);
                    return;
                }
            }
            System.out.println("範囲外です");
        }
        newThread();
    }

    private void newThread() throws IOException {
        List<String[]> list = mainData;//forumData.getData();
        User user = new User();
        String name = request.getParameter("name");
        if (!user.exists(name)) {
            user.saveData(user.newUser(name, forumData.getNewId(forumData.getData())));
        }

        File file = new File(CSV_FILEPATH, CSV_FILENAME);
        String text[] = addNewThread(request);
        list.add(text);
        forumData.saveData(String.join(",", text), file);
        mainData = list;
    }

    public void findThread(String name) throws IOException {
        List<String[]> list = mainData;//forumData.getData();
        List<String[]> data = new ArrayList<>();
        System.out.println(list.size());
        for (int i = 0; i < list.size(); i++) {
            if (name.equals(ForumData.getParameter(list, i, "NAME"))) {
                data.add(list.get(i));
            }
        }
        mainData = data;
    }

    public void editThread() {

    }

    public void deleteThread(int id) throws IOException {
        List<String[]> list = forumData.getData();
        if (ForumData.getParameter(list, id, "PASSWORD").isEmpty()) {
            return;
        }
        if (request.getParameter("password").equals(ForumData.getParameter(list, id, "PASSWORD"))) {
            list.remove(id);
            mainData = list;
            forumData.saveData(list, new File(CSV_FILEPATH, CSV_FILENAME));
            return;
        }
        System.out.println("パスワードが合っていません");
    }

    private String[] addNewThread(HTTPRequest request) throws IOException {
        List<String> saveData = new ArrayList<>();
        for (int i = 0; i < key.size(); i++) {
            String key = this.key.get(i);
            switch (key) {
                case "ID":
                    saveData.add(key + ":" + forumData.getNewId(forumData.getData()));
                    break;
                case "ICON":
                    saveData.add(key + ":" + "blank");
                    break;
                case "DATE":
                    saveData.add(key + ":" + forumData.getDate());
                    break;

                default:
                    saveData.add(key + ":" + request.getParameter(key.toLowerCase()));
            }
        }
        return saveData.toArray(new String[0]);
    }

    private void setHTML(String html) {
        this.html = html;

    }

    public void GETThread() {
        mainData = forumData.getData();
    }

    public void generateResponse() {
        response.addResponseHeader("Content-Type", "text/html; charset=UTF-8");

        if (statusCode == 0 || statusCode == 200) {
            response.setResponseBody(this.html);
        } else {
            generateErrorPage(statusCode);
        }
    }
}
