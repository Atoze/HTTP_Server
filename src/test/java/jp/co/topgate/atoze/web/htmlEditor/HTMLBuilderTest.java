package jp.co.topgate.atoze.web.htmlEditor;

import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by atoze on 2017/05/23.
 */
public class HTMLBuilderTest {
    @Test
    public void HTML作成テスト() throws IOException {
        HTMLBuilder html = new HTMLBuilder();
        assertThat("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n<title>null</title><meta charset=\"UTF-8\">\n</head>\n<body>\n</body>\n</html>", is(html.getHTML()));

        //null代入テスト
        html.addBody(null);
        assertThat("null\n", is(html.getBodyData()));

        //空文字代入テスト
        html.setBody("");
        assertThat("\n", is(html.getBodyData()));

        //囲みタグ生成テスト
        String h1 = html.generateField("h1", "TitleTag");
        html.setBody(h1);
        assertThat("<h1>\nTitleTag</h1>\n", is(html.getBodyData()));
    }
}