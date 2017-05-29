package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.util.Status;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
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

        OutputStream output = new ByteArrayOutputStream();

        status.setStatus(200);
        response.addResponseHeader("ContentType", "text/html");
        response.writeTo(output);
        assertThat("HTTP/1.1 " + status.getStatus() + "\n" + "ContentType: text/html\n", is(response.toString()));

    }
}
