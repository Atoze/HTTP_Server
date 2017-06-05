package jp.co.topgate.atoze.web.app.board;

import jp.co.topgate.atoze.web.*;
import jp.co.topgate.atoze.web.exception.RequestBodyParseException;
import jp.co.topgate.atoze.web.util.Status;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * 掲示板の挙動を制御します.
 */
public class IndexHandler extends HTTPHandler {
    private final static String DEFAULT_ENCODING = "UTF-8";

    private final ForumApp forum;

    private Map<String, String> QUERY;
    private final String PATH;
    private final String HOST;

    private String ENCODER = DEFAULT_ENCODING;
    private String html;

    private Status status = Status.OK;

    private static String SEARCH = "search";
    private static String CHARSET_KEY = "charset=";

    public IndexHandler(HTTPRequest request) throws IOException {
        PATH = request.getPath();
        HOST = request.getHost();
        forum = new ForumApp();

        if (request.getContentType() != null) {
            String[] encode = request.getContentType().split(";",2);
            if (encode.length >= 2 && encode[1].trim().startsWith(CHARSET_KEY)) {
                ENCODER = checkEncode(encode[1].substring(CHARSET_KEY.length() + 1).trim());
            }
        }
        try {
            QUERY = request.getFormQuery();
            handle(request.getMethod());
        } catch (RequestBodyParseException e) {
            status = Status.INTERNAL_SERVER_ERROR;
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
        html = new ForumHTML(HOST).getIndexHTML(forum.getOutputData());
    }

    /**
     * "GET"時の処理
     */
    private void handlerGET() throws IOException {
        String path = PATH.replaceFirst(Server.BOARD_APP_DIRECTORY, "");

        if (path.startsWith("search") && getQueryParam("search") != null) {
            forum.findThread(getQueryParam("search"), ENCODER);
            return;
        }
        if (path.equals("index.html") || path.equals("")) {
            forum.threadByCSV();
            return;
        }
        status = Status.NOT_FOUND;
    }

    private void handlerPOST() throws IOException {
        if (getQueryParam("search") != null) {
            forum.findThread(getQueryParam("search"), ENCODER);
            return;
        }
        status = Status.SEE_OTHER;
        if ("DELETE".equals(getQueryParam("_method"))) {
            String threadID;
            String requestPassword = getQueryParam(ForumDataKey.PASSWORD.getQueryKey());
            if ((threadID = getQueryParam("tableIndex")) != null) {
                forum.deleteThreadByListIndex(threadID, requestPassword);
            } else if ((threadID = getQueryParam("threadID")) != null) {
                forum.deleteThreadByID(threadID, requestPassword);
            }
            return;
        }
        forum.createThread(QUERY, ENCODER);
    }

    /**
     * レスポンスを生成します.
     */
    @Override
    public HTTPResponse generateResponse() {
        if (status != Status.OK && status != Status.SEE_OTHER) {
            return generateErrorResponse(status);
        }
        HTTPResponse response = new HTTPResponse(status.getCode(), status.getMessage());
        if (status == Status.SEE_OTHER) {
            response.addResponseHeader("Location", "http://" + HOST + Server.BOARD_APP_DIRECTORY);
        }
        response.setResponseBody(html);
        return response;
    }

    private String checkEncode(String encode) {
        try {
            URLDecoder.decode("", encode);
            return encode;
        } catch (UnsupportedEncodingException e) {
            return DEFAULT_ENCODING;
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
