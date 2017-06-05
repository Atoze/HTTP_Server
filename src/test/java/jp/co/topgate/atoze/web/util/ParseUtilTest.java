package jp.co.topgate.atoze.web.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static jp.co.topgate.atoze.web.util.ParseUtil.parseQueryString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by atoze on 2017/05/24.
 */
public class ParseUtilTest {
    @Test
    public void parseQueryString_NULL() {
        Map<String, String> map = parseQueryString(null);
        assertThat(null, is(map));
    }

    @Test
    public void parseQueryString_通常() {
        String text = "hoge=foo";
        Map<String, String> map = parseQueryString(text);
        assertThat(1, is(map.size()));
        assertThat("foo", is(map.get("hoge")));

        text = "hoge=foo&hoge2=bar";
        map = parseQueryString(text);
        assertThat(2, is(map.size()));
        assertThat("foo", is(map.get("hoge")));
        assertThat("bar", is(map.get("hoge2")));

        text = "&";
        map = parseQueryString(text);
        assertThat(0, is(map.size()));
        assertThat(new HashMap<>(), is(map));
    }
}
