package jp.co.topgate.atoze.web;

import java.io.*;

/**
 * Created by atoze on 2017/04/12.
 */
public class ServerHandler {
    private String bodyText;

    public ServerHandler(InputStream in, OutputStream out) throws IOException {
        HTTPRequest request = new HTTPRequest(in);
        //if(request.getHeaderText()!=null){
        String headerText = request.getHeaderText();
        System.out.println(headerText);

        RequestHeaderCheck requestHeader = new RequestHeaderCheck(headerText);

        PrintWriter writer = new PrintWriter(out, true);
        StringBuilder builder = new StringBuilder();//文字列生成
        builder.append("HTTP/1.1 ");


        if ((requestHeader.getMethod().equals("GET"))){

            File file = new File(requestHeader.getFilePath());
            ContentType contentType = new ContentType(requestHeader.getFilePath());
            builder.append("200 OK").append("\n");
            builder.append("Content-Type: "+contentType.getContentType()+"/").append(contentType.getExtension()).append("\n");
            System.out.println("Responding...");
            System.out.println(builder.toString() + "\n");
            writer.println(builder.toString());



            FileLook look = new FileLook();

            if(look.checkFile(file)){
                BufferedInputStream bi
                        = new BufferedInputStream(new FileInputStream(file));
                try {
                    for (int c = bi.read(); c >= 0; c = bi.read()) {
                        out.write(c);
                    }

                }catch (IOException e) {
                    e.printStackTrace();
                }

                finally {
                    if (bi != null) {
                       bi.close();
                    }
                }

            }else{
                System.out.println("ないです");}

        }

    }
    //}


    private boolean isGETRequest(String request){
        //GETリクエスト
        if(request.startsWith("GET")){
            return true;
        }return false;
    }






}

