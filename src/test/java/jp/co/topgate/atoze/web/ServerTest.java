package jp.co.topgate.atoze.web;
import org.junit.Test;

import java.io.*;

/**
 * Created by atoze on 2017/04/20.
 */
public class ServerTest {

    @Test
    public void Serverテスト () throws IOException{
        File file = new File("Document/test.txt");
        InputStream input = new FileInputStream(file);

        File log = new File("Document/request.txt");
        OutputStream output = new FileOutputStream(log);

        HTTPRequest request = new HTTPRequest();
        request.readRequest(input, "localhost:8080");
    }
}