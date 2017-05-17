package jp.co.topgate.atoze.web.HTMLEditor;

import jp.co.topgate.atoze.web.Util.ContentType;

import java.util.*;

/**
 * Created by atoze on 2017/05/14.
 */

public class HTML5Editor extends HTMLEditor {
    private String title = "";
    private String bodyText = "";
    private String charset = "UTF-8";
    private String language = "en";

    private final String lineFeed = System.getProperty("line.separator");
    private Map<String, String[]> metaData = new HashMap<>();
    private List<String[]> stylesheet = new ArrayList<>();

    public HTML5Editor() {
        doctype = "html";
        setMeta("charset", charset);
    }

    public HTML5Editor(String body) {
        this();
        this.bodyText = body;
    }

    public HTML5Editor(String title, String body) {
        this(body);
        this.title = title;
    }

    public void setMetaData(String attribute, String value, String content) {
        metaData.put(attribute, new String[]{value, content});
    }

    String generateMetaDataField() {
        StringBuffer sb = new StringBuffer();
        Iterator entries = metaData.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String keyName = (String) entry.getKey();
            sb.append("<meta ").append(keyName).append("=\"");
            String[] valSize = metaData.get(keyName);
            String[] values = metaData.get(keyName);
            if (valSize.length >= 2) {
                sb.append(values[0]).append("\" content=\"").append(values[1]).append("\">").append(lineFeed);

            } else {
                sb.append(values[0]).append("\">").append(lineFeed);
            }
        }
        return sb.toString();
    }

    String generateLinkField() {
        StringBuffer sb = new StringBuffer();
        for (String[] stylesheet : this.stylesheet) {
            sb.append("<link rel=\"stylesheet\" href=\"").append(stylesheet[0]).append("\" type=\"").append(stylesheet[1]).append("\">");
        }
        return sb.toString();
    }

    String generateHeader() {
        StringBuffer sb = new StringBuffer();
        sb.append(generateMetaDataField());
        sb.append(generateLinkField());
        sb.append("<title>").append(title).append("</title>").append(lineFeed);
        return sb.toString();
    }

    @Override
    public String getHTML() {
        return doctypeField() + HTMLField("lang", language);
    }

    String generateBody() {
        return this.bodyText;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setKeywords(String keywords) {
        setMetaData("name", "keywords", keywords);
    }

    public void setMeta(String attribute, String content) {
        metaData.put(attribute, new String[]{content});
    }

    public void setStylesheet(String fileName) {
        stylesheet.add(new String[]{fileName, ContentType.getContentType(fileName)});
    }
}