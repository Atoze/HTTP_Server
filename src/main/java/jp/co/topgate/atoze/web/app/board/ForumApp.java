package jp.co.topgate.atoze.web.app.board;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 掲示板アプリ
 */
public class ForumApp {
    private DataHandler data;
    private List<ForumData> outputData = new ArrayList<>();

    private static final String CSV_FILE_DIRECTORY = "./src/main/resources/program/board/";
    private static final String CSV_FILE_NAME = "save.csv";

    ForumApp() throws IOException {
        data = new DataHandler(new File(CSV_FILE_DIRECTORY, CSV_FILE_NAME));
        outputData = data.getData();
    }

    void setForumDataFile(File file) throws IOException {
        data = new DataHandler(file);
        outputData = data.getData();
    }

    /**
     * 新規に投稿された時
     */
    void createThread(Map<String, String> query, String encode) throws IOException {
        ForumData data = generateNewThreadData(query, encode);
        outputData.add(data);
        this.data.save(data);
    }

    /**
     * 検索時
     */
    void findThread(String name, String encoder) throws IOException {
        List<ForumData> list = data.getData();
        List<ForumData> data = new ArrayList<>();
        if (name != null) {
            for (int i = 0; i < list.size(); i++) {
                String nameData = list.get(i).getName();
                if (name.equals(URLDecoder.decode(nameData, encoder)) || name.equals(nameData)) {
                    data.add(list.get(i));
                }
            }
        }
        outputData = data;
    }

    //TODO:IDから見るデータとインデックスから見る処理を統合したい

    /**
     * 保存しているデータのID値から一致したものを削除
     */
    void deleteThreadByID(String id, String requestPassword) throws IOException {
        if (!isNumber(id)) {
            return;
        }
        List<ForumData> list = data.getData();
        if (list.size() <= 0) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            if (Integer.parseInt(id) == (list.get(i).getId())) {
                BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
                String savedPassword = list.get(i).getPassword();
                if (bCrypt.matches(requestPassword, savedPassword)) {
                    list.remove(i);
                    outputData = list;
                    data.overWrite(list);
                }
            }
        }
    }

    /**
     * Listのインデックス番号を指定し削除
     */
    void deleteThreadByListIndex(String id, String requestPassword) throws IOException {
        if (!isNumber(id)) {
            return;
        }
        int listIndex = Integer.parseInt(id);
        List<ForumData> list = data.getData();
        if (list.size() <= 0) {
            return;
        }
        if (listIndex >= 0 && listIndex < list.size()) {
            if (list.get(listIndex).getPassword().isEmpty()) {
                return;
            }
            BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
            String savedPassword = list.get(listIndex).getPassword();
            if (bCrypt.matches(requestPassword, savedPassword)) {
                list.remove(listIndex);
                outputData = list;
                data.overWrite(list);
            }
        }
    }

    /**
     * 追加するデータをリクエストのクエリ値を元に生成します.
     */
    private ForumData generateNewThreadData(Map<String, String> query, String encode) {
        ForumData newData = new ForumData();
        for (int i = 0; i < ForumDataKey.size(); i++) {
            ForumDataKey pattern = ForumDataKey.getPatternByIndex(i);
            switch (pattern) {
                case ID:
                    newData.setId(retrieveNewID(data.getData()));
                    break;
                case DATE:
                    newData.setDate(date());
                    break;
                case ENCODER:
                    newData.setEncoder(encode);
                    break;
                case PASSWORD:
                    String password = query.get(pattern.getQueryKey());
                    try {
                        password = URLDecoder.decode(password, encode);
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                    newData.setPassword(new BCryptPasswordEncoder().encode(password));
                    break;
                default:
                    newData = DataHandler.insertValueToData(newData, pattern.getIndex(), query.get(pattern.getQueryKey()));
            }
        }
        return newData;
    }

    @NotNull
    List<ForumData> getOutputData() {
        return outputData;
    }

    //直接読み込んだものをそのまま返す
    void threadByCSV() {
        outputData = data.getData();
    }

    private String date() {
        Date date = new Date();
        return date.toString();
    }

    @Contract(pure = true)
    private int retrieveNewID(List<ForumData> list) {
        if (list.size() == 0) {
            return 0;
        }
        return list.get(list.size() - 1).getId() + 1;
    }

    /**
     * 文字列が数字であるか判別します.
     *
     * @param num 文字列
     * @return 文字列が数値であるか否か
     */
    @Contract(pure = true, value = "null -> false")
    private static boolean isNumber(String num) {
        try {
            Integer.parseInt(num);
            return true;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }
}
