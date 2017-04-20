package jp.co.topgate.atoze.web;

import java.io.*;

/**
 * Created by atoze on 2017/04/12.
 */
class ServerHandler {

    private final String hostname = "localhost";
    private HTTPResponse response = new HTTPResponse();
    private HTTPRequest request = new HTTPRequest();
    private Status status = new Status();
    private int PORT;

    ServerHandler(int PORT) {
        this.PORT = PORT;
    }

    private int checkHTTPCode() throws IOException {
        String host = this.request.getRequestValue("Host");
        if (host == null || this.request.getMethod() == null || !host.startsWith(this.hostname + ":" + PORT)) {
            return 400;
        }
        if (!this.request.getMethod().equals("GET")) {
            return 405;
        }

        String filePath = "." + this.request.getFilePath();
        if (filePath.endsWith("/")) {
            filePath += "index.html";
        }

        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            return 404;
        }
        if (!file.canRead()) {
            return 403;
        }

        this.response.addText("Content-Type", ContentTypeUtil.getContentType(filePath));
        this.response.setResponseBody(file);
        return 200;
    }

    public void handleIn(InputStream in) throws IOException {
        this.request.readRequestText(in, this.hostname + ":" + PORT);
        System.out.println(this.request.getHeaderText());
    }

    public void handleOut(OutputStream out) throws IOException {
        this.status.setStatus(this.checkHTTPCode());
        if (this.checkHTTPCode() != 200) {
            this.setError(checkHTTPCode());
        }
        this.response.writeTo(out, this.status);
    }

    private void setError(int error) {
        File file = new File(error + ".html");
        if (file.exists() && file.isFile() && file.canRead()) {
            this.response.setResponseBody(file);
        } else {
            this.response.setResponseBody("<html><head><title>" + status.getStatus() + "</title></head><body><h1>" +
                    status.getStatus() + "</h1></body></html>");
        }
    }
}