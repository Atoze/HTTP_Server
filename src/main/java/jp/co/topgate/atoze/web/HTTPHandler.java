package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.HTMLEditor.HTML5Editor;
import jp.co.topgate.atoze.web.Util.ContentType;
import jp.co.topgate.atoze.web.Util.Status;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.*;

/**
 * Created by atoze on 2017/05/01.
 */
public abstract class HTTPHandler {

    protected HTTPRequest request;
    protected HTTPResponse response = new HTTPResponse();
    protected int statusCode;

    public void request(HTTPRequest request) throws IOException {
        System.out.println("\nRequest incoming..." + Thread.currentThread().getName());
        this.request = request;
    }

    public abstract void generateResponse();

    protected void generateErrorPage(int statusCode) {
        Status status = new Status();
        status.setStatus(statusCode);
        File errorFile = new File(Server.ROOT_DIRECTORY, statusCode + ".html");
        if (errorFile.exists() && errorFile.isFile() && errorFile.canRead()) {
            response.addResponseHeader("Content-Type", ContentType.getContentType(".html") + "; charset=" + detectFileEncoding(errorFile));
            response.setResponseBody(errorFile);
        } else {
            response.addResponseHeader("Content-Type", ContentType.getContentType(".html") + "; charset=UTF-8");
            HTML5Editor html = new HTML5Editor();
            html.setTitle(status.getStatus());
            html.setBody("<h1>" + status.getStatus() + "</h1>");
            response.setResponseBody(html.getHTML());
        }
    }

    protected static String detectFileEncoding(File file) {
        String result = null;
        byte[] buf = new byte[4096];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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

    public void response(OutputStream out) throws IOException {
        generateResponse();
        response.writeTo(out, statusCode);
    }

}