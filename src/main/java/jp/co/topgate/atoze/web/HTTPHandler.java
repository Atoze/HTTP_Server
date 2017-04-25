package jp.co.topgate.atoze.web;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * HTTPリクエストを元に、レスポンスを生成する.
 */
class HTTPHandler {

    HTTPHandler() {
    }

    public void handlerGET(int statusCode, File file, OutputStream out) throws IOException {
        HTTPResponse response = new HTTPResponse();
        Status status = new Status();
        status.setStatus(statusCode);

        if (statusCode == 200) {
            if (Arrays.asList("html", "txt").contains(ContentTypeUtil.getFileExtension(file.toString()))) {
                response.addResponseHeader("Content-Type", ContentTypeUtil.getContentType(file.toString()) + "; charset=" + detectFileEncoding(file));
            } else {
                response.addResponseHeader("Content-Type", ContentTypeUtil.getContentType(file.toString()));
            }
            response.setResponseBody(file);
            response.writeTo(out, status);
        } else {
            this.handlerError(statusCode, out);
        }
    }

    /**
     * エラー発生時のステータスコードに合わせたページを設定、またはテンプレートを作成します.
     * ページは設置したホームディレクトリの "ステータスコード".html を参照します.
     * 存在しない場合は、テンプレートを送信します.
     *
     * @param statusCode 　ステータスコード
     * @param out        書き出し先
     */
    public void handlerError(int statusCode, OutputStream out) throws IOException {
        HTTPResponse response = new HTTPResponse();
        Status status = new Status();
        status.setStatus(statusCode);
        File errorFile = new File(Server.ROOT_DIRECTORY, statusCode + ".html");
        if (errorFile.exists() && errorFile.isFile() && errorFile.canRead()) {
            response.addResponseHeader("Content-Type", ContentTypeUtil.getContentType(".html") + "; charset=" + detectFileEncoding(errorFile));
            response.setResponseBody(errorFile);
        } else {
            response.addResponseHeader("Content-Type", ContentTypeUtil.getContentType(".html") + "; charset=UTF-8");
            response.setResponseBody("<html><head><title>" + status.getStatus() + "</title></head><body><h1>" +
                    status.getStatus() + "</h1></body></html>");
        }
        response.writeTo(out, status);
    }

    private String detectFileEncoding(File file) throws IOException {
        String result = null;
        byte[] buf = new byte[4096];
        FileInputStream fis = new FileInputStream(file);
        UniversalDetector detector = new UniversalDetector(null);

        int nread;
        while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
            detector.handleData(buf, 0, nread);
        }
        detector.dataEnd();

        result = detector.getDetectedCharset();
        detector.reset();

        return result;
    }
}
