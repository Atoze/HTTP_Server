package jp.co.topgate.atoze.web;

import java.io.*;

/**
 * Created by atoze on 2017/04/12.
 */
class ServerHandler {

    String hostname = "localhost";

    private FileLook look = new FileLook();
    HTTPResponse response = new HTTPResponse();

    public ServerHandler(InputStream in, OutputStream out, Integer PORT) throws IOException {

        HTTPRequest request = new HTTPRequest(in);
        System.out.println(request.getHeaderText());
        PrintWriter writer = new PrintWriter(out, true);
        ContentType contentType = new ContentType();

        HTTPResponse response = new HTTPResponse();
        System.out.println("Responding...");
        //System.out.println(request.getProtocol());

        if(request.getHost()!=null && request.getHost().startsWith("Host: " +this.hostname+ ":" +PORT.toString())){
            if (request.getMethod()!=null && request.getMethod().equals("GET")) {

            File file = new File(request.getFilePath());
            contentType.setContentType(request.getFilePath());

            if (look.ifcheckFile(file)) {
                Status.setStatusCode(200);
                response.addLine("Content-Type", contentType.getContentType());
                //response.addLine("Content-Length", contentType.getContentType());
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
                    response.setError(500);
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
        }else{
            response.setError(400);
        }
        response.writeTo();
        writer.println(response.getResponse());
    }
}