package jp.co.topgate.atoze.web;

import java.util.HashMap;

/**
 * Created by atoze on 2017/04/13.
 */
class ContentType {
    private String fileContentTypeText;
    private String fileExtension;

    private void allContentTypeLists(String contentType) {
        HashMap<String, String> content = new HashMap<String, String>() {
            {
                put("plain", "text");
                put("html", "text");
                put("css", "text");
                put("xml", "text");

                put("js", "application");
                put("json", "application");

                put("jpeg", "image");
                put("jpg", "image");
                put("png", "image");
                put("gif", "image");

                put("mp4", "video");
            }
        };
        if (content.containsKey(contentType)) {
            this.fileContentTypeText = content.get(contentType);
        } else {
            this.fileContentTypeText = content.get("plain");
            this.fileExtension = "plain";
            //this.fileExtension = "plain";
        }

    }

    public void setContentType(String fileName) {
        int point = fileName.lastIndexOf(".");
        if (point != -1) {
            this.fileExtension = fileName.substring(point + 1, fileName.length());
            allContentTypeLists(this.fileExtension);
        }
    }

    public void setContentTypeDefault() {
        allContentTypeLists("none");
    }

    //public String getContentTypeDefault(){
    //    return "text" + "/" + "plain";

    public String getContentType(String fileName) {

        return this.fileContentTypeText + "/" + this.fileExtension;
    }

    public String getContentTypeValue() {
        return this.fileContentTypeText;
    }

    public String getExtension() {
        return this.fileExtension;
    }
}
