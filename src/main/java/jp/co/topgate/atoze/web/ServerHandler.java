package jp.co.topgate.atoze.web;

import java.io.*;

/**
 * Created by atoze on 2017/04/12.
 */
class ServerHandler {


    private FileLook look = new FileLook();
    HTTPResponseGenerate response = new HTTPResponseGenerate();

    public ServerHandler(InputStream in, OutputStream out) throws IOException {

        while(in == null){}
        String bodyText;
        HTTPRequest request = new HTTPRequest(in);
        PrintWriter writer = new PrintWriter(out, true);
        ContentType contentType = new ContentType();

        HTTPResponseGenerate response = new HTTPResponseGenerate();
        System.out.println("Responding...");

        String protocol = request.getProtocol();

        if (protocol!=null) {
            if (protocol!=null && protocol.equals("1.1")) {
                //requireHost;
                //response.setBody("HTTP/1.1 ");

                if (request.getMethod() != null) {
                    if ((request.getMethod().equals("GET"))) {
                        File file = new File(request.getFilePath());

                        //System.out.println(request.getProtocol());

                        contentType.setContentType(request.getFilePath());

                        if (look.ifcheckFile(file)) {
                            Status.setStatusCode(200);
                            response.addLine("Content-Type" , contentType.getContentType());
                            response.addLine("Content-Type" , contentType.getContentType());
                            response.setResponseBody(file);
                            response.writeTo(out);

                            writer.println(response.getResponse());

                            System.out.println(response.getResponse() + "\n");

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
                            //Status.setStatusCode(404);
                            response.setError(404);
                            System.out.println(response.toString());
                            response.writeTo(out);
                            writer.println(response.getResponse());

                        }
                    } else {
                        //Status.setStatusCode(405);
                        response.setError(405);
                        System.out.println(response.toString());
                        response.writeTo(out);
                        writer.println(response.getResponse());

                    }
                } else {
                    //Status.setStatusCode(400);
                    response.setError(400);
                    System.out.println(response.toString());
                    response.writeTo(out);
                    writer.println(response.getResponse());
                }
            }else{
                //Status.setStatusCode(505);
                response.setError(505);
                System.out.println(response.toString());
                response.writeTo(out);
                writer.println(response.getResponse());
            }

        }else{
            Status.setStatusCode(400);
            response.setError(400);
            System.out.println(response.toString());
            response.writeTo(out);
            writer.println(response.getResponse());
            //writer.println(builder.toString());
        }
    }
/*
    public void setError(Integer error) {
        Status.setStatusCode(error);
        File file = new File(error.toString() + ".html");
        if (file.exists() && file.isFile() && file.canRead()) {
                response.setResponseBody(file);
        } else {
            response.setResponseBody("<html><head><title>" + Status.getStatus() + "</title></head><body><h1>" +
                    Status.getStatus() + "</h1></body></html>");
        }
    }
    */
}

