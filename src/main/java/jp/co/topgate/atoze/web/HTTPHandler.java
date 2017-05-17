package jp.co.topgate.atoze.web;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.*;

/**
 * Created by atoze on 2017/05/01.
 */
abstract class HTTPHandler {

    HTTPRequest request;
    HTTPResponse response = new HTTPResponse();
    int statusCode;

    public void request(HTTPRequest request) throws IOException {
        System.out.println("\nRequest incoming..." + Thread.currentThread().getName());
        this.request = request;
    }

    abstract void generateResponse();

    void generateErrorPage(int statusCode) {
        Status status = new Status();
        status.setStatus(statusCode);
        File errorFile = new File(Server.ROOT_DIRECTORY, statusCode + ".html");
        if (errorFile.exists() && errorFile.isFile() && errorFile.canRead()) {
            response.addResponseHeader("Content-Type", ContentTypeUtil.getContentType(".html") + "; charset=" + detectFileEncoding(errorFile));
            response.setResponseBody(errorFile);
        } else {
            response.addResponseHeader("Content-Type", ContentTypeUtil.getContentType(".html") + "; charset=UTF-8");
            HTML5Generator html = new HTML5Generator();
            html.setTitle(status.getStatus());
            html.setBody("<h1>" + status.getStatus() + "</h1>");
            response.setResponseBody(html.getHTML());
        }
    }

    String detectFileEncoding(File file) {
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