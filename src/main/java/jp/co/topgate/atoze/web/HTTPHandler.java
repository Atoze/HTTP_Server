package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.htmlEditor.HTMLEditor;
import jp.co.topgate.atoze.web.util.ContentType;
import jp.co.topgate.atoze.web.util.Status;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.*;

/**
 * HTTPを受け取った際に、実行されるハンドラーのAbstractClass
 */
public abstract class HTTPHandler {
    public HTTPHandler(HTTPRequest request) {
    }

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
            HTMLEditor html = new HTMLEditor();
            //html.setTitle(status.getStatus());
            html.setBody("<h1>" + status.getStatus() + "</h1>");
            response.setResponseBody(html.getHTML());
        }
        return response;
    }

    /**
     * ファイルのエンコード値を求めます.
     *
     * @param file 判別するファイル
     * @return エンコード値
     */
    protected static String detectFileEncoding(File file) {
        String result = null;
        byte[] buf = new byte[4096];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return null;
        }
        UniversalDetector detector = new UniversalDetector(null);

        int nread;
        try {
            while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        detector.dataEnd();

        result = detector.getDetectedCharset();
        detector.reset();

        return result;
    }

    public void response(OutputStream out) {
        HTTPResponse response = generateResponse();
        response.writeTo(out);
    }
}