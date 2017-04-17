package jp.co.topgate.atoze.web;

import java.io.IOException;
import java.io.InputStream;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by atoze on 2017/04/12.
 */
class HTTPRequest {
    private String bodyText;
    private String headerText;
    private String method;
    private String filepath;
    private String fileQuery;
    private String protocol;
    //private String getFileQuery;
    private String host;

    public HTTPRequest(InputStream input) throws IOException {
        HTTPHeader header = new HTTPHeader(input);
        this.headerText = header.getHeaderText();
        this.method = header.getMethod();
        this.filepath = header.getFilePath();
        this.protocol = header.getProtocolVer();
        this.host=header.getHost();
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
        return "." + this.filepath;
    }

    private void URIDivider(String filepath) {
        String pattern = "http*.//";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(filepath);
        if (m.find()) {
            if (filepath.startsWith(m.group())) {
                this.filepath = filepath.substring(filepath.indexOf(this.host) + this.host.length());
                System.out.println(this.filepath);
            }
        }
        String URIQuerys[] = filepath.split("\\?");
        this.filepath = URIQuerys[0];
        if (URIQuerys[0] != filepath) {
            this.fileQuery = URIQuerys[1];
        }
    }

    public String getProtocol() {
        ProtocolVer(this.protocol);
        return this.protocol;
    }
    public String getHost(){
        return this.host;
    }

    public void ProtocolVer(String proto) {
        if (proto != null && proto.startsWith("HTTP/")) {
                this.protocol = proto.substring(proto.indexOf("HTTP/") + "HTTP/".length());
            } else {
                //this.protocol = null;
                Status.setStatusCode(400);
            }
        //return this.protocol;
        }
    }
