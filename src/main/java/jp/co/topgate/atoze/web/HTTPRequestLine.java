package jp.co.topgate.atoze.web;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by atoze on 2017/04/12.
 */

class HTTPRequestLine {
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

    private String headMethod;
    private String filePath;
    private String protocol;

    private final int RequestHeaderValue = 3;

    HTTPRequestLine(String line) {
        this.readRequestHeader(line);
    }

    private void readRequestHeader(String line) {
        if (line == null) {
            return;
        }
        String headerLines[] = line.split(" ");
        if (headerLines.length == this.RequestHeaderValue) {
            this.headMethod = isMethod(headerLines[0]);
            this.filePath = headerLines[1];
            if (headerLines[2].startsWith("HTTP/")) {
                this.protocol = headerLines[2];
            }
        }
    }

    private String isMethod(String method) {
        if (methods.contains(method)) {
            return method;
        }
        return null;
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
