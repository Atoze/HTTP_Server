package jp.co.topgate.atoze.web;

import org.junit.Test;

import java.io.*;

/**
 * Created by atoze on 2017/04/20.
 */
public class ServerTest {

    @Test
    public void Serverテスト() throws IOException {
        File file = new File("src/test/Document/test.txt");
        InputStream input = new FileInputStream(file);

        File log = new File("src/test/Document/httpRequestLine.txt");
        OutputStream output = new FileOutputStream(log);
    }
}