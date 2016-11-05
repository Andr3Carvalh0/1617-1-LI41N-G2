package pt.isel.ls;

import org.junit.Test;
import pt.isel.ls.Commands.*;
import pt.isel.ls.Dtos.Tag;
import pt.isel.ls.Utils.GetConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

import static junit.framework.Assert.assertEquals;

public class TagTest {
    private static final String SUCCESS = "Success!" ;
    private Connection con = null;
    private final String TAG_COLOR = "Blood_Red";
    private final String TAG_NAME = "I_am_a_test_tag";
    private final String CHECKLIST_NAME = "I_am_a_test_checklist";
    private final String CHECKLIST_DESCRIPTION = "I_am_a_test_checklist";
    private final String CHECKLIST_DATE = "06-10-2016";

    private int getLastInsertedTag(Connection con) throws SQLException {
        String s = "select max(Tg_id) from tag";
        PreparedStatement ps = con.prepareStatement(s);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    private int addChecklist(Connection con, String name, String desc, String date) throws SQLException {
        String s1 = "insert into checklist(Cl_name,  Cl_desc, Cl_duedate) values (?, ?, CAST(? as datetime))";
        PreparedStatement ps = con.prepareStatement(s1, PreparedStatement.RETURN_GENERATED_KEYS);

        ps.setString(1, name);
        ps.setString(2, desc);
        ps.setString(3, date);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        return rs.getInt(1);
    }

    private int addTag(Connection con, String name, String color) throws SQLException {
        String s1 = "insert into tag values (?, ?)";
        PreparedStatement ps = con.prepareStatement(s1, PreparedStatement.RETURN_GENERATED_KEYS);

        ps.setString(1, name);
        ps.setString(2, color);

        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        return rs.getInt(1);
    }

    private void deleteTag(int gid, Connection con) throws SQLException {
        String s = "delete from tag where Tg_id = ?";
        PreparedStatement ps = con.prepareStatement(s);
        ps.setInt(1,gid);
        ps.execute();
    }

    @Test
    public void TestpostAndDeleteTag() throws Exception {
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

            String r = (String) new DeleteTagsGid().execute(map,con);
            assertEquals(SUCCESS, r);

            s = "select * from tag where Tg_id = ?";
            ps = con.prepareStatement(s);
            ps.setInt(1,result);
            rs = ps.executeQuery();
            rs.next();
            int d = 0;
            while(rs.next())d++;
            assertEquals(0,d);
        }
        finally {
            if (con != null){
                if (result != -1) {
                    deleteTag(result,con);
                }
                con.close();
            }
        }
    }

    @Test
    public void TestPostChecklistsCidTagsAndThenDeletes() throws Exception {
        int cid = -1;
        int gid = -1;
        try{
            HashMap<String, String> map = new HashMap<>();
            map.put("name", TAG_NAME);
            map.put("color", TAG_COLOR);
            con = GetConnection.connect(true);
            cid = addChecklist(con, CHECKLIST_NAME,CHECKLIST_DESCRIPTION, CHECKLIST_DATE);
            gid = addTag(con, TAG_NAME, TAG_COLOR);
            map.put("cid", Integer.toString(cid));
            map.put("gid", Integer.toString(gid));

            String result = (String) new PostChecklistsCidTags().execute(map,con);
            assertEquals(SUCCESS, result);

            String s = "select * from tag_checklist where Tg_id = ? and Cl_id = ?";
            PreparedStatement ps = con.prepareStatement(s);
            ps.setInt(1,gid);
            ps.setInt(2,cid);
            ResultSet rs = ps.executeQuery();
            rs.next();
            assertEquals(gid, rs.getInt(1));
            assertEquals(cid, rs.getInt(2));

            String dels = (String) new DeleteChecklistsCidTagsGid().execute(map,con);
            assertEquals(dels, SUCCESS);
            s = "select * from tag_checklist where Tg_id = ? and Cl_id = ?";
            ps = con.prepareStatement(s);
            ps.setInt(1,gid);
            ps.setInt(2,cid);
            rs = ps.executeQuery();
            assertEquals(rs.next(),false);

        }
        finally {
            if(con!=null){
                if(cid != -1){
                    String s = "delete from tag_checklist where Tg_id = ? and Cl_id = ?";
                    PreparedStatement ps = con.prepareStatement(s);
                    ps.setInt(1,gid);
                    ps.setInt(2,cid);
                    ps.execute();

                    s = "delete from checklist where Cl_id = ?";
                    ps = con.prepareStatement(s);
                    ps.setInt(1,cid);

                    deleteTag(gid,con);
                }
                con.close();
            }
        }
    }

    @Test
    public void TestGetTags() throws Exception {
        int[] gid ={-1,-1,-1};
        try {
            HashMap<String, String> map = new HashMap<>();
            con = GetConnection.connect(true);
            for(int i = 0; i<gid.length; i++)
                gid[i] = addTag(con, TAG_NAME+i,TAG_COLOR+i);

            LinkedList<Tag> tl = (LinkedList<Tag>) new GetTags().execute(map,con);
            for(int i=0; i<gid.length; i++){
                assertEquals(tl.get(i).getTg_id(),gid[i]);
            }
        }
        finally {
            if(con!=null) {
                if (gid[0] != -1) {
                    for(int i=0; i<gid.length; i++) deleteTag(gid[i],con);
                }
            }
        }

    }
}
