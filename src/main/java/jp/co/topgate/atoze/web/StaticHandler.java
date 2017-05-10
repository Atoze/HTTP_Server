package jp.co.topgate.atoze.web;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import static jp.co.topgate.atoze.web.Server.ROOT_DIRECTORY;

/**
 * Created by atoze on 2017/05/01.
 */
public class StaticHandler extends HTTPHandler {

    File file;

    private int statusCode;
    private String filePath;
    private String HOST;

    StaticHandler(String filePath, String host) throws IOException {
        this.HOST = host;
        this.filePath = filePath;
    }

    public void response(OutputStream out) throws IOException {
        this.file = new File(ROOT_DIRECTORY, this.filePath);
        statusCode = checkStatusCode(request, this.file);

        Status status = new Status();
        status.setStatus(statusCode);

        if (statusCode == 200) {
            if (Arrays.asList("html", "txt").contains(ContentTypeUtil.getFileExtension(file.toString()))) {
                response.addResponseHeader("Content-Type", ContentTypeUtil.getContentType(file.toString()) + "; charset=" + detectFileEncoding(file));
            } else {
                response.addResponseHeader("Content-Type", ContentTypeUtil.getContentType(file.toString()));
            }
            response.setResponseBody(file);
            response.writeTo(out, statusCode);
        } else {
            this.handlerError(out);
        }
        System.out.println(response.getResponse());
    }

    private void handlerError(OutputStream out) throws IOException {
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
        response.writeTo(out, statusCode);
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

    /**
     * HTTPリクエストに応じてステータスコードを設定します.
     *
     * @param request 　HTTPリクエスト
     * @param file    要求されたファイルパス
     * @return ステータスコード
     */
    private int checkStatusCode(HTTPRequest request, File file) throws IOException {
        String host = request.getHeaderParam("HOST");
        if (host == null || !host.startsWith(HOST)) {
            return 400;
        }
        if (!file.exists() || !file.isFile()) {
            return 404;
        }
        if (!file.canRead()) {
            return 403;
        }
        return 200;
    }

}
