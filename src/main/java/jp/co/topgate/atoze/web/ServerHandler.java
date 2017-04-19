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
    ServerHandler(){}



    public void ServerHandler2(InputStream in, OutputStream out, Integer PORT) throws IOException {
        request.setRequestText(in);
        System.out.println(request.getHeaderText());

        System.out.println("Responding...");
        String host = request.getRequestValue("Host");

        if (host == null || request.getMethod() == null || !host.startsWith(this.hostname + ":" + PORT.toString())) {
            setError(400);
            response.writeTo(out, this.status);
            return;
        }

        if (!request.getMethod().equals("GET")) {
            setError(405);
            response.writeTo(out, this.status);
            return;
        }

        String filePath = "." + request.getFilePath();

        if (filePath.endsWith("/")) {
            filePath += "index.html";
        }

        File file = new File(filePath);

        if(!file.canRead()){
            setError(403);
            response.writeTo(out, this.status);
            return;
        }

        if(!file.exists() || !file.isFile()){
            setError(404);
            response.writeTo(out, this.status);
            return;
        }

        //if (checkFile(file)) {
            this.status.setStatus(200);
            response.addText("Content-Type", ContentTypeUtil.getContentType(filePath));
            response.setResponseBody(file);
            response.writeTo(out, this.status);
        /*} else {
            setError(404);
            response.writeTo(out, this.status);
        }*/
        System.out.println(response.getResponse());
    }
    public void handleIn(InputStream in){
        request.setRequestText(in);
        System.out.println(request.getHeaderText());
    }

    public void handleOut(OutputStream out , int PORT) throws IOException {
        String host = request.getRequestValue("Host");
        if (host == null || request.getMethod() == null || !host.startsWith(this.hostname + ":" + PORT)) {
            setError(400);
            response.writeTo(out, this.status);
            return;
        }

        if (!request.getMethod().equals("GET")) {
            setError(405);
            response.writeTo(out, this.status);
            return;
        }

        String filePath = "." + request.getFilePath();

        if (filePath.endsWith("/")) {
            filePath += "index.html";
        }

        File file = new File(filePath);

        if(!file.canRead()){
            setError(403);
            response.writeTo(out, this.status);
            return;
        }

        if(!file.exists() || !file.isFile()){
            setError(404);
            response.writeTo(out, this.status);
            return;
        }

        //if (checkFile(file)) {
        this.status.setStatus(200);
        response.addText("Content-Type", ContentTypeUtil.getContentType(filePath));
        response.setResponseBody(file);
        response.writeTo(out, this.status);
        /*} else {
            setError(404);
            response.writeTo(out, this.status);
        }*/
        System.out.println(response.getResponse());

    }




    private void setError(int error) {
        this.status.setStatus(error);
        File file = new File(error + ".html");
        if (file.exists() && file.isFile() && file.canRead()) {
            response.setResponseBody(file);
        } else {
            response.setResponseBody("<html><head><title>" + status.getStatus() + "</title></head><body><h1>" +
                    status.getStatus() + "</h1></body></html>");
        }
    }

    /*
    private boolean checkFile(File file) {
        if (file.exists() && file.isFile()) {
                return true;
            }
        return false;
    }
    */
}