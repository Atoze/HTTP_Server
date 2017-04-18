package jp.co.topgate.atoze.web;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * Created by atoze on 2017/04/17.
 */
public class HTTPGeneralHeaderTest {
    @Test
    public void HTTPRequestのジェネラルヘッダを処理するクラスのテスト() {
        HTTPHeader header = new HTTPHeader();

        assertNull(header.getFilePath());
        assertNull(header.getFileQuery());
        assertNull(header.getMethod());
        assertNull(header.getProtocol());

        String line = "GET / HTTP/1.1";
        header.setHTTPHeader(line);

        assertThat("/", is(header.getFilePath()));
        assertThat(null, is(header.getFileQuery()));
        assertThat("GET", is(header.getMethod()));
        assertThat("HTTP/1.1", is(header.getProtocol()));

    }

    @Test
    public void エラーテスト() {
        HTTPHeader header = new HTTPHeader();

        String line = "HogeHoge";
        header.setHTTPHeader(line);

        assertNull(header.getFilePath());
        assertNull(header.getFileQuery());
        assertNull(header.getMethod());
        assertNull(header.getProtocol());

    }

}
