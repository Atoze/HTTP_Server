package jp.co.topgate.atoze.web.app.forum;

import jp.co.topgate.atoze.web.*;

import java.io.IOException;
import java.util.Map;

/**
 * 掲示板の挙動を制御します.
 */
public class ForumAppHandler extends HTTPHandler {
    private ForumApp forum;

    private final Map<String, String> QUERY;
    private final String PATH;
    private final String HOST;

    private String html;

    private int statusCode = 200;

    private static String SEARCH = "search";

    public ForumAppHandler(HTTPRequest request) {
        PATH = request.getFilePath().replaceFirst(URLPattern.PROGRAM_BOARD.getURL(), "");
        QUERY = request.getQuery();
        HOST = request.getHost();
        try {
            forum = new ForumApp();
            handle(request.getMethod());
        } catch (NullPointerException | IOException e) {
            statusCode = 500;
            e.printStackTrace();
        }
    }

    /**
     * 受け取ったリクエストのメソッドに基づいて処理を分岐します.
     *
     * @param method リクエストされたメソッド
     */
    private void handle(String method) throws IOException {
        switch (method) {
            case "GET":
                handlerGET();
                break;
            case "POST":
                handlerPOST();
                break;
        }
        html = new ForumHTML(HOST).getIndexHTML(forum.getMainData());
    }

    /**
     * "GET"時の処理
     */
    private void handlerGET() throws IOException {
        if (PATH.startsWith("search") && getQueryParam("search") != null) {
            forum.findThread(getQueryParam("search"));
            return;
        }
        if (PATH.equals("index.html")) {
            forum.threadByCSV();
            return;
        }
    }

    private void handlerPOST() throws IOException {
        if (getQueryParam("search") != null) {
            forum.findThread(getQueryParam("search"));
            return;
        }
        if ("DELETE".equals(getQueryParam("_method"))) {
            String threadID;
            if ((threadID = getQueryParam("tableIndex")) != null) {
                forum.deleteThreadByListIndex(threadID, getQueryParam(ForumDataPattern.PASSWORD.getQueryKey()));
            } else if ((threadID = getQueryParam("threadID")) != null) {
                if (ForumData.isNumber(threadID)) {
                    forum.deleteThreadByID(threadID, getQueryParam(ForumDataPattern.PASSWORD.getQueryKey()));
                }
            }
            return;
        }
        statusCode = 303;
        forum.createThread(QUERY);
    }

    /**
     * レスポンスを生成します.
     */
    @Override
    public HTTPResponse generateResponse() {
        if (statusCode != 200 && statusCode != 303) {
            return generateErrorResponse(statusCode);
        }
        System.out.print(statusCode);
        HTTPResponse response = new HTTPResponse(statusCode);
        if (statusCode == 303) {
            response.addResponseHeader("Location", "http://" + HOST + URLPattern.PROGRAM_BOARD.getURL());
        }
        response.setResponseBody(html);
        return response;
    }

    private String getQueryParam(String key) {
        return QUERY.getOrDefault(key, null);
    }
}
