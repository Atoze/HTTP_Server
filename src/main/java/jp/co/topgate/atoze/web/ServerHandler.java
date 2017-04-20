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

    public void handle(InputStream in, OutputStream out, int PORT) throws IOException {
        this.request.readRequestText(in, this.hostname + ":" + PORT);
        System.out.println(this.request.getHeaderText());

        String host = this.request.getRequestValue("Host");
        if (host == null || this.request.getMethod() == null || !host.startsWith(this.hostname + ":" + PORT)) {
            setError(400);
            this.response.writeTo(out, this.status);
            return;
        }

        if (!this.request.getMethod().equals("GET")) {
            setError(405);
            this.response.writeTo(out, this.status);
            return;
        }

        String filePath = "." + this.request.getFilePath();

        if (filePath.endsWith("/")) {
            filePath += "index.html";
        }

        File file = new File(filePath);

        if(!file.exists() || !file.isFile()){
            setError(404);
            this.response.writeTo(out, this.status);
            return;
        }

        if(!file.canRead()){
            setError(403);
            this.response.writeTo(out, this.status);
            return;
        }

        this.status.setStatus(200);
        this.response.addText("Content-Type", ContentTypeUtil.getContentType(filePath));
        this.response.setResponseBody(file);
        this.response.writeTo(out, this.status);

        System.out.println(this.response.getResponse());

    }

    private void setError(int error) {
        status.setStatus(error);
        File file = new File(error + ".html");
        if (file.exists() && file.isFile() && file.canRead()) {
            this.response.setResponseBody(file);
        } else {
            this.response.setResponseBody("<html><head><title>" + status.getStatus() + "</title></head><body><h1>" +
                    status.getStatus() + "</h1></body></html>");
        }
    }
}