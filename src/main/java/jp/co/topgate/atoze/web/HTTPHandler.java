package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.htmlEditor.HTMLBuilder;
import jp.co.topgate.atoze.web.util.ContentType;
import jp.co.topgate.atoze.web.util.Status;

import java.io.File;

import static jp.co.topgate.atoze.web.util.FileUtil.detectFileEncoding;

/**
 * HTTPを受け取った際に、実行されるハンドラーのAbstractClass
 */
public abstract class HTTPHandler {

    public abstract HTTPResponse generateResponse();

    /**
     * エラーページを生成し設定します.
     *
     * @param statusCode ステータスコードの値
     */
    protected HTTPResponse generateErrorResponse(int statusCode) {
        HTTPResponse response = new HTTPResponse(statusCode);
        Status status = new Status();
        status.setStatus(statusCode);
        File errorFile = new File(Server.ROOT_DIRECTORY, statusCode + ".html");
        if (errorFile.exists() && errorFile.isFile() && errorFile.canRead()) {
            response.addResponseHeader("Content-Type", ContentType.getContentType(".html") + "; charset=" + detectFileEncoding(errorFile));
            response.setResponseBody(errorFile);
        } else {
            response.addResponseHeader("Content-Type", ContentType.getContentType(".html") + "; charset=UTF-8");
            HTMLBuilder html = new HTMLBuilder();
            //html.setTitle(status.getStatus());
            html.setBody("<h1>" + status.getStatus() + "</h1>");
            response.setResponseBody(html.getHTML());
        }
        return response;
    }
}