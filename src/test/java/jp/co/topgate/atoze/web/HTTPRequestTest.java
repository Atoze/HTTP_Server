package jp.co.topgate.atoze.web;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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
        assertThat(null, is(httpRequest.getMethod()));
        assertThat(null, is(httpRequest.getFilePath()));
        assertThat(null, is(httpRequest.getProtocolVer()));

        //データ挿入
        try {
            httpRequest.readRequest(input, "localhost:8080");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertThat("GET", is(httpRequest.getMethod()));
        assertThat("/public/index.html", is(httpRequest.getFilePath()));
        assertThat("1.1", is(httpRequest.getProtocolVer()));

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

        assertThat("GET", is(httpRequest.getMethod()));
        assertThat("/hoge.html", is(httpRequest.getFilePath()));
        assertThat("1.1", is(httpRequest.getProtocolVer()));

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
        assertThat("http://hogehoge/hoge.html", is(httpRequestLine.getFilePath()));
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

        assertThat(null, is(httpRequestLine.getMethod()));
        assertThat("", is(httpRequestLine.getFilePath()));
        assertThat(null, is(httpRequestLine.getProtocolVer()));

        //スペースが2つだけの場合
        writer.flush();
        writer.println("GET https://localhost:8080/hoge.htmlHTTP/1.1");
        httpRequestLine.readRequest(input, "localhost:8080");

        assertThat(null, is(httpRequestLine.getMethod()));
        assertThat("", is(httpRequestLine.getFilePath()));
        assertThat(null, is(httpRequestLine.getProtocolVer()));

        //スペースが４つ以上の場合
        writer.flush();
        writer.println("GET https://localhost:8080/hoge.html HTTP/1.1 hogehoge");
        httpRequestLine.readRequest(input, "localhost:8080");

        assertThat(null, is(httpRequestLine.getMethod()));
        assertThat("", is(httpRequestLine.getFilePath()));
        assertThat(null, is(httpRequestLine.getProtocolVer()));

        //順番がバラバラの場合
        writer.flush();
        writer.println("HTTP/1.1 GET https://localhost:8080/hoge.html");
        httpRequestLine.readRequest(input, "localhost:8080");

        assertThat(null, is(httpRequestLine.getMethod()));
        assertThat("GET", is(httpRequestLine.getFilePath()));
        assertThat(null, is(httpRequestLine.getProtocolVer()));

        //Methodが間違っている場合
        writer.flush();
        writer.println("Foo: http://localhost:8080/hoge.html HTTP/1.1");

        httpRequestLine.readRequest(input, "localhost:8080");

        assertThat(null, is(httpRequestLine.getMethod()));
        assertThat("/hoge.html", is(httpRequestLine.getFilePath()));
        assertThat("1.1", is(httpRequestLine.getProtocolVer()));

        //URL指定忘れ & HTTP指定が間違っている場合
        writer.flush();
        writer.println("GET  HTTPhoge");

        httpRequestLine.readRequest(input, "localhost:8080");

        assertThat("GET", is(httpRequestLine.getMethod()));
        assertThat("", is(httpRequestLine.getFilePath()));
        assertThat(null, is(httpRequestLine.getProtocolVer()));
    }

    @Test
    public void POSTテスト() throws IOException {
        HTTPRequest httpRequest = new HTTPRequest();
        File file = new File("src/test/Document/test_POST.txt"); //実データに近いもの
        InputStream input = new FileInputStream(file);

        assertThat(null, is(httpRequest.getRequestHeader()));
        assertThat(null, is(httpRequest.getMethod()));
        assertThat(null, is(httpRequest.getFilePath()));
        assertThat(null, is(httpRequest.getProtocolVer()));

        //データ挿入
        try {
            httpRequest.readRequest(input, "localhost:8080");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertThat("POST", is(httpRequest.getMethod()));
        assertThat("/test.html", is(httpRequest.getFilePath()));
        assertThat("1.1", is(httpRequest.getProtocolVer()));

        assertThat("key1=value1&key2=あいうえお", is(httpRequest.getRequestText()));
        System.out.println(httpRequest.getRequestHeader());
        System.out.println(httpRequest.getRequestText());

    }

    @Test
    public void POSTLargeテスト() throws IOException {
        HTTPRequest httpRequest = new HTTPRequest();
        File file = new File("src/test/Document/test_LargePOST.txt"); //実データに近いもの
        InputStream input = new FileInputStream(file);

        assertThat(null, is(httpRequest.getRequestHeader()));
        assertThat(null, is(httpRequest.getMethod()));
        assertThat(null, is(httpRequest.getFilePath()));
        assertThat(null, is(httpRequest.getProtocolVer()));

        //データ挿入
        try {
            httpRequest.readRequest(input, "localhost:8080");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertThat("POST", is(httpRequest.getMethod()));
        assertThat("/test.html", is(httpRequest.getFilePath()));
        assertThat("1.1", is(httpRequest.getProtocolVer()));

        String largePOST = new String(httpRequest.getRequestBodyFile(), "UTF-8");

        System.out.println(httpRequest.getRequestHeader());
        System.out.println(largePOST);

    }

    @Test
    public void 画像データ取得のテスト() throws Exception {
        HTTPRequest httpRequest = new HTTPRequest();
        String filePath = "src/test/Document/test_imagePOST.txt";
        File file = new File(filePath);
        File img = new File("src/test/Document/bird.png");

        OutputStream output = new FileOutputStream(file);
        InputStream input = new FileInputStream(filePath);

        String imgTest = "src/test/Document/test_imagePOST.txt";
        File imgTestFile = new File(imgTest);
        FileOutputStream imgTestOutput = new FileOutputStream(imgTestFile);

        StringBuilder sb = new StringBuilder();
        String lineFeed = System.getProperty("line.separator");

        //Imageバイト付きリクエストヘッダの作成
        sb.append("POST /test.html HTTP/1.1").append(lineFeed);
        sb.append("Host: localhost:8080").append(lineFeed);
        sb.append("Content-Type: image/plain").append(lineFeed);
        sb.append("Content-Length:");

        BufferedImage originalImage = ImageIO.read(img);

        byte[] imgByte = getBytesFromImage(originalImage, "png");
        sb.append(imgByte.length);
        sb.append(lineFeed);
        sb.append(lineFeed);
        byte[] header = sb.toString().getBytes();
        output.write(header);
        output.write(imgByte);
        //imgTestOutput.write(imgByte);

        httpRequest.readRequest(input, "localhost:8080");
        assertThat("POST", is(httpRequest.getMethod()));

        //System.out.println(httpRequest.getRequestHeader());
        //System.out.println(new String(httpRequest.getRequestFile(), "UTF-8"));

        BufferedInputStream bi = new BufferedInputStream(new FileInputStream(imgTestFile));
        byte[] test = httpRequest.getRequestBodyFile();
        //byte[] test = requestFile(bi, imgByte.length);
        BufferedImage outputImage = getImageFromBytes(test);
        //System.out.println(new String(test, "UTF-8"));

        ImageIO.write(outputImage, "png", new File("src/test/Document/bird2.png"));
    }

    public static byte[] getBytesFromImage(BufferedImage img, String format) throws IOException {
        if (format == null) {
            format = "png";
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, format, baos);
        return baos.toByteArray();
    }

    private byte[] readFileToByte(String filePath) throws Exception {
        byte[] b = new byte[1];
        FileInputStream fis = new FileInputStream(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (fis.read(b) > 0) {
            baos.write(b);
        }
        baos.close();
        fis.close();
        b = baos.toByteArray();

        return b;
    }

    public static BufferedImage getImageFromBytes(byte[] bytes) throws IOException {
        ByteArrayInputStream baos = new ByteArrayInputStream(bytes);
        BufferedImage img = ImageIO.read(baos);
        return img;
    }
}