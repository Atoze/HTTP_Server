package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.util.ContentType;
import jp.co.topgate.atoze.web.util.HTMLBuilder;
import jp.co.topgate.atoze.web.util.Status;

import java.io.File;
import java.io.FileNotFoundException;

import static jp.co.topgate.atoze.web.util.FileUtil.detectFileEncoding;

/**
 * HTTPを受け取った際に、実行されるハンドラーのAbstractClass
 */
public abstract class HTTPHandler {

    private final static String SYSTEM_CHARSET = System.getProperty("file.encoding");
    public abstract HTTPResponse generateResponse();

    /**
     * エラーページを生成し設定します.
     */
    protected HTTPResponse generateErrorResponse(Status status) {
        HTMLBuilder html;
        switch (status) {
            case NOT_FOUND:
                html = new HTMLBuilder();
                html.setTitle("ファイルが見つかりません");
                html.setBody("現在お探しのページは、存在していません。");
                break;
            default:
                html = new HTMLBuilder();
                html.setTitle(status.getCode() + status.getMessage());
                html.setBody(html.generateField("h1", status.getCode() + status.getMessage()));
                return generateErrorResponse(status, html.toString());
        }
        return generateErrorResponse(status, html.toString());
    }

    protected HTTPResponse generateErrorResponse(Status status, String contentHTML) {
        return generateErrorResponse(status.getCode(), status.getMessage(), contentHTML);
    }

    protected HTTPResponse generateErrorResponse(int statusCode, String statusMessage, String contentHTML) {
        ExtendedHTTPResponse response = new ExtendedHTTPResponse(statusCode, statusMessage);
        File errorFile = new File(Server.ROOT_DIRECTORY, statusCode + ".html");
        try {
            response.setResponseBody(errorFile, ContentType.getContentType(errorFile.toString()), detectFileEncoding(errorFile));
        } catch (FileNotFoundException e) {
            response.setContentType(ContentType.getContentType(".html"), SYSTEM_CHARSET);
            response.setResponseBody(contentHTML);
        }
        return response;
    }
}