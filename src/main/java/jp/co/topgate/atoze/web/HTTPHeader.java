package jp.co.topgate.atoze.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by atoze on 2017/04/12.
 */

class HTTPHeader {
    private Status status;
    private String headerText;
    private final static String method = "GET|POST|HEAD|OPTIONS|PUT|DELETE|TRACE";

    private String headMethod;
    private String filePath;
    private String fileQuery;
    private String protocolVer;

    private String host;

    //private boolean isMethod = false;

    private final int RequestHeaderValue = 3;

    public HTTPHeader(InputStream in) throws IOException {
        this.headerText = this.readRequestHeader(in);
        this.readRequestBodyLine(in);
    }

    private String readRequestHeader(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = br.readLine();

        //リクエストヘッダをパース
        if (br!=null){
        String headerLines[] = line.split(" ");
        if (headerLines.length == RequestHeaderValue) {
            isHTTPMethod(headerLines[0]);
            this.filePath = headerLines[1];
            this.protocolVer = headerLines[2];}
        }else {
            Status.setStatusCode(400);
            //this.isMethod = false;
        }

        //ホスト宣言確認
        this.host = br.readLine();

        StringBuilder header = new StringBuilder();
        while (line != null && !line.isEmpty()) {
            header.append(line + "\r\n");
            line = br.readLine();
        }
        return header.toString();
    }

    private void isHTTPMethod(String method) { //正しいヘッダであるか否か
        Pattern p = Pattern.compile(HTTPHeader.method);
        Matcher m = p.matcher(method);

        if (m.find()) {
            this.headMethod = m.group();
        } else {
            Status.setStatus(400);
            //this.isMethod = false;
        }
    }


    private StringBuilder readRequestBodyLine(InputStream in) {
        StringBuilder bodyText = new StringBuilder();
        return bodyText;
    }

    public String getHeaderText() {
        return this.headerText;
    }

    public String getMethod() {
        return this.headMethod;
    }

    public String getHost() {
        return this.host;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public String getFileQuery() {
        return this.fileQuery;
    }

    public String getProtocolVer() {
        return this.protocolVer;
    }

}
