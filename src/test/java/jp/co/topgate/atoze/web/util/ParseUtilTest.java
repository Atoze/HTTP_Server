package jp.co.topgate.atoze.web.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static jp.co.topgate.atoze.web.util.ParseUtil.parseQueryData;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by atoze on 2017/05/24.
 */
public class ParseUtilTest {
    @Test
    public void parseQueryData_NULL() {
        Map<String, String> map = parseQueryData(null);
        assertThat(null, is(map));
    }

    @Test
    public void parseQueryData_通常() {
        String text = "hoge=foo";
        Map<String, String> map = parseQueryData(text);
        assertThat(1, is(map.size()));
        assertThat("foo", is(map.get("hoge")));

        text = "hoge=foo&hoge2=bar";
        map = parseQueryData(text);
        assertThat(2, is(map.size()));
        assertThat("foo", is(map.get("hoge")));
        assertThat("bar", is(map.get("hoge2")));

        text = "&";
        map = parseQueryData(text);
        assertThat(0, is(map.size()));
        assertThat(new HashMap<>(), is(map));
    }
}
