package jp.co.topgate.atoze.web;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by atoze on 2017/04/24.
 */
public class HTTPHandler {

    HTTPHandler() {
    }

    public void handlerGET(int statusCode, File file, OutputStream out) throws IOException {
        HTTPResponse response = new HTTPResponse();
        Status status = new Status();
        status.setStatus(statusCode);
        if (statusCode == 200) {
            response.addResponseHeader("Content-Type", ContentTypeUtil.getContentType(file.toString()));
            response.setResponseBody(file);
            response.writeTo(out, status);
        } else {
            this.handlerError(statusCode, out);
        }
    }

    /**
     * エラー発生時のステータスコードに合わせたページを設定、またはテンプレートを作成します.
     * 設置したホームディレクトリの "ステータスコード".html を参照します.
     * 存在しない場合は、テンプレートを送信します.
     *
     * @param statusCode 　ステータスコード
     * @param out        書き出し先
     */
    public void handlerError(int statusCode, OutputStream out) throws IOException {
        HTTPResponse response = new HTTPResponse();
        Status status = new Status();
        status.setStatus(statusCode);
        File errorFile = new File(statusCode + ".html");
        if (errorFile.exists() && errorFile.isFile() && errorFile.canRead()) {
            response.setResponseBody(errorFile);
        } else {
            response.addResponseHeader("Content-Type", ContentTypeUtil.getContentType(".html"));
            response.setResponseBody("<html><head><title>" + status.getStatus() + "</title></head><body><h1>" +
                    status.getStatus() + "</h1></body></html>");
        }
        response.writeTo(out, status);
    }
}
