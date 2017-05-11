package jp.co.topgate.atoze.web;

/**
 * Created by atoze on 2017/04/17.
 */
public class HTTPRequestHeaderTest {
    /*
    @Test
    public void HTTPRequestのRequestLineを分けるクラスのテスト() throws IOException {
        String line = "GET / HTTP/1.1";

        HTTPRequestHeader header = new HTTPRequestHeader(line);

        assertThat("GET", is(header.getMethod()));
        assertThat("/", is(header.getFilePath()));
        assertThat("HTTP/1.1", is(header.getProtocol()));
    }

    @Test
    public void nullテスト() throws IOException {
        String line = null;

        HTTPRequestHeader header = new HTTPRequestHeader(line);

        assertThat("", is(header.getMethod()));
        assertThat(null, is(header.getFilePath()));
        assertThat(null, is(header.getProtocol()));
    }

    @Test
    public void エラーテスト() throws IOException {
        //スペースなし
        String line = "GET/HTTP/1.1";

        HTTPRequestLine header = new HTTPRequestLine(line);

        assertThat("", is(header.httpRequestLine.getMethod()));
        assertThat(null, is(header.httpRequestLine.getFilePath()));
        assertThat(null, is(header.httpRequestLine.getProtocolVer()));

        //二重スペース
        line = "GET  /  HTTP/1.1";

        header = new HTTPRequest();
        assertThat("", is(header.httpRequestLine.getMethod()));
        assertThat(null, is(header.httpRequestLine.getFilePath()));
        assertThat(null, is(header.httpRequestLine.getProtocolVer()));

        //URI指定忘れ
        line = "GET  HTTP/1.1";

        header = new HTTPRequestHeader(line);

        assertThat("GET", is(header.httpRequestLine.getMethod()));
        assertThat("", is(header.httpRequestLine.getFilePath()));
        assertThat("HTTP/1.1", is(header.httpRequestLine.getProtocolVer()));
    }*/

}
