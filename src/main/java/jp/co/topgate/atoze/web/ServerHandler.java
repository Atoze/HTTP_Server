package jp.co.topgate.atoze.web;

import java.io.*;

/**
 * Created by atoze on 2017/04/12.
 */
class ServerHandler {

    private final String hostname = "localhost";
    private FileLook look = new FileLook();
    private HTTPResponse response = new HTTPResponse();
    private HTTPRequest request = new HTTPRequest();
    private Status status = new Status();

    public ServerHandler(InputStream in, OutputStream out, Integer PORT) throws IOException {
        request.setRequestText(in);
        System.out.println(request.getHeaderText());
        PrintWriter writer = new PrintWriter(out, true);
        ContentType contentType = new ContentType();

        System.out.println("Responding...");

        String host = request.getSpecificRequestLine("Host");

        if (host == null || request.getMethod() == null || !host.startsWith("Host: " + this.hostname + ":" + PORT.toString())) {
            setError(400);
            response.writeTo();
            writer.println(response.getResponse());
            return;
        }

        if(!request.getMethod().equals("GET")){
            setError(405);
            response.writeTo();
            writer.println(response.getResponse());
            return;
        }
        String filepath = "." + request.getFilePath();

            if (filepath.endsWith("/")) {
                filepath += "index.html";
            }
            File file = new File(filepath);
            System.out.println(filepath);
            contentType.setContentType(filepath);

            if (look.ifcheckFile(file)) {
                this.status.setStatus(200);
                response.addLine("Content-Type", contentType.getContentType());
                //response.addLine("Content-Length", file.length());
                response.setResponseBody(file);
                response.writeTo();
                writer.println(response.getResponse());

                BufferedInputStream bi
                        = new BufferedInputStream(new FileInputStream(file));
                try {
                    for (int c = bi.read(); c >= 0; c = bi.read()) {
                        out.write(c);
                    }
                } catch (IOException e) {
                    //setError(500);
                    e.printStackTrace();
                } finally {
                    if (bi != null) {
                        bi.close();
                    }
                }
            } else {
                setError(404);
                response.writeTo();
                writer.println(response.getResponse());
            }
        System.out.println(response.getResponse());
        }


    private void setError(Integer error) {
        this.status.setStatus(error);
        File file = new File(error.toString() + ".html");
        if (file.exists() && file.isFile() && file.canRead()) {
            response.setResponseBody(file);
        } else {
            response.setResponseBody("<html><head><title>" + this.status.getStatus() + "</title></head><body><h1>" +
                    this.status.getStatus() + "</h1></body></html>");
        }
    }
}