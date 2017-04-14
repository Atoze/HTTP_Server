package jp.co.topgate.atoze.web;

//import java.util.Arrays;
//import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by atoze on 2017/04/12.
 */
public class RequestHeaderCheck {

    private final String method = "GET|POST|HEAD|OPTIONS|PUT|DELETE|TRACE";

    private String headMethod = null;
    private String filePath = null;
    private String fileQuery = null;
    private String protocolVer = null;

    private boolean correctMethod = false;

    private final int RequestHeaderValue = 3;

    public RequestHeaderCheck(String headerText) {
        String headerTexts[] = headerText.split("\\n+");
        String headerLines[] = headerTexts[0].split(" ");

        if (headerLines.length == RequestHeaderValue) {
            headMethod = headerLines[0];
            protocolVer = headerLines[2];
            String URIQuerys[] = headerLines[1].split("\\?");
            if (URIQuerys[0] != headerLines[1]) {
                fileQuery = URIQuerys[1];
            }
            filePath = "." + URIQuerys[0];

            if (!checkHTTPMethod()) {
                System.out.println("500エラー不正なリクエスト2");
                correctMethod = true;
            }

        } else {

            System.out.println("500エラー不正なリクエスト1");
            correctMethod = false;
            Status status = new Status();
        }
    }

    boolean checkHTTPMethod() { //正しいヘッダであるか否か
        Pattern p = Pattern.compile(this.method);
        Matcher m = p.matcher(this.headMethod);

        if (m.find()) {
            this.headMethod = m.group();
            return true;
        }
        return false;
    }

    public String getMethod() {
        return this.headMethod;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public boolean getCheckHTTPMethod() {
        return this.correctMethod;
    }


}
