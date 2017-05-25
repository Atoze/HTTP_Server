package jp.co.topgate.atoze.web.util;

import org.junit.Test;

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

        //通常HTMLファイル
        assertThat("text/html", is(ContentType.getContentType("test.html")));

        //テキストファイル
        assertThat("text/plain", is(ContentType.getContentType("test.txt")));

        //拡張子がcontent Mapにない場合
        assertThat("application/octet-stream", is(ContentType.getContentType("hoge.hoge")));

        //拡張子がない場合
        assertThat(null, is(ContentType.getContentType("hoge")));

        //'.'しか送られてこなかった場合
        assertThat("application/octet-stream", is(ContentType.getContentType(".")));

        //二重に'.'がある場合
        assertThat("text/html", is(ContentType.getContentType("hoge..html")));

        //終わりに拡張子がない場合
        assertThat("application/octet-stream", is(ContentType.getContentType("hoge.")));
    }

    @Test
    public void ファイル拡張子を確認する() {
        //NULL
        assertThat(null, is(ContentType.getFileExtension(null)));

        //通常ファイル
        assertThat("html", is(ContentType.getFileExtension("hoge.html")));

        //拡張子がない場合
        assertThat(null, is(ContentType.getContentType("hoge")));

        //'.'しか送られてこなかった場合
        assertThat("", is(ContentType.getFileExtension("."))); //

        //二重に'.'がある場合
        assertThat("html", is(ContentType.getFileExtension("hoge..html")));

        //終わりに拡張子がない場合
        assertThat("", is(ContentType.getFileExtension("hoge.")));
    }
}
