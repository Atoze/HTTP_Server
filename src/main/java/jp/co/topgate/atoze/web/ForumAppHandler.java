package jp.co.topgate.atoze.web;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by atoze on 2017/05/02.
 */
public class ForumAppHandler extends HTTPHandler {
    private static final String FORUM_TITLE = "簡易掲示板のテスト";
    //private User user = new User();
    private static final String CSV_FILEPATH = "./src/main/resources/program/";
    private static final String CSV_FILENAME = "save.csv";

    private List<String> mainData = new ArrayList<>();

    ForumData data;

    ForumAppHandler() throws IOException {
        data = new ForumData();
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
        String newThread = addNewThread(request);
        data.addList(newThread);
        data.saveData(newThread, file);
        mainData = data.getData();
    }

    public void findThread(String name) throws IOException {
        mainData= findUserThread(data.getData(), name);
    }

    public void editThread() {

    }

    public void deleteThread(int id) throws IOException {
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

    public void GETThread(){
        mainData = data.getData();
    }


    public List<String> findUserThread(List<String> list, String name) throws UnsupportedEncodingException {
        List<String> data = new ArrayList<>();
        for (int i = 0; i <= list.size() - 1; i++) {
            if (name.equals(this.data.getCsvData(list, i, "name"))) {
                data.add(list.get(i));
            }
        }
        return data;
    }

    private void generateResponse() throws IOException {
        File file = new File("./src/main/resources/program", "index.html");
        response.addResponseHeader("Content-Type", "text/html; charset=UTF-8");
        response.setResponseBody(indexHTML(mainData));
        //response.setResponseBody(file);
    }

    public void response(OutputStream output) throws IOException {
        generateResponse();
        response.writeTo(output, 200);
    }
}
