package jp.co.topgate.atoze.web;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by atoze on 2017/05/02.
 */
public class ForumAppHandler extends Handler {
    static final String FORUM_TITLE = "簡易掲示板のテスト";
    private User user = new User();
    static final String CSV_FILEPATH = "./src/main/resources/program/";
    static final String CSV_FILENAME = "save.csv";

    ForumData data = new ForumData();

    ForumAppHandler() throws IOException {
    }
    private String indexHTML(List list) throws IOException {
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

    private String table(List list) throws IOException {
        StringBuffer builder = new StringBuffer();
        builder.append("<table>");
        for (int i = 0; i < list.size(); i++) {
            builder.append("<tr>");
            String[] datas = list.get(i).toString().split(",");
            for (int j = 0; j < datas.length; j++) {
                String[] values = datas[j].split(":", 2);
                builder.append("<td>");
                if (values.length >= 2) {
                    builder.append(URLDecoder.decode(values[1], "UTF-8"));
                }
                builder.append("</td>");
            }
            builder.append("</tr>");
        }
        builder.append("</table>");
        return builder.toString();
    }

    private String form() {
        StringBuffer builder = new StringBuffer();
        builder.append("<FORM action='FormInput1' method=post>");
        builder.append(" あなたのお名前は：<INPUT type='text' name='name' value=''><BR>");
        builder.append("テキスト：<TEXTAREA name='text'></TEXTAREA>");
        builder.append("<INPUT type='submit' name='button' value='入力'>");
        builder.append("</FORM>");
        return builder.toString();
    }

    public void newThread() throws IOException {
        File file = new File(CSV_FILEPATH, CSV_FILENAME);
        //data.readCSV(file);
        data.addList(addNewThread(request));
        data.saveData(addNewThread(request),file);
    }

    public void editThread() {

    }

    public void deleteThread(int id) throws IOException {
        //File file = new File("./src/main/resources/program/save.csv");
        File file = new File(CSV_FILEPATH, CSV_FILENAME);

        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        FileOutputStream output = new FileOutputStream(file);
        PrintWriter writer = new PrintWriter(output, true);
        StringBuffer sb = new StringBuffer();
        String line = br.readLine();

        for (int i = 0; i < id; i++) {
            //writer.println(line);
            sb.append(line);
            line = br.readLine();
        }
        sb.append("");
        br.readLine();

        System.out.println(sb.toString());
        writer.close();
        output.close();
    }


    private String addNewThread(HTTPRequest request) throws IOException {
        StringBuffer sb = new StringBuffer();
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


    private void generateResponse() throws IOException {
        File file = new File("./src/main/resources/program", "index.html");
        response.addResponseHeader("Content-Type", "text/html; charset=UTF-8");
        //response.setResponseBody(editHTML());
        response.setResponseBody(indexHTML(data.getData()));
        //response.setResponseBody(file);
    }

    public void response(OutputStream output) throws IOException {
        generateResponse();
        response.writeTo(output, 200);
    }
}
