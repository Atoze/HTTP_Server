package jp.co.topgate.atoze.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import java.io.InputStreamReader;
import java.util.HashMap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by atoze on 2017/04/12.
 */
class HTTPRequest {
    private String headerText;
    private String bodyText;
    private String method;
    private String filepath;
    private String fileQuery;
    private String protocolVer;
    //private String getFileQuery;
    private String host="localhost:8080";
    HashMap<String, Object> headerData = new HashMap<String, Object>();

    private void setHTTPRequestHeader(String key, Object value) {
        this.headerData.put(key, value);
    }

    public void setRequestText(InputStream input) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        String line = br.readLine();

        HTTPHeader header = new HTTPHeader();
        header.setHTTPHeader(line);

        StringBuilder text= new StringBuilder();
        while (line != null && !line.isEmpty()) {
            text.append(line).append("\n");
            String[] headerData = line.split(": ");
            if (headerData.length >= 2) {
                this.setHTTPRequestHeader(headerData[0], headerData[1]);
            }
            line = br.readLine();
        }
        this.headerText = text.toString();
        this.method = header.getMethod();
        this.filepath = header.getFilePath();
        this.protocolVer = header.getProtocol();
    }

    public String getHeaderText() {
        return this.headerText;
    }

    public String getBodyText() {
        return this.bodyText;
    }

    public String getMethod() {
        return this.method;
    }

    public String getFilePath() {
        URIDivider(this.filepath);
        URIQuerySplitter(this.filepath);
        return this.filepath;
    }

    private void URIDivider(String filepath) {
        String pattern = "http*.//";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(filepath);

        if (m.find()) {
            if (filepath.startsWith(m.group())) {
                this.filepath = filepath.substring(filepath.indexOf(this.host) + this.host.length());
            }
        }
    }

    private void URIQuerySplitter(String filepath){
        String URIQuerys[] = filepath.split("\\?");
        this.filepath = URIQuerys[0];
        if (URIQuerys[0] != filepath) {
            this.fileQuery = URIQuerys[1];
        }
    }

    public String getSpecificRequestLine(String key) {
        if (headerData.containsKey(key)) {
            return key + ": " + headerData.get(key);
        } else {
            return "No Data";
        }
    }
    public Object getRequestValue(String value) {
        return headerData.getOrDefault(value, "No Data");
    }

    public String getProtocolVer() {
        ProtocolVer(this.protocolVer);
        return this.protocolVer;
    }

    private void ProtocolVer(String proto) {
        if (proto != null && proto.startsWith("HTTP/")) {
            this.protocolVer = proto.substring(proto.indexOf("HTTP/") + "HTTP/".length());
        } else {
            Status.setStatusCode(400);
        }
    }
}
