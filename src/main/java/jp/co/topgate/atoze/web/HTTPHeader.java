package jp.co.topgate.atoze.web;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by atoze on 2017/04/12.
 */

class HTTPHeader {
    private final static Set<String> methods = new HashSet<>();

    static {
        methods.add("GET");
        methods.add("POST");
        methods.add("HEAD");
        methods.add("OPTIONS");
        methods.add("PUT");
        methods.add("DELETE");
        methods.add("TRACE");
    }

    private final static String method = "GET|POST|HEAD|OPTIONS|PUT|DELETE|TRACE";

    private String headMethod;
    private String filePath;
    private String protocol;

    private final int RequestHeaderValue = 3;

    public HTTPHeader(String line){this.readRequestHeader(line);}

    private void readRequestHeader(String line) {
        //リクエストヘッダをパース
        if (line != null) {
            String headerLines[] = line.split(" ", this.RequestHeaderValue);
            if (headerLines.length == this.RequestHeaderValue) {
                isHTTPMethod(headerLines[0]);
                this.filePath = headerLines[1];
                this.protocol = headerLines[2];
            }
        }
    }

    //正しいHTTPメソッドであるか否か
    private void isHTTPMethod(String method) {
        Pattern p = Pattern.compile(HTTPHeader.method);
        Matcher m = p.matcher(method);

        if (m.find()) {
            this.headMethod = m.group();
        } else {
        }
    }

    public String getMethod() {
        return this.headMethod;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public String getProtocol() {
        return this.protocol;
    }
}
