package jp.co.topgate.atoze.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by atoze on 2017/04/12.
 */

class HTTPHeader {
    private final static String method = "GET|POST|HEAD|OPTIONS|PUT|DELETE|TRACE";

    private String headMethod;
    private String filePath;
    private String fileQuery;
    private String protocol;

    private String host;

    private final int RequestHeaderValue = 3;

    public void setHTTPHeader(String line){
        this.readRequestHeader(line);
    }

    private void readRequestHeader(String line) {
        //リクエストヘッダをパース
        if (line != null) {
            String headerLines[] = line.split(" ",this.RequestHeaderValue);
            if (headerLines.length == this.RequestHeaderValue) {
                isHTTPMethod(headerLines[0]);
                this.filePath = headerLines[1];
                this.protocol = headerLines[2];
            }
        } else {
            //Status.setStatusCode(400);
            this.filePath = "";
            this.protocol = "";
        }
        //return header.toString();
    }

    //正しいHTTPメソッドであるか否か
    private void isHTTPMethod(String method) {
        Pattern p = Pattern.compile(HTTPHeader.method);
        Matcher m = p.matcher(method);

        if (m.find()) {
            this.headMethod = m.group();
            //return true;
        } else {
            Status.setStatus(400);
            //return = false;
        }
    }
    public String getMethod() {
        return this.headMethod;
    }
    public String getFilePath() {
        return this.filePath;
    }
    public String getFileQuery() {
        return this.fileQuery;
    }
    public String getProtocol() {
        return this.protocol;
    }
}
