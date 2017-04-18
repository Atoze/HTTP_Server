package jp.co.topgate.atoze.web;

import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
/**
 * Created by atoze on 2017/04/18.
 */
public class ContentTypeTest {
    @Test
    public void ContentTypeを判断する() {
        ContentType contentType = new ContentType();

        assertThat(null + "/" + null, is(contentType.getContentType()));
        assertNull(contentType.getContentTypeValue());
        assertNull(contentType.getExtension());

        //ファイルをいれてみる
        File file = new File("test.html");
        contentType.setContentType(file.toString());
        assertThat("text/html", is(contentType.getContentType()));
        assertThat("text", is(contentType.getContentTypeValue()));
        assertThat("html", is(contentType.getExtension()));

        contentType.setContentTypeDefault();
        assertThat("text/plain", is(contentType.getContentType()));
    }
}
