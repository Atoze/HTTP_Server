package jp.co.topgate.atoze.web;

/**
 * URLパターンを保管する
 */
public enum URLPattern {
    /**
     * 名前とURLのパターンで登録する
     */
    PROGRAM_BOARD("/program/board/");

    private final String url;

    URLPattern(String url) {
        this.url = url;
    }

    /**
     * 登録したURLのパターンを取得するメソッド
     *
     * @return URLのパターンを返す
     */
    public String getURL() {
        return this.url;
    }
}
