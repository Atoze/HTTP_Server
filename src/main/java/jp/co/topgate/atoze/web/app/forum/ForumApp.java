package jp.co.topgate.atoze.web.app.forum;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 掲示板アプリ
 */
public class ForumApp {
    private ForumData forumData;

    private List<String[]> mainData = new ArrayList<>();

    private static final String CSV_FILEPATH = "./src/main/resources/program/board/";
    private static final String CSV_FILENAME = "save.csv";

    public ForumApp() throws IOException {
        forumData = new ForumData(new File(CSV_FILEPATH, CSV_FILENAME));
        //forumData = new ForumData(null);
        mainData = forumData.getData();
    }

    public void setMainData(List<String[]> data) {
        mainData = data;
    }

    public void setForumDataFile(File file) throws IOException {
        this.forumData = new ForumData(file);
        mainData = forumData.getData();
    }

    /**
     * 新規に投稿された時
     */
    public void createThread(Map<String, String> query) throws IOException {
        List<String[]> list = forumData.getData();
        //TODO ユーザー管理
        /*
        User user = new User();
        String name = query.get("name");
        if (!user.exists(name)) {
            user.saveData(user.newUser(name, retrieveNewID(forumData.getData())));
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
    public void findThread(String name) throws IOException {
        List<String[]> list = forumData.getData();
        List<String[]> data = new ArrayList<>();
        if (name != null) {
            for (int i = 0; i < list.size(); i++) {
                if (name.equals(ForumData.getParameter(list, i, "NAME"))) {
                    data.add(list.get(i));
                }
            }
        }
        mainData = data;
    }

    /**
     * 保存しているデータのID値から一致したものを削除
     */
    public void deleteThreadByID(String id, String requestPassword) throws IOException {
        List<String[]> list = forumData.getData();
        if (list.size() <= 0) {
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            if (id.equals(ForumData.getParameter(list, i, "ID"))) {
                if (requestPassword.equals(ForumData.getParameter(list, i, "PASSWORD"))) {
                    list.remove(i);
                    mainData = list;
                    forumData.overWriteData(list, new File(CSV_FILEPATH, CSV_FILENAME));
                    return;
                }
            }
        }
    }

    /**
     * Listのインデックス番号を指定し削除
     */
    public void deleteThreadByListIndex(String id, String requestPassword) throws IOException {
        if (!ForumData.isNumber(id)) {
            return;
        }
        int listIndex = Integer.parseInt(id);
        List<String[]> list = forumData.getData();
        if (list.size() <= 0) {
            return;
        }
        if (listIndex >= 0 && listIndex < list.size()) {
            if (ForumData.getParameter(list, listIndex, "PASSWORD").isEmpty()) {
                return;
            }
            if (requestPassword.equals(ForumData.getParameter(list, listIndex, "PASSWORD"))) {
                list.remove(listIndex);
                mainData = list;
                forumData.overWriteData(list, new File(CSV_FILEPATH, CSV_FILENAME));
            }
            System.out.println("パスワードが合っていません");
        }
    }

    /**
     * 追加するデータをリクエストのクエリ値を元に生成します.
     */
    @Contract(pure = true)
    private String[] generateNewThreadData(Map<String, String> query) throws IOException {
        List<String> saveData = new ArrayList<>();

        for (int i = 0; i < ForumDataPattern.size(); i++) {
            String key = ForumDataPattern.getKeyByIndex(i);
            switch (key) {
                case "ID":
                    saveData.add(key + ":" + retrieveNewID(forumData.getData()));
                    break;
                case "ICON":
                    saveData.add(key + ":" + "blank");
                    break;
                case "DATE":
                    saveData.add(key + ":" + date());
                    break;
                default:
                    saveData.add(key + ":" + query.get(ForumDataPattern.getQueryKeyByIndex(i)));
            }
        }
        return saveData.toArray(new String[0]);
    }

    @NotNull
    public List<String[]> getMainData() {
        return mainData;
    }

    //直接読み込んだものをそのまま返す
    void threadByCSV() {
        mainData = forumData.getData();
    }

    private String date() {
        Date date = new Date();
        return date.toString();
    }

    @Contract(pure = true)
    private int retrieveNewID(List<String[]> list) throws IOException {
        if (list.size() == 0) {
            return 0;
        }
        return Integer.parseInt(forumData.getParameter(list, list.size() - 1, ForumDataPattern.ID.getKey())) + 1;
    }
}
