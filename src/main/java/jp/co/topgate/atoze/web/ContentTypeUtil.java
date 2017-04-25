package jp.co.topgate.atoze.web;

import java.util.HashMap;
import java.util.Map;

/**
 * ファイルの名前から拡張子を取得し,それに合わせた適切なContent-Typeを返します.
 *
 * @author atoze
 */
class ContentTypeUtil {
    private static final Map<String, String> CONTENT = new HashMap<String, String>() {
        {
            put("octet-stream", "application");
            put("plain", "text");
            put("html", "text");
            put("htm", "text");
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
     *
     * @param fileName ファイル名
     * @return ファイル拡張子
     */
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

    /**
     * Content-Typeを返します.
     *
     * @param fileName ファイル名
     * @return Content-Type
     */
    public static String getContentType(String fileName) {
        fileName = getFileExtension(fileName);
        if (fileName == null) {
            return null;
        }

        if (CONTENT.containsKey(fileName)) {
            return CONTENT.get(fileName) + "/" + fileName;
        } else {
            if ("txt".equals(fileName)) {
                return CONTENT.get("plain") + "/plain";
            }
            //DefaultContentType
            return CONTENT.get("octet-stream") + "/octet-stream";
        }
    }
}
