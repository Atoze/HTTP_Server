package jp.co.topgate.atoze.web;

import java.io.*;

/**
 * Created by atoze on 2017/04/12.
 */
class ResponseHandler {

    private String hostName;
    private int PORT;
    private File file;

    ResponseHandler(String hostName, int PORT) {
        this.hostName = hostName;
        this.PORT = PORT;
    }

    private int checkHTTPCode(HTTPRequest request) throws IOException {
        String host = request.getRequestValue("HOST");
        if (host == null || request.getMethod() == null || !host.startsWith(this.hostName + ":" + PORT)) {
            return 400;
        }
        if (!request.getMethod().equals("GET")) {
            return 405;
        }

        String filePath = "." + request.getFilePath();
        if (filePath.endsWith("/")) {
            filePath += "index.html";
        }

        this.file = new File(filePath);
        if (!this.file.exists() || !this.file.isFile()) {
            return 404;
        }
        if (!this.file.canRead()) {
            return 403;
        }
        return 200;
    }

    public void responseOutput(HTTPRequest request, OutputStream out) throws IOException {
        HTTPResponse response = new HTTPResponse();
        Status status = new Status();
        status.setStatus(this.checkHTTPCode(request));
        if (this.checkHTTPCode(request) == 200) {
            response.addResponseHeader("Content-Type", ContentTypeUtil.getContentType(this.file.toString()));
            response.setResponseBody(this.file);
            response.writeTo(out, status);
            return;
        }
        File file = new File(this.checkHTTPCode(request) + ".html");
        if (file.exists() && file.isFile() && file.canRead()) {
            response.setResponseBody(file);
        } else {
            response.setResponseBody("<html><head><title>" + status.getStatus() + "</title></head><body><h1>" +
                    status.getStatus() + "</h1></body></html>");
        }
        response.writeTo(out, status);
    }
}