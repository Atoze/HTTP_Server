package jp.co.topgate.atoze.web;

import org.junit.Test;

import java.io.IOException;

/**
 * Created by atoze on 2017/04/18.
 */
public class HTTPResponseTest {
    @Test
    public void HTTPResponseの動きを確認 () throws IOException {
        HTTPResponse response = new HTTPResponse();
        Status status = new Status();

        status.setStatus(200);
        response.addLine("ContentType", "text/html");
        response.writeTo(200);
        //assertThat("HTTP/1.1 "+Status.getStatus()+"\\n"+"ContentType: text/html\\n", equalTo(response.getResponse().toString()));

    }
}
