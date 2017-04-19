package jp.co.topgate.atoze.web;

import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
/**
 * Created by atoze on 2017/04/18.
 */
public class ContentTypeUtilTest {
    @Test
    public void ContentTypeを判断する() {
        //NULL
        assertThat(null, is(ContentTypeUtil.getContentType(null)));

        //ファイルをいれてみる
        File file = new File("test.html");
        assertThat("text/html", is(ContentTypeUtil.getContentType(file.toString())));

        //拡張子がMIME-Typeにない場合
        assertThat("text/plain", is(ContentTypeUtil.getContentType("hoge.hoge")));

        //拡張子がない場合=ファイルではない
        assertThat(null, is(ContentTypeUtil.getContentType("hoge")));

        //二重に'.'がある場合
        assertThat("text/html", is(ContentTypeUtil.getContentType("hoge.hoge.html")));

    }
}
