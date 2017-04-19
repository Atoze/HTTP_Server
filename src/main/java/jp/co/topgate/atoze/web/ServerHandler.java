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


    public ServerHandler(InputStream in, OutputStream out, Integer PORT) throws IOException {
        request.setRequestText(in);
        System.out.println(request.getHeaderText());
        PrintWriter writer = new PrintWriter(out, true);
        ContentType contentType = new ContentType();

        System.out.println("Responding...");

        String host = request.getSpecificRequestLine("Host");

        if (host == null || request.getMethod() == null || !host.startsWith("Host: " + this.hostname + ":" + PORT.toString())) {
            setError(400);
            response.writeTo(400);
            writer.println(response.getResponse());
            return;
        }

        if (!request.getMethod().equals("GET")) {
            setError(405);
            response.writeTo(405);
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

        if (checkFile(file)) {
            response.addLine("Content-Type", contentType.getContentType(filepath));
            //response.addLine("Content-Length", file.length());
            response.setResponseBody(file);
            response.writeTo(200);
            writer.println(response.getResponse());

            BufferedInputStream bi
                    = new BufferedInputStream(new FileInputStream(file));
            try {
                for (int c = bi.read(); c >= 0; c = bi.read()) {
                    out.write(c);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (bi != null) {
                    bi.close();
                }
            }
        } else {
            setError(404);
            response.writeTo(404);
            writer.println(response.getResponse());
        }
        System.out.println(response.getResponse());
    }


    private void setError(Integer error) {
        status.setStatus(error);
        File file = new File(error.toString() + ".html");
        if (file.exists() && file.isFile() && file.canRead()) {
            response.setResponseBody(file);
        } else {
            response.setResponseBody("<html><head><title>" + status.getStatus() + "</title></head><body><h1>" +
                    status.getStatus() + "</h1></body></html>");
        }
    }

    private boolean checkFile(File file) {
        if (file.exists()) {
            if (file.isFile() && file.canRead()) {
                return true;
            }
        }
        return false;
    }
}