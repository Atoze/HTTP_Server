package jp.co.topgate.atoze.web;

import java.io.*;

/**
 * Created by atoze on 2017/04/12.
 */
class ServerHandler {

    String hostname = "localhost";

    private FileLook look = new FileLook();
    HTTPResponseGenerate response = new HTTPResponseGenerate();

    public ServerHandler(InputStream in, OutputStream out) throws IOException {

        String bodyText;
        HTTPRequest request = new HTTPRequest(in);
        System.out.println(request.getHeaderText());
        PrintWriter writer = new PrintWriter(out, true);
        ContentType contentType = new ContentType();

        HTTPResponseGenerate response = new HTTPResponseGenerate();
        System.out.println("Responding...");

        if (request.getMethod()!=null && request.getMethod().equals("GET")) {

            
            File file = new File(request.getFilePath());
            contentType.setContentType(request.getFilePath());

            if (look.ifcheckFile(file)) {
                Status.setStatusCode(200);
                response.addLine("Content-Type", contentType.getContentType());
                //response.addLine("Content-Length", contentType.getContentType());
                response.setResponseBody(file);
                response.writeTo(out);
                writer.println(response.getResponse());

                BufferedInputStream bi
                        = new BufferedInputStream(new FileInputStream(file));
                try {
                    for (int c = bi.read(); c >= 0; c = bi.read()) {
                        out.write(c);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (bi != null) {
                        bi.close();
                    }
                }
            } else {
                response.setError(404);
            }
        } else {
            response.setError(400);
        }
        response.writeTo(out);
        writer.println(response.getResponse());
    }
}