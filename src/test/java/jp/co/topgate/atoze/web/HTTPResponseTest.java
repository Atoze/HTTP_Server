package jp.co.topgate.atoze.web;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by atoze on 2017/04/18.
 */
public class HTTPResponseTest {
    @Test
    public void HTTPResponseの動きを確認() throws IOException {
        HTTPResponse response = new HTTPResponse();
        Status status = new Status();

        File log = new File("src/test/Document/response.txt");
        OutputStream output = new FileOutputStream(log);

        status.setStatus(200);
        response.addResponseHeader("ContentType", "text/html");
        response.writeTo(output, status);
        assertThat("HTTP/1.1 " + status.getStatus() + "\n" + "ContentType: text/html\n", is(response.getResponse()));

    }
}
