package jp.co.topgate.atoze.web;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by atoze on 2017/05/08.
 */
public class Message {
    List<String> list = new ArrayList<String>();

    private String table() throws IOException {
        File file = new File("./src/main/resources/program/save.csv");
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        StringBuffer builder = new StringBuffer();

        Map<String, String> data = new HashMap<String, String>();
        builder.append("<table>");

        String line = br.readLine();
        while (line != null) {
            builder.append("<tr>");
            String[] datas = line.split(",");
            for (int i = 0; i < datas.length; i++) {
                this.list.add(datas[i]);
                String[] values = datas[i].split(":", 2);
                builder.append("<td>");
                if (values.length >= 2) {
                    data.put(values[0], values[1]);
                    builder.append(URLDecoder.decode(values[1], "UTF-8"));
                }
                builder.append("</td>");
            }
            builder.append("</tr>");
            //data.add(line);
            line = br.readLine();
        }
        builder.append("</table>");
        return builder.toString();
    }



    private void readCSV() throws IOException {
        File file = new File("");
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line = br.readLine();
        while (line != null) {
            this.list.add(line);
            line = br.readLine();
        }
    }

    private String getCsvData(int id, String key) throws UnsupportedEncodingException {
        String[] datas = this.list.get(id).split(",");
        for (String data1 : datas) {
            String[] values = data1.split(":", 2);
            if (values.length >= 2) {
                Map<String, String> data = new HashMap<>();
                data.put(values[0], values[1]);
                return URLDecoder.decode(data.get(key), "UTF-8");
            }
        }
        return null;
    }



    private List readLastId(List list) throws IOException {
        int index = list.size() - 1;
        String last = list.get(index).toString();
        String[] data = last.split(",", 0);
        while (true) {
            list.remove(index);
            index = index - 1;
            last = list.get(index).toString();
            data = last.split(",", 0);
            break;
        }
        //this.id = Integer.parseInt(data[0]) + 1;
        //this.id = Integer.parseInt(data[0]) + 1;
        //System.out.println(last);
        return list;
    }
}
