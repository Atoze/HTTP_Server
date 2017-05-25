package jp.co.topgate.atoze.web.app.forum;

/**
 * app.forumパッケージ内で使われるデータの名称を保管します.
 * このENUMを参照する際のインデックス番号、キー値、クエリのキー値で保存してください
 * インデックス番号は必ず順番に来るようにしてください.
 * また、DEFAULTのインデックス番号は必ず最後に来るようにしてください。
 */
public enum ForumDataPattern {
    ID(0, "ID", null),
    NAME(1, "NAME", "name"),
    TITLE(2, "TITLE", "title"),
    TEXT(3, "TEXT", "text"),
    PASSWORD(4, "PASSWORD", "password"),
    DATE(5, "DATE", null),
    ICON(6, "ICON", "icon"),
    DEFAULT(99, null, null);

    private final int index;
    private final String key;
    private String queryKey;

    ForumDataPattern(int index, String key, String queryKey) {
        this.index = index;
        this.key = key;
        this.queryKey = queryKey;
    }

    public int getIndex() {
        return this.index;
    }

    public String getKey() {
        return this.key;
    }

    public String getQueryKey() {
        return this.queryKey;
    }

    /**
     * インデックスIDから対応するキー値を返します.
     */
    public static String getKeyByIndex(int index) {
        String key = "";
        if (index == ForumDataPattern.DEFAULT.getIndex()) {
            return key;
        }
        for (ForumDataPattern forumDataPattern : ForumDataPattern.values()) {
            if (forumDataPattern.getIndex() == index) {
                key = forumDataPattern.getKey();
                break;
            }
        }
        return key;
    }

    /**
     * インデックスIDから対応するクエリキー値を返します.
     */
    public static String getQueryKeyByIndex(int index) {
        String key = "";
        if (index == ForumDataPattern.DEFAULT.getIndex()) {
            return key;
        }
        for (ForumDataPattern forumDataPattern : ForumDataPattern.values()) {
            if (forumDataPattern.getIndex() == index) {
                key = forumDataPattern.getQueryKey();
                break;
            }
        }
        return key;
    }

    public void setQueryKey(String queryKey){
        this.queryKey = queryKey;
    }
    /**
     * このENUMに保存されているDEFAULT以外のデータの数を返します.
     */
    public static int size() {
        return ForumDataPattern.values().length - 1;
    }
}
