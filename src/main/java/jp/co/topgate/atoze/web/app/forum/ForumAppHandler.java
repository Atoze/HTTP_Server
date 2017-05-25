package jp.co.topgate.atoze.web.app.forum;

import jp.co.topgate.atoze.web.HTTPHandler;
import jp.co.topgate.atoze.web.HTTPRequest;
import jp.co.topgate.atoze.web.HTTPResponse;

import java.io.IOException;
import java.util.Map;

/**
 * 掲示板の挙動を制御します.
 */
public class ForumAppHandler extends HTTPHandler {
    private ForumApp forum;

    private final Map<String, String> query;
    private final String filePath;
    private final String HOST;

    private String html;

    private int statusCode;

    private static String SEARCH="search";


    public ForumAppHandler(HTTPRequest request) {
        filePath = request.getFilePath().replaceFirst("/program/board/","");
        query = request.getQuery();
        HOST = request.getHost();
        try {
            forum = new ForumApp();
            statusCode = 200;
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
        if (filePath.startsWith("search")) {
            forum.findThread(getQueryParam("search"));
            return;
        }
        if (filePath.equals("index.html")) {
            forum.threadByCSV();
            return;
        }
        statusCode = 404;
    }

    private void handlerPOST() throws IOException {
        if (getQueryParam("search") != null) {
            forum.findThread(getQueryParam("search"));
            return;
        }
        if ("DELETE".equals(getQueryParam("_method"))) {
            String threadID;
            if ((threadID = getQueryParam("tableIndex")) != null) {
                forum.deleteThreadByListIndex(threadID, getQueryParam("password"));
            } else if ((threadID = getQueryParam("threadID")) != null) {
                if (ForumData.isNumber(threadID)) {
                    forum.deleteThreadByID(threadID, getQueryParam("password"));
                }
            }
            return;
        }
        forum.createThread(query);
    }

    /**
     * レスポンスを生成します.
     */
    @Override
    public HTTPResponse generateResponse() {
        if (statusCode != 200) {
            return generateErrorResponse(statusCode);
        }
        HTTPResponse response = new HTTPResponse();
        response.setResponseBody(html);
        return response;
    }

    private String getQueryParam(String key) {
        return query.getOrDefault(key, null);
    }
}
