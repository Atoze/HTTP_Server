package jp.co.topgate.atoze.web;

import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by atoze on 2017/04/18.
 */
public class HTTPResponseTest {
    @Test
    public void HTTPResponseの動きを確認 () throws IOException {
        HTTPResponse response = new HTTPResponse();

        Status.setStatus(200);
        response.addLine("ContentType", "text/html");
        response.writeTo();
        assertThat("HTTP/1.1 "+Status.getStatus()+"\\n"+"ContentType: text/html\\n", is(response.getResponse()));

    }
}
