package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.exception.BadRequestException;
import jp.co.topgate.atoze.web.exception.RequestBodyParseException;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by atoze on 2017/04/16.
 */
public class HTTPRequestTest {
    @Test
    public void Requestのデータをパース() throws IOException, BadRequestException {
        File file = new File("src/test/Document/request.txt"); //実データに近いもの
        HTTPRequest httpRequest = new HTTPRequest(null, null, null, null);
        InputStream input = new FileInputStream(file);

        assertThat(null, is(httpRequest.getHeader()));
        assertThat(null, is(httpRequest.getMethod()));
        assertThat(null, is(httpRequest.getPath()));
        assertThat(null, is(httpRequest.getProtocolVer()));

        //データ挿入
        try {
            httpRequest = HTTPRequestParser.parse(input, "localhost:8080");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertThat("GET", is(httpRequest.getMethod()));
        assertThat("/public/index.html", is(httpRequest.getPath()));
        assertThat("1.1", is(httpRequest.getProtocolVer()));

        assertThat("localhost:8080", is(httpRequest.getHeaderParam("Host")));


        File test = new File("src/test/Document/requestHoge.txt");
        input = new FileInputStream(test);
        httpRequest = HTTPRequestParser.parse(input, "localhost:8080");

        assertThat("GET", is(httpRequest.getMethod()));
        assertThat("/hoge.html", is(httpRequest.getPath()));
        assertThat("1.1", is(httpRequest.getProtocolVer()));

        assertThat("localhost:8080", is(httpRequest.getHeaderParam("Host")));
        assertThat("hogehoge", is(httpRequest.getHeaderParam("Test")));
        assertThat("hoge: hoge: hoge", is(httpRequest.getHeaderParam("ManyCollon")));
        assertThat("hoge", is(httpRequest.getHeaderParam("Hoge")));
        assertThat(null, is(httpRequest.getHeaderParam("Foo")));
    }

    @Test
    public void 絶対パスのテスト() throws IOException, BadRequestException {
        File test = new File("src/test/Document/requestAbsolutePath");
        InputStream input = new FileInputStream(test);

        HTTPRequest httpRequest = HTTPRequestParser.parse(input, "localhost:8080");
        assertThat("/hoge.html", is(httpRequest.getPath()));
    }

    @Test
    public void サーバーの絶対パスと異なる時のテスト() throws IOException, BadRequestException {
        File test = new File("src/test/Document/requestWrongAbsolutePath");
        InputStream input = new FileInputStream(test);

        HTTPRequest httpRequest = HTTPRequestParser.parse(input, "localhost:8080");
        assertThat("http://hogehoge/hoge.html", is(httpRequest.getPath()));
    }

    @Test
    public void POSTテスト() throws IOException, BadRequestException, RequestBodyParseException {
        File file = new File("src/test/Document/requestPost.txt"); //実データに近いもの
        InputStream input = new FileInputStream(file);
        HTTPRequest httpRequest;
        //データ挿入
        try {
            httpRequest = HTTPRequestParser.parse(input, "localhost:8080");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertThat("POST", is(httpRequest.getMethod()));
        assertThat("/test.html", is(httpRequest.getPath()));
        assertThat("1.1", is(httpRequest.getProtocolVer()));

        assertThat("key1=value1&key2=あいうえお", is(httpRequest.getBodyText()));
        System.out.println(httpRequest.getHeader());
        System.out.println(httpRequest.getBodyText());
    }

    @Test
    public void POSTLargeテスト() throws IOException, BadRequestException, RequestBodyParseException {
        File file = new File("src/test/Document/requestLargePOST.txt"); //実データに近いもの
        InputStream input = new FileInputStream(file);

        HTTPRequest httpRequest;
        //データ挿入
        try {
            httpRequest = HTTPRequestParser.parse(input, "localhost:8080");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertThat("POST", is(httpRequest.getMethod()));
        assertThat("/test.html", is(httpRequest.getPath()));
        assertThat("1.1", is(httpRequest.getProtocolVer()));

        String largePOST = httpRequest.getBodyText();

        System.out.println(httpRequest.getHeader());
        System.out.println(largePOST);
    }

    @Test
    public void POSTの中身がない場合のテスト() throws IOException, BadRequestException, RequestBodyParseException {
        File file = new File("src/test/Document/requestPostNoData"); //実データに近いもの
        InputStream input = new FileInputStream(file);

        HTTPRequest httpRequest;
        //データ挿入
        try {
            httpRequest = HTTPRequestParser.parse(input, "localhost:8080");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertThat("POST", is(httpRequest.getMethod()));
        assertThat("/", is(httpRequest.getPath()));
        assertThat("1.1", is(httpRequest.getProtocolVer()));

        assertThat(null, is(httpRequest.getBodyText()));
        assertThat(null, is(httpRequest.getBodyFile()));
        assertThat(new HashMap<String, String>(), is(httpRequest.getFormQueryParam()));
    }

    //直接元画像(src/test/Document/bird.png)と出力画像(src/test/Document/bird2.png)を比べてください
    @Test
    public void 画像データ取得のテスト() throws Exception {
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
        sb.append("Content-Type: multipart/form-data").append(lineFeed);
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

        HTTPRequest httpRequest = HTTPRequestParser.parse(input, "localhost:8080");
        assertThat("POST", is(httpRequest.getMethod()));

        BufferedInputStream bi = new BufferedInputStream(new FileInputStream(imgTestFile));
        byte[] test = httpRequest.getBodyFile();
        //byte[] test = requestFile(bi, imgByte.length);
        BufferedImage outputImage = getImageFromBytes(test);
        //System.out.println(new String(test, "UTF-8"));
        ImageIO.write(outputImage, "png", new File("src/test/Document/bird2.png"));
    }

    private static byte[] getBytesFromImage(BufferedImage img, String format) throws IOException {
        if (format == null) {
            format = "png";
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, format, baos);
        return baos.toByteArray();
    }

    private static BufferedImage getImageFromBytes(byte[] bytes) throws IOException {
        ByteArrayInputStream baos = new ByteArrayInputStream(bytes);
        BufferedImage img = ImageIO.read(baos);
        return img;
    }

}