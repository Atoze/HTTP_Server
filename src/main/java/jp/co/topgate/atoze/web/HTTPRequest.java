package jp.co.topgate.atoze.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by atoze on 2017/04/12.
 */
class HTTPRequest {
    private String headerText;
    private String method;
    private String filePath;
    private String fileQuery;
    private String protocolVer;
    private String host;
    Map<String, String> headerData = new HashMap<String, String>();

    HTTPRequest() {
    }

    private void addRequestData(String key, String value) {
        this.headerData.put(key, value);
    }

    public void readRequestText(InputStream input, String host) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        String line = null;
        line = br.readLine();

        HTTPRequestLine header = new HTTPRequestLine(line);

        StringBuilder text = new StringBuilder();
        while (line != null && !line.isEmpty()) {
            text.append(line).append("\n");
            String[] headerLineData = line.split(":",2);
            if (headerLineData.length == 2) {
                this.addRequestData(headerLineData[0].toUpperCase(), headerLineData[1].trim());
            }
            line = br.readLine();
        }
        this.host = host;
        this.headerText = text.toString();
        this.method = header.getMethod();
        this.filePath = header.getFilePath();
        this.protocolVer = header.getProtocol();
    }

    public String getHeaderText() {
        return this.headerText;
    }

    public String getMethod() {
        return this.method;
    }

    public String getFilePath() {
        this.filePath = uriQuerySplitter(urlDivider(this.filePath, this.host));
        return this.filePath;
    }

    private String urlDivider(String filePath, String host) {
        if (filePath == null) {
            return null;
        }

        String pattern = "http*.://";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(filePath);

        if (m.find()) {
            if (filePath.startsWith(m.group()+host)) {
                return filePath.substring(filePath.indexOf(host) + host.length());
            }
        }
        return filePath;
    }

    private String uriQuerySplitter(String filePath) {
        if (filePath == null) {
            return null;
        }

        String urlQuery[] = filePath.split("\\?",2);
        if (urlQuery[0] != filePath) {
            this.fileQuery = urlQuery[1];
        }
        return urlQuery[0];
    }

    public String getRequestValue(String value) {
        return headerData.getOrDefault(value, null);
    }

    public String getProtocolVer() {
        this.protocolVer = ProtocolVer(this.protocolVer);
        return this.protocolVer;
    }

    private String ProtocolVer(String protocol) {
        //if("HTTP/".startsWith(protocol)){
        if (protocol != null) {
            return protocol.substring(protocol.indexOf("HTTP/") + "HTTP/".length());
        } else {
            return null;
        }
        //}
    }

    public String getFileQuery() {
        return this.fileQuery;
    }
}
