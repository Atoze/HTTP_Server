package jp.co.topgate.atoze.web;

import org.junit.Test;

import java.io.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by atoze on 2017/04/16.
 */
public class HTTPRequestTest {

    @Test
    public void HTTPRequestのデータ保管するクラスのテスト() throws IOException {
        HTTPRequest httpRequest = new HTTPRequest();
        File file = new File("src/test/Document/test.txt"); //実データに近いもの
        InputStream input = new FileInputStream(file);

        assertThat(null, is(httpRequest.getRequestHeader()));
        assertThat(null, is(httpRequest.httpRequestLine.getMethod()));
        assertThat(null, is(httpRequest.httpRequestLine.getFilePath()));
        assertThat(null, is(httpRequest.httpRequestLine.getProtocolVer()));

        //データ挿入
        try {
            httpRequest.readRequest(input, "localhost:8080");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertThat("GET", is(httpRequest.httpRequestLine.getMethod()));
        assertThat("/public/index.html", is(httpRequest.httpRequestLine.getFilePath()));
        assertThat("1.1", is(httpRequest.httpRequestLine.getProtocolVer()));

        assertThat("localhost:8080", is(httpRequest.getHeaderParam("HOST")));


        File test = new File("src/test/Document/httpRequestLine.txt");
        OutputStream output = new FileOutputStream(test);
        PrintWriter writer = new PrintWriter(output, true);

        //検証ファイルにデータ挿入
        writer.println("GET http://localhost:8080/hoge.html HTTP/1.1");
        writer.println("Host: localhost:8080");
        writer.println("test: hogehoge");
        writer.println("ManyCollon: hoge: hoge: hoge");
        writer.println("hoge:hoge");

        input = new FileInputStream(test);
        httpRequest.readRequest(input, "localhost:8080");

        assertThat("GET", is(httpRequest.httpRequestLine.getMethod()));
        assertThat("/hoge.html", is(httpRequest.httpRequestLine.getFilePath()));
        assertThat("1.1", is(httpRequest.httpRequestLine.getProtocolVer()));

        assertThat("localhost:8080", is(httpRequest.getHeaderParam("HOST")));
        assertThat("hogehoge", is(httpRequest.getHeaderParam("TEST")));
        assertThat("hoge: hoge: hoge", is(httpRequest.getHeaderParam("MANYCOLLON")));
        assertThat("hoge", is(httpRequest.getHeaderParam("HOGE")));
        assertThat(null, is(httpRequest.getHeaderParam("FOO")));
    }

    @Test
    public void 絶対パスのテスト() throws IOException {
        File test = new File("src/test/Document/httpRequestLine.txt");
        OutputStream output = new FileOutputStream(test);
        PrintWriter writer = new PrintWriter(output, true);

        HTTPRequest httpRequestLine = new HTTPRequest();

        InputStream input = new FileInputStream(test);
        httpRequestLine.readRequest(input, "localhost:8080");

        //間違ったローカルホスト指定 そのまま返して来る
        writer.flush();
        writer.println("GET http://hogehoge/hoge.html HTTP/1.1");

        httpRequestLine.readRequest(input, "localhost:8080");
        assertThat("http://hogehoge/hoge.html", is(httpRequestLine.httpRequestLine.getFilePath()));
    }

    @Test
    public void 間違ったリクエストがきた場合() throws IOException {
        File test = new File("src/test/Document/httpRequestLine.txt");
        OutputStream output = new FileOutputStream(test);
        PrintWriter writer = new PrintWriter(output, true);

        HTTPRequest httpRequestLine = new HTTPRequest();
        InputStream input = new FileInputStream(test);

        //スペースがない場合
        writer.println("GET/HTTP/1.1");
        httpRequestLine.readRequest(input, "localhost:8080");

        assertThat("", is(httpRequestLine.httpRequestLine.getMethod()));
        assertThat("", is(httpRequestLine.httpRequestLine.getFilePath()));
        assertThat(null, is(httpRequestLine.httpRequestLine.getProtocolVer()));

        //スペースが2つだけの場合
        writer.flush();
        writer.println("GET https://localhost:8080/hoge.htmlHTTP/1.1");
        httpRequestLine.readRequest(input, "localhost:8080");

        assertThat("", is(httpRequestLine.httpRequestLine.getMethod()));
        assertThat("", is(httpRequestLine.httpRequestLine.getFilePath()));
        assertThat(null, is(httpRequestLine.httpRequestLine.getProtocolVer()));

        //スペースが４つ以上の場合
        writer.flush();
        writer.println("GET https://localhost:8080/hoge.html HTTP/1.1 hogehoge");
        httpRequestLine.readRequest(input, "localhost:8080");

        assertThat("", is(httpRequestLine.httpRequestLine.getMethod()));
        assertThat("", is(httpRequestLine.httpRequestLine.getFilePath()));
        assertThat(null, is(httpRequestLine.httpRequestLine.getProtocolVer()));

        //順番がバラバラの場合
        writer.flush();
        writer.println("HTTP/1.1 GET https://localhost:8080/hoge.html");
        httpRequestLine.readRequest(input, "localhost:8080");

        assertThat("", is(httpRequestLine.httpRequestLine.getMethod()));
        assertThat("GET", is(httpRequestLine.httpRequestLine.getFilePath()));
        assertThat(null, is(httpRequestLine.httpRequestLine.getProtocolVer()));

        //Methodが間違っている場合
        writer.flush();
        writer.println("Foo: http://localhost:8080/hoge.html HTTP/1.1");

        httpRequestLine.readRequest(input, "localhost:8080");

        assertThat("", is(httpRequestLine.httpRequestLine.getMethod()));
        assertThat("/hoge.html", is(httpRequestLine.httpRequestLine.getFilePath()));
        assertThat("1.1", is(httpRequestLine.httpRequestLine.getProtocolVer()));

        //URL指定忘れ & HTTP指定が間違っている場合
        writer.flush();
        writer.println("GET  HTTPhoge");

        //httpRequestLine.readRequest(input, "localhost:8080");

        assertThat("GET", is(httpRequestLine.httpRequestLine.getMethod()));
        assertThat("", is(httpRequestLine.httpRequestLine.getFilePath()));
        assertThat(null, is(httpRequestLine.httpRequestLine.getProtocolVer()));
    }

    @Test
    public void POSTテスト() throws IOException {
        HTTPRequest httpRequestLine = new HTTPRequest();
        File file = new File("src/test/Document/test_POST.txt"); //実データに近いもの
        InputStream input = new FileInputStream(file);

        assertThat(null, is(httpRequestLine.getRequestHeader()));
        //assertThat(null, is(httpRequestLine.getMethod()));
        //assertThat(null, is(httpRequestLine.getFilePath()));
        //assertThat(null, is(httpRequestLine.getProtocolVer()));

        //データ挿入
        //try {
            //httpRequestLine.readRequest(input, "localhost:8080");
        //} catch (IOException e) {
        //    throw new RuntimeException(e);
        //}
        //assertThat("POST", is(httpRequestLine.getMethod()));
        //assertThat("/test.html", is(httpRequestLine.getFilePath()));
        //assertThat("1.1", is(httpRequestLine.getProtocolVer()));

        assertThat("key1=value1&key2=あいうえお", is(httpRequestLine.getMessageBody()));
        System.out.println(httpRequestLine.getRequestHeader());
        System.out.println(httpRequestLine.getMessageBody());

    }

    @Test
    public void POSTLargeテスト() throws IOException {
        HTTPRequest httpRequestLine = new HTTPRequest();
        File file = new File("src/test/Document/test_LargePOST.txt"); //実データに近いもの
        InputStream input = new FileInputStream(file);

        assertThat(null, is(httpRequestLine.getRequestHeader()));
        assertThat(null, is(httpRequestLine.httpRequestLine.getMethod()));
        assertThat(null, is(httpRequestLine.httpRequestLine.getFilePath()));
        assertThat(null, is(httpRequestLine.httpRequestLine.getProtocolVer()));

        //データ挿入
        try {
            httpRequestLine.readRequest(input, "localhost:8080");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertThat("POST", is(httpRequestLine.httpRequestLine.getMethod()));
        assertThat("/test.html", is(httpRequestLine.httpRequestLine.getFilePath()));
        assertThat("1.1", is(httpRequestLine.httpRequestLine.getProtocolVer()));

        String largePOST = new String(httpRequestLine.getMessageFile(), "UTF-8");

        System.out.println(httpRequestLine.getRequestHeader());
        System.out.println(largePOST);

    }
}