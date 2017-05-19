package jp.co.topgate.atoze.web.app.forum;

import jp.co.topgate.atoze.web.*;
import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 掲示板の挙動を制御します.
 */
public class ForumAppHandler extends HTTPHandler {
    private static final String CSV_FILEPATH = "./src/main/resources/program/board/";
    private static final String CSV_FILENAME = "save.csv";

    private Map<String, String> query;

    //private String method;
    private String filePath;
    private String HOST;

    private static ForumData forumData;

    private final List<String> KEY = Arrays.asList(
            "ID",
            "NAME",
            "TITLE",
            "TEXT",
            "PASSWORD",
            "DATE",
            "ICON"
    );

    private List<String[]> mainData = new ArrayList<>();
    private String html;

    public ForumAppHandler() throws IOException {
        super();
        forumData = new ForumData(new File(CSV_FILEPATH, CSV_FILENAME));
        //forumData = new ForumData(new File(CSV_FILEPATH, CSV_FILENAME));
        mainData = forumData.getData();
    }

    @Override
    public void setRequest(HTTPRequest request) {
        //method = request.getMethod();
        filePath = request.getFilePath();
        HOST = request.getHost();
        query = request.getQuery();
    }

    /**
     * 掲示板の挙動を制御します.
     */

    public void handle(String method) throws IOException {
        //String method = method;
        switch (method) {
            case "GET":
                getHandler();
                break;
            case "POST":
                postHandler();
                break;
        }
        setHTML(new ForumHTML(HOST).getIndexHTML(mainData));
    }

    void getHandler() throws IOException {
        if (filePath.endsWith("search")) {
            findThread(getQueryParam("search"));
            return;
        }
        GETThread();
    }

    void postHandler() throws IOException {
        if (filePath.endsWith("search")) {
            findThread(getQueryParam("search"));
            return;
        }
        if (getQueryParam("_method").equals("DELETE")) {
            if (forumData.isNumber(getQueryParam("threadID"))) {
                int id = Integer.parseInt(getQueryParam("threadID"));
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
        List<String[]> list = mainData;
        User user = new User();
        String name = getQueryParam("name");
        if (!user.exists(name)) {
            user.saveData(user.newUser(name, getNewId(forumData.getData())));
        }

        File file = new File(CSV_FILEPATH, CSV_FILENAME);
        String text[] = addNewThread();
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

    public void deleteThread(int id) throws IOException {
        List<String[]> list = forumData.getData();
        if (ForumData.getParameter(list, id, "PASSWORD").isEmpty()) {
            return;
        }
        if (getQueryParam("password").equals(ForumData.getParameter(list, id, "PASSWORD"))) {
            list.remove(id);
            mainData = list;
            forumData.overWriteData(list, new File(CSV_FILEPATH, CSV_FILENAME));
            return;
        }
        System.out.println("パスワードが合っていません");
    }

    /**
     * 掲示板の挙動を制御します.
     */
    private String[] addNewThread() throws IOException {
        List<String> saveData = new ArrayList<>();
        for (int i = 0; i < KEY.size(); i++) {
            String key = this.KEY.get(i);
            switch (key) {
                case "ID":
                    saveData.add(key + ":" + getNewId(forumData.getData()));
                    break;
                case "ICON":
                    saveData.add(key + ":" + "blank");
                    break;
                case "DATE":
                    saveData.add(key + ":" + getDate());
                    break;

                default:
                    saveData.add(key + ":" + getQueryParam(key.toLowerCase()));
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

    @Override
    public void generateResponse() {
        response.addResponseHeader("Content-Type", "text/html; charset=UTF-8");

        if (statusCode == 0 || statusCode == 200) {
            response.setResponseBody(html);
        } else {
            generateErrorPage(statusCode);
        }
    }

    @Contract(pure = true)
    int getNewId(List<String[]> list) throws IOException {
        if (list.size() == 0) {
            return 0;
        }
        return Integer.parseInt(forumData.getParameter(list, list.size() - 1, "ID")) + 1;
    }

    public String getQueryParam(String key) {
        return query.getOrDefault(key, "");
    }

    String getDate() {
        Date date = new Date();
        return date.toString();
    }

}
