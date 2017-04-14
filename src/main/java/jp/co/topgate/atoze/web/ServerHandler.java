package jp.co.topgate.atoze.web;

import java.io.*;

/**
 * Created by atoze on 2017/04/12.
 */
public class ServerHandler {
    private String bodyText;


    private Status status = new Status();
    private FileLook look = new FileLook();
    private StringBuilder builder = new StringBuilder();//文字列生成


    public ServerHandler(InputStream in, OutputStream out) throws IOException {
        HTTPRequest request = new HTTPRequest(in);
        String headerText = request.getHeaderText();

        PrintWriter writer = new PrintWriter(out, true);
        builder.append("HTTP/1.1 ");

        if ((request.getMethod().equals("GET"))) {

            File file = new File(request.getFilePath());
            ContentType contentType = new ContentType(request.getFilePath());
            System.out.println("Responding...");

            if (look.checkFile(file)) {
                status.setStatus(200);
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
                status.setStatus(404);
                builder.append(status.getStatus() + "\n");
                builder.append("Content-Type: " + contentType.getContentType() + "/").append(contentType.getExtension()).append("\n");
                System.out.println(builder.toString() + "\n");
                builder.append("<html><head><title>Hello world!</title></head><body><h1>Hello world!</h1>Hi!</body></html>");
                writer.println(builder.toString());


            }

            //}else{
            //builder.append("\n"+ status.getStatusCode(405)+"\n");
            //File file = new File(requestHeader.getFilePath());
            //builder.append("Content-Type: "+contentType.getContentType()+"/").append(contentType.getExtension()).append("\n");
            //System.out.println("Responding...");
            //System.out.println(builder.toString() + "\n");
            //writer.println(builder.toString());
            //}

        } else {
            status.setStatus(404);
            builder.append("\n" + status.getStatus() + "\n");
            //File file = new File(requestHeader.getFilePath());
            builder.append("Content-Type: plain" + "/").append("html").append("\n");
            builder.append("<html><head><title>Hello world!</title></head><body><h1>Hello world!</h1>Hi!</body></html>");
            System.out.println("Responding...");
            System.out.println(builder.toString() + "\n");


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

