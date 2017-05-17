package jp.co.topgate.atoze.web.HTMLEditor;

import java.io.*;

/**
 * Created by atoze on 2017/05/15.
 */
abstract class HTMLEditor {
    String doctype;
    String html;
    String title;
    String header;
    String body;
    final String lineFeed = System.getProperty("line.separator");

    public String getHTML() {
        return HTMLField();
    }

    public static void exportFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.createNewFile()) {
            return;
        }
        OutputStream output = new FileOutputStream(file);
        PrintWriter writer = new PrintWriter(output, true);
    }

    String doctypeField() {
        StringBuffer sb = new StringBuffer();
        sb.append("<!DOCTYPE ");
        sb.append(doctype);
        sb.append(">").append(lineFeed);
        return sb.toString();
    }

    String HTMLField(String attribute, String value) {
        StringBuffer sb = new StringBuffer();
        sb.append("<html");
        if (attribute != null && !attribute.isEmpty()) {
            sb.append(" ").append(attribute).append("=\"").append(value).append("\"");
        }
        sb.append(">");
        sb.append(lineFeed);
        sb.append(headerField());
        sb.append(bodyField());
        sb.append("</html>");
        this.html = sb.toString();
        return sb.toString();
    }

    String HTMLField() {
        return HTMLField(null, null);
    }

    String headerField() {
        StringBuffer sb = new StringBuffer();
        sb.append("<head>").append(lineFeed);
        if (header != null && !header.isEmpty()) {
            sb.append(header);
        }
        sb.append(generateHeader());
        sb.append("</head>").append(lineFeed);
        return sb.toString();
    }

    String bodyField() {
        StringBuffer sb = new StringBuffer();
        sb.append("<body>").append(lineFeed);
        if (body != null && !body.isEmpty()) {
            sb.append(body);
        }
        sb.append(generateBody());
        sb.append("</body>").append(lineFeed);
        return sb.toString();
    }

    abstract String generateHeader();

    abstract String generateBody();

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getHeader() {
        return headerField();
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return generateBody();
    }
}
