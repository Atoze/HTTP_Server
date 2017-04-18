package jp.co.topgate.atoze.web;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

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
        assertNotNull(response.getResponse());//要検討？ストリングだからイコールで繋げない？
    }
}
