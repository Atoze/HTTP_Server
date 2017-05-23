package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.util.Status;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by atoze on 2017/04/18.
 */
public class StatusTest {

    @Test
    public void Status管理をみるテスト() {
        Status status = new Status();
        assertThat(200, is(status.getStatusCode()));
        assertThat("OK", is(status.getStatusMessage()));
        assertThat("200 OK", is(status.getStatus()));

        status.setStatus(200);
        assertThat(200, is(status.getStatusCode()));
        assertThat("OK", is(status.getStatusMessage()));
        assertThat("200 OK", is(status.getStatus()));

        //存在しないステータスコードのテスト
        status = new Status(10);
        assertThat(10, is(status.getStatusCode()));
        assertThat("Unknown Status", is(status.getStatusMessage()));
        assertThat("Unknown Status", is(status.getStatus()));

        //コンストラクタで指定してから再度上書きするテスト
        status = new Status(200);
        status.setStatus(400);
        assertThat(400, is(status.getStatusCode()));
        assertThat("Bad Request", is(status.getStatusMessage()));
        assertThat("400 Bad Request", is(status.getStatus()));
    }
}
