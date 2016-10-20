package pt.isel.ls;

import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;

import static junit.framework.Assert.assertEquals;

public class SQLTest {
    Connection con = null;

    @Test
    public void connectionTest() throws Exception {
        try {
            con = GetConnection.connect(true);
            assertEquals(true, con != null);
        } finally {
            assert con != null;
            if (con != null) {
                con.close();
            }
        }
    }
}
