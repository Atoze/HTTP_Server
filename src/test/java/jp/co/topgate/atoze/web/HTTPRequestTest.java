package jp.co.topgate.atoze.web;

import org.junit.Test;

import java.io.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by atoze on 2017/04/16.
 */
public class HTTPRequestTest {
    @Test
    public void HTTPRequestのデータ保管するクラスのテスト()throws IOException {
        HTTPRequest request = new HTTPRequest();
        File file = new File("Document/test.txt");
        InputStream input = new FileInputStream(file);

        assertNull(request.getHeaderText());
        assertNull(request.getMethod());
        assertNull(request.getFilePath());
        assertNull(request.getProtocolVer());

        //データ挿入
        try {
            request.readRequestText(input, "localhost:8080");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertThat("GET", is(request.getMethod()));
        assertThat("/public/index.html", is(request.getFilePath()));
        assertThat("1.1", is(request.getProtocolVer()));

        assertThat("localhost:8080", is(request.getRequestValue("Host")));


        File test = new File("Document/Request.txt");
        OutputStream output = new FileOutputStream(test);
        PrintWriter writer = new PrintWriter(output, true);
        StringWriter sr = new StringWriter();

        //検証ファイルにデータ挿入
        writer.println("GET http://localhost:8080/hoge.html HTTP/1.1");
        writer.println("Host: localhost:8080");
        writer.println("test: hogehoge");
        writer.println("ManyCollon: hoge: hoge: hoge");
        writer.println("hoge:hoge");

        input = new FileInputStream(test);
        request.readRequestText(input, "localhost:8080");

        assertNotNull(request.getHeaderText());

        assertThat("GET", is(request.getMethod()));
        assertThat("/hoge.html", is(request.getFilePath()));
        assertThat("1.1", is(request.getProtocolVer()));

        assertThat("localhost:8080", is(request.getRequestValue("Host")));
        assertThat("hogehoge", is(request.getRequestValue("test")));
        assertThat("hoge: hoge: hoge", is(request.getRequestValue("ManyCollon")));
        assertThat("hoge", is(request.getRequestValue("hoge")));
        assertThat(null, is(request.getRequestValue("foo")));
    }

    @Test
    public void 絶対パスのテスト() throws IOException {
        File test = new File("Document/Request.txt");
        OutputStream output = new FileOutputStream(test);
        PrintWriter writer = new PrintWriter(output, true);
        StringWriter sr = new StringWriter();

        HTTPRequest request = new HTTPRequest();

        InputStream input = new FileInputStream(test);
        request.readRequestText(input, "localhost:8080");

        //https指定
        writer.println("GET https://localhost:8080/hoge.html HTTP/1.1");

        request.readRequestText(input, "localhost:8080");
        assertThat("/hoge.html", is(request.getFilePath()));

        //間違ったローカルホスト指定
        writer.flush();
        sr.getBuffer().setLength(0);
        writer.println("GET http://hogehoge:8080/hoge.html HTTP/1.1");

        request.readRequestText(input, "localhost:8080");
        assertThat("http://hogehoge:8080/hoge.html", is(request.getFilePath()));


    }

    @Test
    public void 間違ったリクエストがきた場合() throws IOException {
        File test = new File("Document/Request.txt");
        OutputStream output = new FileOutputStream(test);
        PrintWriter writer = new PrintWriter(output, true);
        StringWriter sr = new StringWriter();

        HTTPRequest request = new HTTPRequest();
        InputStream input = new FileInputStream(test);

        //スペースがない場合
        writer.println("GET/HTTP/1.1");
        request.readRequestText(input, "localhost:8080");

        assertNull(request.getHeaderText());
        assertNull(request.getMethod());
        assertNull(request.getFilePath());
        assertNull(request.getProtocolVer());

        //順番がバラバラの場合
        writer.flush();
        sr.getBuffer().setLength(0);
        writer.println("HTTP/1.1 GET https://localhost:8080/hoge.html");
        request.readRequestText(input, "localhost:8080");

        assertNull(request.getHeaderText());
        assertNull(request.getMethod());
        assertNull(request.getFilePath());
        assertNull(request.getProtocolVer());

        //順番がバラバラの場合
        writer.flush();
        sr.getBuffer().setLength(0);
        writer.println("GET: https://localhost:8080/hoge.html HTTP/1.1");

        request.readRequestText(input, "localhost:8080");

        assertNull(request.getHeaderText());
        assertNull(request.getMethod());
        assertNull(request.getFilePath());
        assertNull(request.getProtocolVer());
    }


}