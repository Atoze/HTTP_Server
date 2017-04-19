package jp.co.topgate.atoze.web;

import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
/**
 * Created by atoze on 2017/04/18.
 */
public class ContentTypeTest {
    @Test
    public void ContentTypeを判断する() {
        //NULL
        assertThat(null, is(ContentType.getContentType(null)));

        //ファイルをいれてみる
        File file = new File("test.html");
        assertThat("text/html", is(ContentType.getContentType(file.toString())));

        //拡張子がMIME-Typeにない場合
        assertThat("text/plain", is(ContentType.getContentType("hoge.hoge")));

        //拡張子がない場合=ファイルではない
        assertThat(null, is(ContentType.getContentType("hoge")));

        //二重に'.'がある場合
        assertThat("text/html", is(ContentType.getContentType("hoge.hoge.html")));

    }
}
