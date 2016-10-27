package pt.isel.ls;

import org.junit.Test;
import pt.isel.ls.Commands.PostTags;
import pt.isel.ls.Commands.PostTemplates;
import pt.isel.ls.Dtos.Tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import static junit.framework.Assert.assertEquals;

public class TagTest {
    private Connection con = null;
    private final String TAG_COLOR = "Blood_Red";
    private final String TAG_NAME = "I_am_a_test_tag";

    private int getLastInsertedTag(Connection con) throws SQLException {
        String s = "select max(Tg_id) from tag";
        PreparedStatement ps = con.prepareStatement(s);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    @Test
    public void postAndGetTag() throws Exception {
        int result = -1;
        try {
            con = GetConnection.connect(true);
            HashMap<String, String> map = new HashMap<>();
            map.put("name", TAG_NAME);
            map.put("color", TAG_COLOR);
            result = (int) new PostTags().execute(map, con);
            assertEquals(getLastInsertedTag(con), result);

            String s = "select * from tag where Tg_id = ?";
            PreparedStatement ps = con.prepareStatement(s);
            ps.setInt(1,result);
            ResultSet rs = ps.executeQuery();
            rs.next();
            assertEquals(result, rs.getInt(1));
            assertEquals(TAG_NAME, rs.getString(2));
            assertEquals(TAG_COLOR, rs.getString(3));
        }
        finally {
            if (con != null){
                if (result != -1) {
                    String dels = "delete from tag where Tg_id = ?";
                    PreparedStatement ps = con.prepareStatement(dels);
                    ps.setInt(1, result);
                    ps.execute();
                }
                con.close();
            }
        }
    }

}
