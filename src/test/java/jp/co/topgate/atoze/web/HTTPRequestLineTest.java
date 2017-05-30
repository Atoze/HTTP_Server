package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.exception.StatusBadRequestException;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by atoze on 2017/04/17.
 */
public class HTTPRequestLineTest {
    @Test
    public void HTTPRequestのRequestLineを分けるクラスのテスト() throws IOException, StatusBadRequestException {
        String line = "GET / HTTP/1.1";
        HTTPRequestLine header = new HTTPRequestLine(line, "localhost:8080");
        //header.readRequestLine(line, "localhost:8080");

        assertThat("GET", is(header.getMethod()));
        assertThat("/", is(header.getPath()));
        assertThat("1.1", is(header.getProtocolVer()));

        line = "GET http://localhost:8080/ HTTP/1.1";
        header = new HTTPRequestLine(line, "localhost:8080");
        //header.readRequestLine(line, "localhost:8080");

        assertThat("GET", is(header.getMethod()));
        assertThat("/", is(header.getPath()));
        assertThat("1.1", is(header.getProtocolVer()));

        //URI指定忘れ
        line = "GET  HTTP/1.1";
        header = new HTTPRequestLine(line, "localhost:8080");
        //header.readRequestLine(line, "localhost:8080");
        assertThat("GET", is(header.getMethod()));
        assertThat("", is(header.getPath()));
        assertThat("1.1", is(header.getProtocolVer()));
    }

    @Test
    public void nullテスト() throws IOException, StatusBadRequestException {
        String line = null;
        HTTPRequestLine header = new HTTPRequestLine(line, "localhost:8080");
        assertThat(null, is(header.getMethod()));
        assertThat(null, is(header.getPath()));
        assertThat(null, is(header.getProtocolVer()));
    }

    @Test(expected = StatusBadRequestException.class)
    public void エラーテスト() throws IOException, StatusBadRequestException {
        //スペースなし
        String line = "GET/HTTP/1.1";

        HTTPRequestLine header = new HTTPRequestLine(line, "localhost:8080");
        //header.readRequestLine(line, "localhost:8080");
        assertThat(null, is(header.getMethod()));
        assertThat("", is(header.getPath()));
        assertThat(null, is(header.getProtocolVer()));

        //二重スペース
        line = "GET  /  HTTP/1.1";
        header = new HTTPRequestLine(line, "localhost:8080");
        //header.readRequestLine(line, "localhost:8080");
        assertThat(null, is(header.getMethod()));
        assertThat("", is(header.getPath()));
        assertThat(null, is(header.getProtocolVer()));

        //スペースがたりない
        line = "GET HTTP/1.1";
        header = new HTTPRequestLine(line, "localhost:8080");
        //header.readRequestLine(line, "localhost:8080");
        assertThat(null, is(header.getMethod()));
        assertThat("", is(header.getPath()));
        assertThat(null, is(header.getProtocolVer()));

        //適当な文字列
        line = "hoge";
        header = new HTTPRequestLine(line, "localhost:8080");
        //header.readRequestLine(line, "localhost:8080");
        assertThat(null, is(header.getMethod()));
        assertThat("", is(header.getPath()));
        assertThat(null, is(header.getProtocolVer()));

        //適当な文字列
        line = "hoge hoge hoge";
        header = new HTTPRequestLine(line, "localhost:8080");
        //header.readRequestLine(line, "localhost:8080");
        assertThat(null, is(header.getMethod()));
        assertThat("hoge", is(header.getPath()));
        assertThat(null, is(header.getProtocolVer()));

        //空スペース
        line = "   ";
        header = new HTTPRequestLine(line, "localhost:8080");
        //header.readRequestLine(line, "localhost:8080");
        assertThat(null, is(header.getMethod()));
        assertThat("", is(header.getPath()));
        assertThat(null, is(header.getProtocolVer()));

        //空スペース
        line = "hoge hoge  ";
        header = new HTTPRequestLine(line, "localhost:8080");
        //header.readRequestLine(line, "localhost:8080");
        assertThat(null, is(header.getMethod()));
        assertThat("", is(header.getPath()));
        assertThat(null, is(header.getProtocolVer()));
    }
}
