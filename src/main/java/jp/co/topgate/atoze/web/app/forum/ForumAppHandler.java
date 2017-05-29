package jp.co.topgate.atoze.web.app.forum;

import jp.co.topgate.atoze.web.HTTPHandler;
import jp.co.topgate.atoze.web.HTTPRequest;
import jp.co.topgate.atoze.web.HTTPResponse;
import jp.co.topgate.atoze.web.URLPattern;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * 掲示板の挙動を制御します.
 */
public class ForumAppHandler extends HTTPHandler {
    private ForumApp forum;

    private final Map<String, String> QUERY;
    private final String FILE_PATH;
    private final String HOST;

    private String ENCODER = "UTF-8";
    private String html;

    private int statusCode = 200;

    private static String SEARCH = "search";

    public ForumAppHandler(HTTPRequest request) {
        FILE_PATH = request.getFilePath();
        QUERY = request.getQuery();
        HOST = request.getHost();

        if (request.getHeaderParam("Content-Type") != null) {
            String[] encode = request.getHeaderParam("Content-Type").split(";");
            if (encode.length >= 2 && encode[1].trim().startsWith("charset=")) {
                ENCODER = checkEncode(encode[1].substring("charset=".length() + 1).trim());
            }
        }

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
        String path = FILE_PATH.replaceFirst(URLPattern.PROGRAM_BOARD.getURL(), "");
        if (path.startsWith("search") && getQueryParam("search") != null) {
            forum.findThread(getQueryParam("search"), ENCODER);
            return;
        }
        if (path.equals("index.html") || path.equals("")) {
            forum.threadByCSV();
            return;
        }
        statusCode = 404;
    }

    private void handlerPOST() throws IOException {
        if (getQueryParam("search") != null) {
            forum.findThread(getQueryParam("search"), ENCODER);
            return;
        }
        if ("DELETE".equals(getQueryParam("_method"))) {
            String threadID;
            String requestPassword = getQueryParam(ForumDataPattern.PASSWORD.getQueryKey());
            if ((threadID = getQueryParam("tableIndex")) != null) {
                forum.deleteThreadByListIndex(threadID, requestPassword);
            } else if ((threadID = getQueryParam("threadID")) != null) {
                if (ForumData.isNumber(threadID)) {
                    forum.deleteThreadByID(threadID, requestPassword);
                }
            }
            return;
        }
        statusCode = 303;
        forum.createThread(QUERY, ENCODER);
    }

    /**
     * レスポンスを生成します.
     */
    @Override
    public HTTPResponse generateResponse() {
        if (statusCode != 200 && statusCode != 303) {
            return generateErrorResponse(statusCode);
        }
        HTTPResponse response = new HTTPResponse(statusCode);
        if (statusCode == 303) {
            response.addResponseHeader("Location", "http://" + HOST + URLPattern.PROGRAM_BOARD.getURL());
        }
        response.setResponseBody(html);
        return response;
    }

    private String checkEncode(String encode) {
        try {
            URLDecoder.decode("", encode);
            return encode;
        } catch (UnsupportedEncodingException e) {
            return "UTF-8";
        }
    }

    private String getQueryParam(String key) {
        try {
            return URLDecoder.decode(QUERY.getOrDefault(key, null), ENCODER);
        } catch (NullPointerException e) {
            return null;
        } catch (UnsupportedEncodingException e) {
            return QUERY.getOrDefault(key, null);
        }
    }
}
