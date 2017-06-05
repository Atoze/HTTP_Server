package jp.co.topgate.atoze.web.app.board;

import jp.co.topgate.atoze.web.util.HTMLBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * 掲示板用のHTMLを作成します.
 * //TODO HTMLEditorを使ってもっと綺麗に生成する
 */
class ForumHTML {
    private static final String FORUM_TITLE = "簡易掲示板のテスト";
    private static final String CSS_FILE_PATH = "/css/board.css";
    private final String host;
    private static final String NAME_QUERY = "name";
    private static final String TITLE_QUERY = "title";
    private static final String TEXT_QUERY = "text";
    private static final String PASSWORD_QUERY = "password";
    private HTMLBuilder html = new HTMLBuilder();


    ForumHTML(String host) {
        this.host = host;
    }

    String getIndexHTML(List<ForumData> list) throws IOException {
        ForumDataKey.NAME.setQueryKey(NAME_QUERY);
        ForumDataKey.TITLE.setQueryKey(TITLE_QUERY);
        ForumDataKey.TEXT.setQueryKey(TEXT_QUERY);
        ForumDataKey.PASSWORD.setQueryKey(PASSWORD_QUERY);

        html.setLanguage("ja");
        html.setMetaData("charset", "UTF-8");
        html.setTitle(FORUM_TITLE);
        html.setStylesheet("http://" + host + CSS_FILE_PATH);
        html.addBody("<div align=\"center\">");
        html.addBody(headerForm());
        html.addBody(table(list));
        html.addBody(footerForm());
        html.addBody("</div>");
        return html.getHTML();
    }

    @NotNull
    private String table(List<ForumData> list) throws IOException {
        if (list == null || list.size() == 0) {
            return "<div id=\"content\"><p>No Data</p></div>";
        }
        StringBuffer sb = new StringBuffer();
        //sb.append("<table width=\"50%\" border=\"1\">");
        for (int i = 0; i < list.size(); i++) {
            sb.append("<div id=\"content\">");
            sb.append("<div id=\"wrapper\">");
            sb.append("<table border=\"1\"><tbody>");
            sb.append("<tr>");
            sb.append("<td rowspan=\"3\" width=\"100px\">");
            sb.append("ID:");//Icon Name
            ForumData data = list.get(i);
            sb.append(data.getId());//ID
            sb.append("<br>投稿者:");
            String encoder = data.getEncoder();
            sb.append(decode(data.getName(), encoder));//Name
            sb.append("</td><td>");
            sb.append("<b>");
            sb.append(decode(data.getTitle(), encoder));//Title
            sb.append("</b><br>");//Title
            sb.append(data.getDate());//Date
            sb.append("</td>");
            sb.append("</tr><tr><td>");
            sb.append(decode(data.getText(), encoder));//Text
            sb.append("</td>");
            sb.append("</tr><tr><td id=\"footer\">");
            sb.append("<form method=\"POST\" action=\"/program/board/\">");
            sb.append("<input type=\"hidden\" name=\"_method\" value=\"DELETE\">");
            sb.append("<input type=\"hidden\" name=\"tableIndex\" value=\"").append(i).append("\">");
            sb.append("パスワード:<input type=\"password\" name=\"password\" size=\"20\">");
            sb.append("<input type=\"submit\" value=\"削除\">");
            sb.append("</form>");
            sb.append("</td></tr>");
            sb.append("</tbody></table>");
            sb.append("</div>");
            sb.append("</div>");
            //sb.append(dataForm(list,i));
            //sb.append("</td>");

            //sb.append("</tr>");
        }
        //sb.append("</table>");
        return sb.toString();
    }

    private String headerForm() {
        StringBuffer sb = new StringBuffer();
        sb.append("<header>");
        sb.append(FORUM_TITLE).append("<br/>");
        sb.append("<form method=\"post\" action=\"/program/board/\"><br/>");
        sb.append("名前:<input required type=\"text\" name=\"");
        sb.append(NAME_QUERY);
        sb.append("\" size=\"50\" maxlength=\"50\" placeholder=\" 名前を入力してください(50文字まで)\"><br/>");
        sb.append("タイトル:<input required type=\"text\" name=\"" + TITLE_QUERY + "\" size=\"50\" maxlength=\"50\" placeholder=\"タイトルを入力してください(50文字まで)\"><br/>");
        sb.append("本文:<textarea required name=\"" + TEXT_QUERY + "\" rows=\"10\" cols=\"50\" maxlength=\"200\" placeholder=\"コメントを入力してください(200文字まで)\"></textarea><br/>");
        sb.append("パスワード:<input required type=\"password\" name=\"" + PASSWORD_QUERY + "\" maxlength=\"30\" ><br/>");
        sb.append("<INPUT type='submit' value='入力'></form>");
        sb.append("</header>");

        return sb.toString();
    }

    private static String footerForm() {
        StringBuffer sb = new StringBuffer();
        sb.append("<footer>");
        sb.append("<hr />");
        sb.append("<form method=\"post\" action=\"/program/board/\"><br/>");
        sb.append("<INPUT type='hidden' name='_method' value='DELETE'>");
        sb.append("メッセージID:<input type=\"text\" name=\"threadID\" size=\"3\">");
        sb.append("パスワード:<input type=\"password\" name=\"password\" size=\"20\">");
        sb.append("<INPUT type='submit' name='button' value='削除'></form>");
        sb.append("<form method=\"get\" action=\"/program/board/search\"><br/>");
        sb.append("検索:<INPUT type='search' name='search'>");
        sb.append("<input type=\"submit\" value=\"送信する\">");
        sb.append("</form>");
        sb.append("</footer>");
        return sb.toString();
    }

    /**
     * タグを無害化します。
     *
     * @param str
     * @return
     */
    private static String sanitizeHTML(String str) {
        if (str == null) {
            return null;
        }
        str = str.replaceAll("&", "&amp;");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        str = str.replaceAll("\"", "&quot;");
        str = str.replaceAll("'", "&#39;");

        str = str.replaceAll("&lt;br&gt;", "<br>");
        if (str.contains("\r\n")) str = str.replaceAll("\r\n", "<br>");
        else str = str.replaceAll("\n", "<br>");
        return str;
    }

    private String decode(String data, String encoder) {
        try {
            data = URLDecoder.decode(data, encoder);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException();
        }
        return sanitizeHTML(data);
    }

}
