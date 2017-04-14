package jp.co.topgate.atoze.web;

import java.io.*;

/**
 * Created by atoze on 2017/04/12.
 */
public class ServerHandler {
    private String bodyText;

    private Status status;
    //private Status statusu;
    private FileLook look = new FileLook();
    private StringBuilder builder = new StringBuilder();//文字列生成


    public ServerHandler(InputStream in, OutputStream out) throws IOException {
        HTTPRequest request = new HTTPRequest(in);
        String headerText = request.getHeaderText();

        PrintWriter writer = new PrintWriter(out, true);
        builder.append("HTTP/1.1 ");
        if (request.getMethod() != null) {
            if ((request.getMethod().equals("GET"))) {
                File file = new File(request.getFilePath());
                ContentType contentType = new ContentType(request.getFilePath());
                System.out.println("Responding...");

                if (look.checkFile(file)) {
                    status.setStatusCode(200);
                    builder.append(status.getStatus() + "\n");
                    builder.append("Content-Type: " + contentType.getContentType() + "/").append(contentType.getExtension()).append("\n");
                    writer.println(builder.toString());
                    System.out.println(builder.toString() + "\n");

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
                    status.setStatusCode(404);
                    builder.append(status.getStatus() + "\n");
                    builder.append("Content-Type: " + "text" + "/").append("html").append("\n");
                    builder.append("<html><head><title>Hello world!</title></head><body><h1>Hello world!</h1>Hi!</body></html>");
                    System.out.println(builder.toString() + "\n");



                }
            } else {
                status.setStatusCode(405);
                builder.append(status.getStatus() + "\n");
                builder.append("Content-Type: plain" + "/").append("html").append("\n");

                System.out.println("Responding...");
                System.out.println(builder.toString() + "\n");
                writer.println(builder.toString());

            }
        }else{
            status.setStatusCode(400);
            builder.append(status.getStatus() + "\n");
            builder.append("Content-Type: plain" + "/").append("text").append("\n");

            System.out.println("Responding...");
            System.out.println(builder.toString() + "\n");
            writer.println(builder.toString());
        }


    }
    //}


    private boolean isGETRequest(String request) {
        //GETリクエスト
        if (request.startsWith("GET")) {
            return true;
        }
        return false;
    }


    private void error404() {

    }


}

