package jp.co.topgate.atoze.web;
import org.junit.Test;

import java.io.*;

/**
 * Created by atoze on 2017/04/20.
 */
public class ServerTest {

    @Test
    public void ServerHandlerテスト () throws IOException{
        ResponseHandler responseHandler = new ResponseHandler("localhost",8080);
        File file = new File("Document/test.txt");
        InputStream input = new FileInputStream(file);

        File log = new File("Document/request.txt");
        OutputStream output = new FileOutputStream(log);

        HTTPRequest request = new HTTPRequest();
        request.readRequestText(input, "localhost:8080");

        responseHandler.responseOutput(request,output);

    }
}