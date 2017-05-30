package jp.co.topgate.atoze.web.app.forum;

import jp.co.topgate.atoze.web.htmlEditor.HTMLBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
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

    String getIndexHTML(List list) throws IOException {
        ForumDataPattern.NAME.setQueryKey(NAME_QUERY);
        ForumDataPattern.TITLE.setQueryKey(TITLE_QUERY);
        ForumDataPattern.TEXT.setQueryKey(TEXT_QUERY);
        ForumDataPattern.PASSWORD.setQueryKey(PASSWORD_QUERY);

        html.setLanguage("ja");
        html.setMetaData("charset", "UTF-8");
        html.setTitle(FORUM_TITLE);
        html.setStylesheet("http://" + host + CSS_FILE_PATH);
        html.addBody("<div align=\"center\">");
        html.addBody(headerForm());
        html.addBody(table(list));
        html.addBody(footerForm());
        html.addBody("</div>");
        //html.setBody(headerForm() + table(list) + footerForm());
        return html.getHTML();
    }

    @NotNull
    private String table(List<String[]> list) throws IOException {
        if (list == null || list.size() == 0) {
            return "<div id=\"content\"><p>No Data</p></div>";
        }
        StringBuffer sb = new StringBuffer();
        //sb.append("<table width=\"50%\" border=\"1\">");
        for (int i = 0; i < list.size(); i++) {
            //sb.append("<tr>");
            //sb.append("<td>");
            sb.append("<div id=\"content\">");
            sb.append("<div id=\"wrapper\">");
            sb.append("<table border=\"1\"><tbody>");
            sb.append("<tr>");
            sb.append("<td rowspan=\"3\" width=\"100px\">");
            sb.append("ID:");//Icon Name
            sb.append(getParameter(list, i, ForumDataPattern.ID.getKey()));//ID
            sb.append("<br>投稿者:");
            sb.append(getParameter(list, i, ForumDataPattern.NAME.getKey()));//Name
            sb.append("</td><td>");
            sb.append("<b>");
            sb.append(getParameter(list, i, ForumDataPattern.TITLE.getKey()));//Title
            sb.append("</b><br>");//Title
            sb.append(getParameter(list, i, ForumDataPattern.DATE.getKey()));//Date
            sb.append("</td>");
            sb.append("</tr><tr><td>");
            sb.append(getParameter(list, i, ForumDataPattern.TEXT.getKey()));//Text
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

    private static String dataForm(List<String[]> list, Integer id) throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append("<table width=\"50%\"><tbody>");
        sb.append("<tr>");
        sb.append("<td rowspan=\"3\">");
        sb.append(ForumData.getParameter(list, id, "name"));//Icon Name
        sb.append("</td><td>");
        sb.append(ForumData.getParameter(list, id, "title"));//Title Date
        sb.append(ForumData.getParameter(list, id, "date"));//Title Date
        sb.append("</td>");
        sb.append("</tr><tr><td>");
        sb.append(ForumData.getParameter(list, id, "text"));//Text
        sb.append("</td>");
        sb.append("</tr><tr><td>");
        sb.append("Edit");
        sb.append("</td></tr>");
        sb.append("</tbody></table>");

        return sb.toString();
    }


    private String getParameter(List<String[]> list, int id, String key) {
        String keyToFind = key.toUpperCase();
        String param = ForumData.getParameter(list, id, keyToFind);
        //String encode = ForumData.getParameter(list, id, ForumDataPattern.ENCODER.getKey());
        return sanitizeHTML(param);
    }

    /**
     * タグを無害化します。
     *
     * @param str
     * @return
     */
    private static String sanitizeHTML(String str) {
        if (str == null) {
            return str;
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

}
