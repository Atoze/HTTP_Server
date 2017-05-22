package jp.co.topgate.atoze.web.app.forum;

import jp.co.topgate.atoze.web.HTTPHandler;
import jp.co.topgate.atoze.web.HTTPRequest;
import jp.co.topgate.atoze.web.HTTPResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

/**
 * 掲示板の挙動を制御します.
 */
public class ForumAppHandler extends HTTPHandler {
    private final ForumApp forum;

    private final Map<String, String> query;
    private final String filePath;
    private final String HOST;

    private String html;

    public ForumAppHandler(HTTPRequest request) {
        super(request);
        filePath = request.getFilePath();
        query = request.getQuery();
        HOST = request.getHost();
        forum = new ForumApp();
        try {
            handle(request.getMethod());
            statusCode = 200;
        } catch (IOException e) {
            statusCode = 500;
        }
    }

    /**
     * 受け取ったリクエストのメソッドに基づいて処理を分岐します.
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

    private void handlerGET() throws IOException {
        if (filePath.endsWith("search?")) {
            forum.findThread(getQueryParam("search"));
        }
        //forum.showThread();
    }

    private void handlerPOST() throws IOException {
        if (!getQueryParam("search").isEmpty()) {
            forum.findThread(getQueryParam("search"));
            return;
        }
        if (getQueryParam("_method").equals("DELETE")) {
            if (ForumData.isNumber(getQueryParam("threadID"))) {
                int id = Integer.parseInt(getQueryParam("threadID"));
                if (id <= forum.getMainData().size() - 1) {
                    forum.deleteThread(id, getQueryParam("password"));
                    return;
                }
            }
            System.out.println("範囲外です");
            return;
        }
        forum.createThread(query);
    }

    /**
     * レスポンスを生成します.
     */
    @Override
    public HTTPResponse generateResponse() {
        if (statusCode == 200) {
            HTTPResponse response = new HTTPResponse();
            response.addResponseHeader("Content-Type", "text/html; charset=UTF-8");
            response.setResponseBody(html);
            return response;
        } else {
            return generateErrorResponse(statusCode);
        }
    }

    @NotNull
    private String getQueryParam(String key) {
        return query.getOrDefault(key, "");
    }
}
