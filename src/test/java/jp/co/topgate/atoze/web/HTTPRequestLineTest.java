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
    public void HTTPRequestのジェネラルヘッダを処理するクラスのテスト() {
        String line = "GET / HTTP/1.1";

        HTTPRequestLine header = new HTTPRequestLine(line);

        assertThat("/", is(header.getFilePath()));
        assertThat("GET", is(header.getMethod()));
        assertThat("HTTP/1.1", is(header.getProtocol()));

    }

    @Test
    public void エラーテスト() {
        String line = null;

        HTTPRequestLine header = new HTTPRequestLine(line);

        assertNull(header.getFilePath());
        assertNull(header.getMethod());
        assertNull(header.getProtocol());

    }

    @Test
    public void 絶対パスのテスト(){

    }

}
