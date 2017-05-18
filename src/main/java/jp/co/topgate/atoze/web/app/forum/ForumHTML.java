package jp.co.topgate.atoze.web.app.forum;

import jp.co.topgate.atoze.web.HTMLEditor.HTMLEditor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by atoze on 2017/05/15.
 */
class ForumHTML {
    private static final String FORUM_TITLE = "簡易掲示板のテスト";
    private String host;
    private static final String CSS_FILENAME = "/css/board.css";

    ForumHTML(String host) {
        this.host = host;
    }

    String indexHTML(List list) throws IOException {
        HTMLEditor html = new HTMLEditor();
        
        html.setLanguage("ja");
        //html.setTitle(FORUM_TITLE);
        html.setStylesheet(host + CSS_FILENAME);
        html.setBody(form() + table(list) + form2());
        return html.getHTML();
    }

    private String table(List<String[]> list) throws IOException {
        if (list == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        sb.append("<table width=\"50%\" border=\"1\">");
        for (int i = 0; i < list.size(); i++) {
            sb.append("<tr>");
            sb.append("<td>");
            //StringBuffer sb = new StringBuffer();
            sb.append("<table border=\"1\" width=\"100%\"><tbody>");
            sb.append("<tr>");
            sb.append("<td rowspan=\"3\">");
            sb.append(getParameter(list, i, "NAME"));//Icon Name
            sb.append("</td><td>");
            sb.append(getParameter(list, i, "TITLE"));//Title Date
            sb.append(getParameter(list, i, "DATE"));//Title Date
            sb.append("</td>");
            sb.append("</tr><tr><td>");
            sb.append(getParameter(list, i, "TEXT"));//Text
            sb.append("</td>");
            sb.append("</tr><tr>");
            sb.append("<td><form method=\"POST\" action=\"/program/board/\"><br/>");
            sb.append("<input type=\"hidden\" name=\"_method\" value=\"DELETE\">");
            sb.append("<input type=\"hidden\" name=\"threadID\" value=\"").append(i).append("\">");
            sb.append("パスワード:<input type=\"text\" name=\"password\" size=\"20\">");
            sb.append("<input type=\"submit\" name=\"button\" value=\"削除\">");
            sb.append("</form></td>");
            sb.append("</tr>");
            sb.append("</tbody></table>");

            //sb.append(dataForm(list,i));
            sb.append("</td>");

            sb.append("</tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }

    private String form() {
        StringBuffer sb = new StringBuffer();
        sb.append("<div align=\"center\">簡易Java掲示板<br/>");
        sb.append("<form method=\"post\" action=\"/program/board/\"><br/>");
        sb.append("名前:<input required type=\"text\" name=\"name\" size=\"50\"><br/>");
        sb.append("タイトル:<input required type=\"text\" name=\"title\" size=\"50\"><br/>");
        sb.append("本文:<textarea required name=\"text\" rows=\"10\" cols=\"48\"></textarea><br/>");
        sb.append("パスワード:<input required type=\"password\" name=\"password\"><br/>");
        sb.append("<INPUT type='submit' name='button' value='入力'></form>");

        return sb.toString();
    }

    private String form2() {
        StringBuffer sb = new StringBuffer();
        sb.append("<form method=\"post\" action=\"/program/board/\"><br/>");
        sb.append("<INPUT type='hidden' name='_method' value='DELETE'>");
        sb.append("メッセージID:<input type=\"text\" name=\"threadID\" size=\"3\">");
        sb.append("パスワード:<input type=\"text\" name=\"password\" size=\"20\">");
        sb.append("<INPUT type='submit' name='button' value='削除'></form>");
        sb.append("<form method=\"post\" action=\"/program/board/search\"><br/>");
        sb.append("検索:<INPUT type='search' name='search'>");
        sb.append("<input type=\"submit\" value=\"送信する\">");
        sb.append("</form>");
        sb.append("</div>");
        return sb.toString();
    }

    private String dataForm(List<String[]> list, Integer id) throws IOException {
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


    private String getParameter(List list, int id, String key) throws UnsupportedEncodingException {
        String keyToFind = key.toUpperCase();
        String param = ForumData.getParameter(list, id, keyToFind);
        return convertSanitize(URLDecoder.decode(param, "UTF-8"));
    }

    /**
     * タグを無害化します。
     *
     * @param str
     * @return
     */
    private static String convertSanitize(String str) {
        if (str == null) {
            return str;
        }
        str = str.replaceAll("&", "&amp;");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        str = str.replaceAll("\"", "&quot;");
        str = str.replaceAll("'", "&#39;");

        return str;
    }

}
