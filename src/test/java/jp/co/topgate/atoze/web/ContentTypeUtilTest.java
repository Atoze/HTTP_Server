package jp.co.topgate.atoze.web;

import org.junit.Test;

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

        //通常ファイル
        String file = "test.html";
        assertThat("text/html", is(ContentTypeUtil.getContentType(file)));

        //拡張子がcontent Mapにない場合
        assertThat("text/plain", is(ContentTypeUtil.getContentType("hoge.hoge")));

        //拡張子がない場合
        assertThat(null, is(ContentTypeUtil.getContentType("hoge")));

        //'.'しか送られてこなかった場合
        assertThat("text/plain", is(ContentTypeUtil.getContentType(".")));

        //二重に'.'がある場合
        assertThat("text/html", is(ContentTypeUtil.getContentType("hoge..html")));

        //終わりに拡張子がない場合
        assertThat("text/plain", is(ContentTypeUtil.getContentType("hoge.")));
    }

    @Test
    public void ファイル拡張子を確認する(){
        //NULL
        assertThat(null, is(ContentTypeUtil.getFileExtension(null)));

        //通常ファイル
        assertThat("html", is(ContentTypeUtil.getFileExtension("hoge.html")));

        //拡張子がない場合
        assertThat(null, is(ContentTypeUtil.getContentType("hoge")));

        //'.'しか送られてこなかった場合
        assertThat("", is(ContentTypeUtil.getFileExtension("."))); //

        //二重に'.'がある場合
        assertThat("html", is(ContentTypeUtil.getFileExtension("hoge..html")));

        //終わりに拡張子がない場合
        assertThat("", is(ContentTypeUtil.getFileExtension("hoge.")));
    }
}
