package pt.isel.ls;

import org.junit.Test;
import java.sql.Connection;

import static junit.framework.Assert.assertEquals;

public class SQLTest {
    Connection con = null;

    @Test
    public void connectionTest() throws Exception {
        try {
            con = EnvVars.connect(true);
            assertEquals(true, con != null);
        } finally {
            assert con != null;
            if (con != null) {
                con.close();
            }
        }
    }
}
