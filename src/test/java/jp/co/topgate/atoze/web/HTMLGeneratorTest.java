package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.HTMLEditor.HTML5Editor;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by atoze on 2017/05/15.
 */
public class HTMLGeneratorTest {

    @Test
    public void HTMLファイル作成() {
        HTML5Editor html = new HTML5Editor();
        assertThat("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n<meta charset=\"UTF-8\">\n<title></title>\n</head>\n<body>\n</body>\n</html>", is(html.getHTML()));
        html.setTitle("foo");
        html.setLanguage("ja");
        html.setMeta("charset","UTF-8");
        html.setKeywords("hoge,hogehoge");
        html.setHeader("haaaaaaaaa");
        System.out.println(html.getHTML());

        html = new HTML5Editor("foo","hoge");
        //html.setDoctype("html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"");
        System.out.println(html.getHTML());
    }
}
