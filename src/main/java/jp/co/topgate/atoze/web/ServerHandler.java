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

    public ServerHandler(InputStream in, OutputStream out, Integer PORT) throws IOException {
        request.setRequestText(in);
        System.out.println(request.getHeaderText());
        PrintWriter writer = new PrintWriter(out, true);
        ContentType contentType = new ContentType();

        System.out.println("Responding...");

        String host = request.getSpecificRequestLine("Host");
        if (host != null && host.startsWith("Host: " + this.hostname + ":" + PORT.toString())) {

            if (request.getMethod() != null && request.getMethod().equals("GET")) {

                String filepath = "." + request.getFilePath();

                if (filepath.endsWith("/")) {
                    filepath += "index.html";
                }
                File file = new File(filepath);
                contentType.setContentType(filepath);

                if (look.ifcheckFile(file)) {
                    Status.setStatus(200);
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
            } else {
                setError(400);
                response.writeTo();
                writer.println(response.getResponse());
            }
        } else {
            setError(400);
            response.writeTo();
            writer.println(response.getResponse());
        }

    }

    private void setError(Integer error) {
        Status.setStatus(error);
        File file = new File(error.toString() + ".html");
        if (file.exists() && file.isFile() && file.canRead()) {
            response.setResponseBody(file);
        } else {
            response.setResponseBody("<html><head><title>" + Status.getStatus() + "</title></head><body><h1>" +
                    Status.getStatus() + "</h1></body></html>");
        }
    }

    private void getRequestText() {

    }


}