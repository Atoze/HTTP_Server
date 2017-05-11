package jp.co.topgate.atoze.web;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by atoze on 2017/05/02.
 */
public class ForumAppHandler extends HTTPHandler {
    private static final String FORUM_TITLE = "簡易掲示板のテスト";
    private static final String CSV_FILEPATH = "./src/main/resources/program/board/";
    private static final String CSV_FILENAME = "save.csv";

    private List<String> mainData = new ArrayList<>();

    ForumData data;

    ForumAppHandler() throws IOException {
        data = new ForumData();
        mainData = data.getData();
    }

    void handle() throws IOException {
        if (request.httpRequestLine.getMethod().equals("POST")) {
            if (request.httpRequestLine.getFilePath().endsWith("search")) {
                findThread(URLDecoder.decode(request.getParameter("search"), "UTF-8"));
            } else if (request.getParameter("_method").equals("DELETE")) {
                if (data.isNumber(request.getParameter("threadID"))) {
                    int id = Integer.parseInt(request.getParameter("threadID"));
                    if (id <= mainData.size() - 1) {
                        deleteThread(id);
                        return;
                    }
                }
                System.out.println("範囲外です");
            } else {
                newThread();
            }
        } else {
            GETThread();
        }
    }

    private String indexHTML2(List list) throws IOException {
        StringBuffer builder = new StringBuffer();
        builder.append("<html>");
        builder.append("<head>");
        builder.append("<title>");
        builder.append(FORUM_TITLE);
        builder.append("</title>");
        builder.append("</head>");
        builder.append("<body>");
        builder.append(form());
        builder.append(table(list));
        builder.append("</body>");
        builder.append("</html>");
        return builder.toString();
    }

    private String indexHTML(List list) throws IOException {
        StringBuffer builder = new StringBuffer();
        builder.append("<html>");
        builder.append("<head>");
        //builder.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"../../css/board.css\" media=\"all\">");
        builder.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"http://");
        builder.append(request.getHost() + "/css/board.css");
        builder.append("\" media=\"all\">");
        builder.append("<title>");
        builder.append(FORUM_TITLE);
        builder.append("</title>");
        builder.append("</head>");
        builder.append("<body>");
        builder.append(table(list));
        builder.append("</body>");
        builder.append("</html>");
        return builder.toString();
    }

    private String table(List list) throws IOException {
        if (list == null) {
            return "";
        }
        StringBuffer builder = new StringBuffer();
        builder.append("<table>");
        for (int i = 0; i < list.size(); i++) {
            builder.append("<tr>");
            String[] datas = list.get(i).toString().split(",");
            for (int j = 0; j < datas.length; j++) {
                String[] values = datas[j].split(":", 2);
                builder.append("<td>");
                if (values.length >= 2) {
                    //builder.append(StringEscapeUtils.escapeHtml4(values[1]));
                    builder.append(StringEscapeUtils.escapeHtml4((URLDecoder.decode(values[1], "UTF-8"))));
                }
                builder.append("</td>");
            }
            builder.append("<td><form method=\"POST\" action=\"/program/board/\"><br/>");
            builder.append("<input type=\"hidden\" name=\"_method\" value=\"DELETE\">");
            builder.append("<input type=\"hidden\" name=\"threadID\" value=\"");
            builder.append(i);
            builder.append("\">");
            builder.append("パスワード:<input type=\"text\" name=\"password\" size=\"20\">");
            builder.append("<input type=\"submit\" name=\"button\" value=\"削除\">");
            //builder.append("<button type='submit' name='threadID' value='");
            builder.append("</form></td>");
            builder.append("</tr>");
        }
        builder.append("</table>");
        return builder.toString();
    }

    private String form() {
        StringBuffer builder = new StringBuffer();
        builder.append("<FORM action=\"/program/board/\" method=post>");
        builder.append(" あなたのお名前は：<INPUT type='text' name=\"name\" value=\"\"><BR>");
        builder.append("テキスト：<TEXTAREA name=\"text\"></TEXTAREA>");
        builder.append("<INPUT type=\"submit\" name=\"button\" value=\"入力\">");
        builder.append("<input type=\"file\" name=\"example1\">");
        builder.append("</FORM>");

        return builder.toString();
    }

    public void newThread() throws IOException {
        List<String> list = data.getData();
        User user = new User();
        String name =request.getParameter("name");
        //System.out.println(user.exists(request.getParameter("name")));
        if(!user.exists(name)){
            user.saveData(user.newUser(name,data.getNewId(data.getData())));
        }

        File file = new File(CSV_FILEPATH, CSV_FILENAME);
        String text = addNewThread(request);
        list.add(text);
        data.saveData(text, file);
        mainData = list;
    }

    public void findThread(String name) throws IOException {
        mainData = findUserThread(data.getData(), name);
    }

    public void editThread() {

    }

    public void deleteThread(int id) throws IOException {
        List<String> list = data.getData();
        if (data.getParameter(list, id, "password").isEmpty()) {
            return;
        }
        if (request.getParameter("password").equals(data.getParameter(list, id, "password"))) {
            list.remove(id);
            mainData = list;
            data.saveData(list, new File(CSV_FILEPATH, CSV_FILENAME));
            return;
        }
        System.out.println("パスワードが合っていません");
    }

    private String addNewThread(HTTPRequest request) throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append("id:");
        sb.append(data.getNewId(data.getData()));
        sb.append(",name:");
        sb.append(request.getParameter("name"));
        sb.append(",title:");
        sb.append(request.getParameter("title"));
        sb.append(",text:");
        sb.append(request.getParameter("text"));
        sb.append(",password:");
        sb.append(request.getParameter("password"));
        sb.append(",date:");
        sb.append(data.getDate());
        sb.append(",icon:");
        sb.append("blank");
        return sb.toString();
    }

    public void GETThread() {
        mainData = data.getData();
    }

    public List<String> findUserThread(List<String> list, String name) throws UnsupportedEncodingException {
        List<String> data = new ArrayList<>();
        for (int i = 0; i <= list.size() - 1; i++) {
            if (name.equals(this.data.getParameter(list, i, "name"))) {
                data.add(list.get(i));
            }
        }
        return data;
    }

    void generateResponse() {
        response.addResponseHeader("Content-Type", "text/html; charset=UTF-8");
        if (statusCode == 0 || statusCode == 200) {
            try {
                response.setResponseBody(indexHTML(mainData));
            } catch (IOException e) {
                statusCode = 500;
                generateErrorPage(statusCode);
            }
        } else {
            generateErrorPage(statusCode);
        }
    }

    void dataCleaner() {


    }
}
