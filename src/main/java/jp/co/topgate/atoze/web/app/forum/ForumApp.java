package jp.co.topgate.atoze.web.app.forum;

import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 掲示板アプリ
 */
class ForumApp {
    private final ForumData forumData;

    private List<String[]> mainData = new ArrayList<>();

    private static final String CSV_FILEPATH = "./src/main/resources/program/board/";
    private static final String CSV_FILENAME = "save.csv";
    private static final List<String> KEY = Arrays.asList(
            "ID",
            "NAME",
            "TITLE",
            "TEXT",
            "PASSWORD",
            "DATE",
            "ICON"
    );

    ForumApp() throws IOException {
        forumData = new ForumData(new File(CSV_FILEPATH, CSV_FILENAME));
        mainData = forumData.getData();
    }

    /**
     * 新規に投稿された時
     */
    void newThread(Map<String, String> query) throws IOException {
        List<String[]> list = forumData.getData();
        //TODO ユーザー管理
        /*
        User user = new User();
        String name = query.get("name");
        if (!user.exists(name)) {
            user.saveData(user.newUser(name, getNewId(forumData.getData())));
        }
        */
        File file = new File(CSV_FILEPATH, CSV_FILENAME);
        String text[] = generateNewThreadData(query);

        list.add(text);
        forumData.saveData(String.join(",", text), file);
        mainData = list;
    }

    /**
     * 検索時
     */
    void findThread(String name) throws IOException {
        List<String[]> list = forumData.getData();
        List<String[]> data = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (name.equals(ForumData.getParameter(list, i, "NAME"))) {
                data.add(list.get(i));
            }
        }
        mainData = data;
    }

    /**
     * 削除時
     */
    void deleteThread(int id, String requestPassword) throws IOException {
        List<String[]> list = forumData.getData();
        if (ForumData.getParameter(list, id, "PASSWORD").isEmpty()) {
            return;
        }
        if (requestPassword.equals(ForumData.getParameter(list, id, "PASSWORD"))) {
            list.remove(id);
            mainData = list;
            forumData.overWriteData(list, new File(CSV_FILEPATH, CSV_FILENAME));
        }
        System.out.println("パスワードが合っていません");
    }

    /**
     * 追加するデータをリクエストのクエリ値を元に生成します.
     */
    @Contract(pure = true)
    private String[] generateNewThreadData(Map<String, String> query) throws IOException {
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
                    saveData.add(key + ":" + query.get(key.toLowerCase()));
            }
        }
        return saveData.toArray(new String[0]);
    }

    List<String[]> getMainData() {
        return mainData;
    }

    void GETThread() {
        mainData = forumData.getData();
    }

    private String getDate() {
        Date date = new Date();
        return date.toString();
    }

    @Contract(pure = true)
    int getNewId(List<String[]> list) throws IOException {
        if (list.size() == 0) {
            return 0;
        }
        return Integer.parseInt(forumData.getParameter(list, list.size() - 1, "ID")) + 1;
    }
}
