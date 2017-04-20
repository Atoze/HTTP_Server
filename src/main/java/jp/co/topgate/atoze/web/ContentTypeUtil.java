package jp.co.topgate.atoze.web;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by atoze on 2017/04/13.
 */
class ContentTypeUtil {
    private static final Map<String, String> content = new HashMap<String, String>() {
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

    public static String getFileExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        int point = fileName.lastIndexOf(".");
        if (point != -1) {
            return fileName.substring(point + 1, fileName.length());
        }
        return null;
    }

    public static String getContentType(String filePath) {
        filePath = getFileExtension(filePath);
        if (filePath == null) {
            return null;
        }

        if (content.containsKey(filePath)) {
            return content.get(filePath) + "/" + filePath;
        } else {
            //DefaultContentType
            return content.get("plain") + "/plain";
        }
    }
}
