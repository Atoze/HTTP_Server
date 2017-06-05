package jp.co.topgate.atoze.web.util;

import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by atoze on 2017/04/18.
 */
public class StatusTest {

    @Ignore
    @Test
    public void Status管理をみるテスト() {
        Status status = Status.OK;
        assertThat(200, is(status.getCode()));
        assertThat("OK", is(status.getMessage()));
    }
}
