package jp.co.topgate.atoze.web;

import java.util.HashMap;
import java.util.Map;

/**
 * ファイルの名前から拡張子を取得し,それに合わせた適切なContent-Typeを返します.
 * @author atoze
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

    /**
     * ファイルの拡張子を返します.
     * @param filePath ファイル名
     * @return ファイル拡張子
     */
    public static String getFileExtension(String filePath) {
        if (filePath == null) {
            return null;
        }
        int point = filePath.lastIndexOf(".");
        if (point != -1) {
            return filePath.substring(point + 1, filePath.length());
        }
        return null;
    }

    /**
     * Content-Typeを返します.
     * @param filePath ファイル名
     * @return Content-Type
     */
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
