package jp.co.topgate.atoze.web.htmlEditor;

import jp.co.topgate.atoze.web.util.ContentType;

import java.util.*;

/**
 * HTMLの形に沿ったStringを生成します.
 * //TODO 効率よくかく
 */
public class HTMLBuilder {

    private static final String LINE_FEED = System.getProperty("line.separator");

    private List<String> headData = new ArrayList<>();
    private List<String> bodyData = new ArrayList<>();
    private Map<String, List<String[]>> attribute = new HashMap<>();

    private String body;
    private String head;

    private String html;

    private String title;
    private Map<String, String[]> metaData = new HashMap<>();
    private List<String[]> stylesheet = new ArrayList<>();

    private String charset = "UTF-8";
    private String doctype = "html";

    public HTMLBuilder() {
        init();
    }

    private void init() {
        //初期言語設定
        setAttribute("html", true, "lang", "en");
        setMetaData("charset", charset);
    }

    private void initBody() {
        bodyData.clear();
    }

    private void initHeader() {
        headData.clear();
    }

    public void setHead(String head) {
        initHeader();
        headData.add(head);
    }

    public void addHead(String head) {
        headData.add(head);
    }

    public void setBody(String body) {
        initBody();
        bodyData.add(body);
    }

    public void addBody(String body) {

        bodyData.add(body);
    }

    private void generateHead() {
        String head = "";
        for (String headData : this.headData) {
            if (headData != null && !headData.isEmpty()) {
                head += headData + LINE_FEED;
            }
        }
        head = head + "<title>" + title + "</title>";
        this.head = head + generateMetaDataField() + generateLinkField();
    }

    public String getHeadData() {
        generateHead();
        return head;
    }

    private void generateBody() {
        String body = "";
        for (String bodyData : this.bodyData) {
            body += bodyData + LINE_FEED;
        }
        this.body = body;
    }

    public String getBodyData() {
        generateBody();
        return body;
    }

    public String getHTML() {
        generateHTML();
        return html;
    }

    private void generateHTML() {
        String doctypeField = "";
        if (doctype != null) {
            doctypeField = "<!DOCTYPE " + doctype + ">" + LINE_FEED;
        }
        generateHead();
        String headField = generateMainField("head", head, getAttribute("head")) + LINE_FEED;
        generateBody();
        String bodyField = generateMainField("body", body, getAttribute("body")) + LINE_FEED;
        String content = headField + bodyField;
        html = doctypeField + generateMainField("html", content, getAttribute("html"));
    }

    private String generateMainField(String tag, String content, List<String[]> attribute) {
        StringBuffer sb = new StringBuffer();
        sb.append("<");
        sb.append(tag);

        List<String[]> data = attribute;
        if (data == null) {
            data = new ArrayList<>();
        }
        if (data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                String[] value = data.get(i);
                if (value != null && value.length > 0) {
                    if (value[1] != null) {
                        sb.append(" ").append(value[0]).append("=\"").append(value[1]).append("\"");
                    } else {
                        sb.append(" ").append(value[0]);
                    }
                }
            }
        }
        sb.append(">").append(LINE_FEED);
        if (content != null) {
            sb.append(content);
            sb.append("</").append(tag).append(">");
        }
        return sb.toString();
    }

    public String generateField(String tag, String content) {
        return generateMainField(tag, content, null);
    }

    public String generateField(String tag, String content, String[]... attribute) {
        List<String[]> attributes = generateAttribute(tag, false, attribute);
        return generateMainField(tag, content, attributes);
    }

    private String generateMetaDataField() {
        StringBuffer sb = new StringBuffer();
        Iterator entries = metaData.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String keyName = (String) entry.getKey();
            sb.append("<meta ").append(keyName).append("=\"");
            String[] valSize = metaData.get(keyName);
            String[] values = metaData.get(keyName);
            if (valSize.length >= 2) {
                sb.append(values[0]).append("\" content=\"").append(values[1]).append("\">").append(LINE_FEED);

            } else {
                sb.append(values[0]).append("\">").append(LINE_FEED);
            }
        }
        return sb.toString();
    }

    private String generateLinkField() {
        StringBuffer sb = new StringBuffer();
        for (String[] stylesheet : this.stylesheet) {
            sb.append("<link rel=\"stylesheet\" href=\"").append(stylesheet[0]).append("\" type=\"").append(stylesheet[1]).append("\">");
        }
        return sb.toString();
    }

    public void setAttribute(String tag, boolean clear, String attribute, String value) {
        String[] str = new String[2];
        str[0] = attribute;
        if (value != null) {
            str[1] = value;
        }
        setAttribute(tag, clear, str);
    }

    public void setAttribute(String tag, boolean clear, String[]... attribute) {
        List<String[]> values = generateAttribute(tag, clear, attribute);
        this.attribute.put(tag, values);
    }

    private List<String[]> generateAttribute(String tag, boolean clear, String[]... attribute) {
        if (attribute == null) {
            return null;
        }
        List<String[]> values;
        if (clear) {
            values = new ArrayList<>();
        } else {
            values = this.attribute.getOrDefault(tag, new ArrayList<>());
        }
        values.addAll(Arrays.asList(attribute));
        return values;
    }

    private List<String[]> getAttribute(String tag) {
        return attribute.get(tag);
    }

    public void setMetaData(String attribute, String content) {
        metaData.put(attribute, new String[]{content});
    }

    public void setMetaData(String attribute, String value, String content) {
        metaData.put(attribute, new String[]{value, content});
    }

    public void setStylesheet(String fileName) {
        stylesheet.add(new String[]{fileName, ContentType.getContentType(fileName)});
    }

    public void setLanguage(String language) {
        setAttribute("html", true, "lang", language);
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
