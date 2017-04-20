package jp.co.topgate.atoze.web;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * Created by atoze on 2017/04/17.
 */
public class HTTPRequestLineTest {
    @Test
    public void HTTPRequestのRequestLineを分けるクラスのテスト() {
        String line = "GET / HTTP/1.1";

        HTTPRequestLine header = new HTTPRequestLine(line);

        assertThat("GET", is(header.getMethod()));
        assertThat("/", is(header.getFilePath()));
        assertThat("HTTP/1.1", is(header.getProtocol()));
    }

    @Test
    public void nullテスト() {
        String line = null;

        HTTPRequestLine header = new HTTPRequestLine(line);

        assertNull(header.getMethod());
        assertNull(header.getFilePath());
        assertNull(header.getProtocol());
    }

    @Test
    public void エラーテスト() {
        //スペースなし
        String line = "GET/HTTP/1.1";

        HTTPRequestLine header = new HTTPRequestLine(line);

        assertNull(header.getMethod());
        assertNull(header.getFilePath());
        assertNull(header.getProtocol());

        //二重スペース
        line = "GET  /  HTTP/1.1";

        header = new HTTPRequestLine(line);
        assertThat("GET", is(header.getMethod()));
        assertThat("", is(header.getFilePath()));
        assertThat(null, is(header.getProtocol()));

        //URI指定忘れ
        line = "GET  HTTP/1.1";

        header = new HTTPRequestLine(line);

        assertThat("GET", is(header.getMethod()));
        assertThat("", is(header.getFilePath()));
        assertThat("HTTP/1.1", is(header.getProtocol()));
    }

}
