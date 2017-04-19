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
    public void HTTPRequestのデータ保管するクラス () throws IOException{
        HTTPRequest request = new HTTPRequest();
        File file = new File("Document/test.txt");
        InputStream input = new FileInputStream(file);

        assertNull(request.getHeaderText());
        assertNull(request.getMethod());
        //assertNull(request.getFilePath()); //NULLエラー
        assertNull(request.getProtocolVer());

        //データ挿入
        request.setRequestText(input);

        System.out.println(request.getHeaderText());

        assertNotNull(request.getMethod());
        assertNotNull(request.getFilePath());
        assertNotNull(request.getProtocolVer());

        assertThat("GET", is(request.getMethod()));
        assertThat("/public/index.html",is(request.getFilePath()));
        assertThat("1.1",is(request.getProtocolVer()));

        assertThat("localhost:8080",is(request.getRequestValue("Host")));
    }
}