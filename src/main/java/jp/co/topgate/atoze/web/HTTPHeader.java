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

public class HTTPHeader {
    private Status status;
    private String headerText;
    private final static String method = "GET|POST|HEAD|OPTIONS|PUT|DELETE|TRACE";

    private String headMethod = null;
    private String filePath = null;
    private String fileQuery = null;
    private String protocolVer = null;

    private boolean correctMethod = false;

    private final int RequestHeaderValue = 3;

    public HTTPHeader(InputStream in) throws IOException {
        this.headerText = this.readRequestHeader(in);
        this.readRequestBodyLine(in);
    }

    private String readRequestHeader(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = br.readLine();

        String headerLines[] = line.split(" ");

        if (headerLines.length == RequestHeaderValue) {
            checkHTTPMethod(headerLines[0]);
            this.protocolVer = headerLines[2];
            String URIQuerys[] = headerLines[1].split("\\?");
            if (URIQuerys[0] != headerLines[1]) {
                this.fileQuery = URIQuerys[1];
            }

            this.filePath = "." + URIQuerys[0];
        } else {
            status.setStatusCode(400);
            this.correctMethod = false;
        }

        StringBuilder header = new StringBuilder();

        while (line != null && !line.isEmpty()) {

            header.append(line + "\r\n");
            line = br.readLine();
        }
        return header.toString();
    }

    private void checkHTTPMethod(String method) { //正しいヘッダであるか否か
        Pattern p = Pattern.compile(this.method);
        Matcher m = p.matcher(method);

        if (m.find()) {
            this.headMethod = m.group();
        } else {
            this.status.setStatus(500);
            this.correctMethod = false;
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

    public String getFilePath() {
        return this.filePath;
    }

}
