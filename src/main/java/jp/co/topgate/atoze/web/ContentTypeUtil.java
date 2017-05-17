package jp.co.topgate.atoze.web;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import java.util.HashMap;
import java.util.Map;

/**
 * ファイルの名前から拡張子を取得し,それに合わせた適切なContent-Typeを返します.
 *
 * @author atoze
 */
public class ContentTypeUtil {
    private static final Map<String, String> CONTENT = new HashMap<String, String>() {
        {
            put("octet-stream", "application/octet-stream");
            put("plain", "text/plain");
            put("text", "text/plain");
            put("txt", "text/plain");
            put("html", "text/html");
            put("htm", "text/html");
            put("css", "text/css");
            put("xml", "text/xml");

            put("js", "application/javascript");
            put("json", "application/json");

            put("jpeg", "image/jpeg");
            put("jpg", "image/jpeg");
            put("png", "image/png");
            put("gif", "image/mp4");

            put("mp4", "video/mp4");
        }
    };

    public static String getKey(String value) {
        BidiMap<String, String> bidiMap = new DualHashBidiMap<>(CONTENT);
        if (value == null) {
            return null;
        }
        return bidiMap.getOrDefault(value, null);
    }

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
            return CONTENT.get(fileName);
        } else {
            //DefaultContentType
            return CONTENT.get("octet-stream");
        }
    }
}
