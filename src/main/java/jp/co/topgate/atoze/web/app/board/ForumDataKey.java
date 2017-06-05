package jp.co.topgate.atoze.web.app.board;

import org.jetbrains.annotations.NotNull;

/**
 * app.forumパッケージ内で使われるデータの名称を保管します.
 * このENUMを参照する際のインデックス番号、キー値、クエリのキー値で保存してください
 * インデックス番号は必ず順番に来るようにしてください.
 * また、DEFAULTのインデックス番号は必ず最後に来るようにしてください。
 */
public enum ForumDataKey {
    ID(0, "ID", null),
    ENCODER(1, "ENCODER", null),
    NAME(2, "NAME", "name"),
    TITLE(3, "TITLE", "title"),
    TEXT(4, "TEXT", "text"),
    PASSWORD(5, "PASSWORD", "password"),
    DATE(6, "DATE", null),
    ICON(7, "ICON", "icon"),
    DEFAULT(99, null, null);

    private final int index;
    private final String key;
    private String queryKey;

    ForumDataKey(final int index, String key, String queryKey) {
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

    public void setQueryKey(String queryKey) {
        this.queryKey = queryKey;
    }

    @NotNull
    public static ForumDataKey getKeyByIndex(int index) {
        ForumDataKey key;
        ForumDataKey[] patterns = ForumDataKey.values();
        for (int z = 0; z < patterns.length; z++) {
            key = patterns[z];
            if (key.getIndex() == index) {
                return key;
            }
        }
        return ForumDataKey.DEFAULT;
    }

    /**
     * このENUMに保存されているDEFAULT以外のデータの数を返します.
     */
    public static int size() {
        return ForumDataKey.values().length - 1;
    }
}
