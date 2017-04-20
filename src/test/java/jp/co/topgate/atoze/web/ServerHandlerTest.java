package jp.co.topgate.atoze.web;
import org.junit.Test;

import java.io.*;

/**
 * Created by atoze on 2017/04/20.
 */
public class ServerHandlerTest {

    @Test
    public void ServerHandlerテスト () throws IOException{
        ServerHandler serverHandler = new ServerHandler();
        File file = new File("Document/test.txt");
        InputStream input = new FileInputStream(file);

        File log = new File("Document/request.txt");
        OutputStream output = new FileOutputStream(log);

        serverHandler.handle(input, output, 8080);

    }
}