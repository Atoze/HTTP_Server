package jp.co.topgate.atoze.web;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by atoze on 2017/04/13.
 */
public class ContentType {
    private String fileContentTypeText;
    private String fileExtension;

    private void allContentTypeLists (String contentTyp){
        HashMap<String,String> content = new HashMap<String, String>() {
            {
                put("plain" ,"text");
                put("html"  ,"text");
                put("css"   ,"text");
                put("xml"   ,"text");

                put("js"    ,"application");
                put("json"  ,"application");

                put("jpeg"  ,"image");
                put("jpg"   ,"image");
                put("png"   ,"image");
                put("gif"   ,"image");

                put("mp4"   ,"video");


            }
        };
        if (content.containsKey(contentTyp)){
            this.fileContentTypeText= content.get(contentTyp);
        }else{
            this.fileContentTypeText= content.get("plain");
            //this.fileExtension = "plain";
        }

    }

    public ContentType(String fileName) {

        int point = fileName.lastIndexOf(".");
        if (point != -1) {
            this.fileExtension = fileName.substring(point+1, fileName.length());
            allContentTypeLists(this.fileExtension);
        }

    }

    public String getContentType(){
        return this.fileContentTypeText;

    }

    public String getExtension(){
        return this.fileExtension;
    }
}
