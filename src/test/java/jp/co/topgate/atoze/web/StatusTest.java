package jp.co.topgate.atoze.web;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by atoze on 2017/04/18.
 */
public class StatusTest {

    @Test
    public void Status管理をみるテスト () {
        Status status = new Status();
        //assertNull(status.getStatus());//Null

        status.setStatus(200);
        assertThat(200,is(status.getStatusCode()));
        assertThat("OK",is(status.getStatusMessage()));
        assertThat("200 OK",is(status.getStatus()));
    }
}
