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
        File file = new File("Document/test.txt"); //実データに近いもの
        InputStream input = new FileInputStream(file);

        assertThat(null, is(request.getRequestHeader()));
        assertThat(null, is(request.getMethod()));
        assertThat(null, is(request.getFilePath()));
        assertThat(null, is(request.getProtocolVer()));

        //データ挿入
        try {
            request.readRequestHeader(input, "localhost:8080");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertThat("GET", is(request.getMethod()));
        assertThat("/public/index.html", is(request.getFilePath()));
        assertThat("1.1", is(request.getProtocolVer()));

        assertThat("localhost:8080", is(request.getHeaderParam("HOST")));


        File test = new File("Document/request.txt");
        OutputStream output = new FileOutputStream(test);
        PrintWriter writer = new PrintWriter(output, true);

        //検証ファイルにデータ挿入
        writer.println("GET http://localhost:8080/hoge.html HTTP/1.1");
        writer.println("Host: localhost:8080");
        writer.println("test: hogehoge");
        writer.println("ManyCollon: hoge: hoge: hoge");
        writer.println("hoge:hoge");

        input = new FileInputStream(test);
        request.readRequestHeader(input, "localhost:8080");

        assertThat("GET", is(request.getMethod()));
        assertThat("/hoge.html", is(request.getFilePath()));
        assertThat("1.1", is(request.getProtocolVer()));

        assertThat("localhost:8080", is(request.getHeaderParam("HOST")));
        assertThat("hogehoge", is(request.getHeaderParam("TEST")));
        assertThat("hoge: hoge: hoge", is(request.getHeaderParam("MANYCOLLON")));
        assertThat("hoge", is(request.getHeaderParam("HOGE")));
        assertThat(null, is(request.getHeaderParam("FOO")));
    }

    @Test
    public void 絶対パスのテスト() throws IOException {
        File test = new File("Document/request.txt");
        OutputStream output = new FileOutputStream(test);
        PrintWriter writer = new PrintWriter(output, true);

        HTTPRequest request = new HTTPRequest();

        InputStream input = new FileInputStream(test);
        request.readRequestHeader(input, "localhost:8080");

        //https指定
        writer.println("GET https://localhost:8080/hoge.html HTTP/1.1");

        request.readRequestHeader(input, "localhost:8080");
        assertThat("/hoge.html", is(request.getFilePath()));

        //間違ったローカルホスト指定 そのまま返して来る
        writer.flush();
        writer.println("GET http://hogehoge/hoge.html HTTP/1.1");

        request.readRequestHeader(input, "localhost:8080");
        assertThat("http://hogehoge/hoge.html", is(request.getFilePath()));


    }

    @Test
    public void 間違ったリクエストがきた場合() throws IOException {
        File test = new File("Document/request.txt");
        OutputStream output = new FileOutputStream(test);
        PrintWriter writer = new PrintWriter(output, true);

        HTTPRequest request = new HTTPRequest();
        InputStream input = new FileInputStream(test);

        //スペースがない場合
        writer.println("GET/HTTP/1.1");
        request.readRequestHeader(input, "localhost:8080");

        assertThat(null, is(request.getMethod()));
        assertThat(null, is(request.getFilePath()));
        assertThat(null, is(request.getProtocolVer()));

        //スペースが2つだけの場合
        writer.flush();
        writer.println("GET https://localhost:8080/hoge.htmlHTTP/1.1");
        request.readRequestHeader(input, "localhost:8080");

        assertThat(null, is(request.getMethod()));
        assertThat(null, is(request.getFilePath()));
        assertThat(null, is(request.getProtocolVer()));

        //スペースが４つ以上の場合
        writer.flush();
        writer.println("GET https://localhost:8080/hoge.html HTTP/1.1 hogehoge");
        request.readRequestHeader(input, "localhost:8080");

        assertThat(null, is(request.getMethod()));
        assertThat(null, is(request.getFilePath()));
        assertThat(null, is(request.getProtocolVer()));

        //順番がバラバラの場合
        writer.flush();
        writer.println("HTTP/1.1 GET https://localhost:8080/hoge.html");
        request.readRequestHeader(input, "localhost:8080");

        assertThat(null, is(request.getMethod()));
        assertThat("GET", is(request.getFilePath()));
        assertThat(null, is(request.getProtocolVer()));

        //Methodが間違っている場合
        writer.flush();
        writer.println("Foo: https://localhost:8080/hoge.html HTTP/1.1");

        request.readRequestHeader(input, "localhost:8080");

        assertThat(null, is(request.getMethod()));
        assertThat("/hoge.html", is(request.getFilePath()));
        assertThat("1.1", is(request.getProtocolVer()));

        //URL指定忘れ & HTTP指定が間違っている場合
        writer.flush();
        writer.println("GET  HTTPhoge");

        request.readRequestHeader(input, "localhost:8080");

        assertThat("GET", is(request.getMethod()));
        assertThat("", is(request.getFilePath()));
        assertThat(null, is(request.getProtocolVer()));
    }
}