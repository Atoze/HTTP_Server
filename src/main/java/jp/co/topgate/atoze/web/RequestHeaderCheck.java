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

    private String headMethod;
    private String filePath;
    private String protocolVer;

    private final int RequestHeaderValue = 3;

    public RequestHeaderCheck(String headerText){
        String headerTexts[] = headerText.split("\\n+");
        String headerLines[] = headerTexts[0].split(" ");
        if (headerLines.length == RequestHeaderValue) {
            headMethod = headerLines[0];
            filePath = "."+headerLines[1];
            protocolVer = headerLines[2];
            if(!checkHTTPMethod()){
                System.out.println("500エラー不正なリクエスト2");
            }

        }else{
            System.out.println("500エラー不正なリクエスト1");
            Status status = new Status();
        }
    }

    private boolean checkHTTPMethod (){ //正しいヘッダであるか否か
        Pattern p = Pattern.compile(method);
        Matcher m = p.matcher(headMethod);
        if(m.find()){
                headMethod = m.group();
                return true;
        }return false;
    }

    public String getMethod(){
        return this.headMethod;
    }
    public String getFilePath(){
        return this.filePath;
    }


}
