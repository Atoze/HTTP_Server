package jp.co.topgate.atoze.web.app.forum;

import jp.co.topgate.atoze.web.HTTPHandler;
import jp.co.topgate.atoze.web.HTTPRequest;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

/**
 * 掲示板の挙動を制御します.
 */
public class ForumAppHandler extends HTTPHandler {
    private final ForumApp forum;

    private Map<String, String> query;
    private String filePath;
    private String HOST;

    private String html;

    public ForumAppHandler() throws IOException {
        super();
        forum = new ForumApp();
    }

    @Override
    public void setRequest(HTTPRequest request) {
        filePath = request.getFilePath();
        HOST = request.getHost();
        query = request.getQuery();
    }

    /**
     * 受け取ったリクエストのメソッドに基づいて処理を分岐します.
     */
    public void handle(String method) throws IOException {
        switch (method) {
            case "GET":
                methodGetHandler();
                break;
            case "POST":
                methodPostHandler();
                break;
        }
        html = new ForumHTML(HOST).getIndexHTML(forum.getMainData());
    }

    private void methodGetHandler() throws IOException {
        if (filePath.endsWith("search?")) {
            forum.findThread(getQueryParam("search"));
            return;
        }
        forum.methodGetHandler();
    }

    private void methodPostHandler() throws IOException {
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
        forum.newThread(query);

    }

    /**
     * レスポンスを生成します.
     */
    @Override
    public void generateResponse() {
        response.addResponseHeader("Content-Type", "text/html; charset=UTF-8");

        if (statusCode == 0 || statusCode == 200) {
            response.setResponseBody(html);
        } else {
            generateErrorPage(statusCode);
        }
    }

    @NotNull
    private String getQueryParam(String key) {
        return query.getOrDefault(key, "");
    }
}
