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
    private String headerText;
    private final String method = "GET|POST|HEAD|OPTIONS|PUT|DELETE|TRACE";

    private String headMethod;
    private String filePath;
    private String fileQuery;
    private String protocolVer;

    private boolean correctMethod = false;

    private final int RequestHeaderValue = 3;

    public HTTPHeader(InputStream in) throws IOException {
        this.headerText = this.readRequestHeader(in);
        this.readRequestBodyLine(in);
    }

    private String readRequestHeader(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = br.readLine();

        //String headerTexts[] = headerText.split("\\n+");
        String headerLines[] = line.split(" ");

        if (headerLines.length == RequestHeaderValue) {

            this.headMethod = headerLines[0];
            this.protocolVer = headerLines[2];
            String URIQuerys[] = headerLines[1].split("\\?");
            if (URIQuerys[0] != headerLines[1]) {
                this.fileQuery = URIQuerys[1];
            }

            this.filePath = "." + URIQuerys[0];
        } else {

            System.out.println("500エラー不正なリクエスト1");
            correctMethod = false;
            Status status = new Status();
        }

        StringBuilder header = new StringBuilder();

        while (line != null && !line.isEmpty()) {

            header.append(line + "\r\n");
            line = br.readLine();
        }
        return header.toString();
    }

    private void checkHTTPMethod() { //正しいヘッダであるか否か
        Pattern p = Pattern.compile(this.method);
        Matcher m = p.matcher(this.headMethod);

        if (m.find()) {
            this.headMethod = m.group();
            //eturn true;
        }//return false;
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
