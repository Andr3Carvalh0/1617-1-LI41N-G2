package pt.isel.ls.Commands;

import pt.isel.ls.Dtos.Tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;

public class GetTags extends Command implements Get_Command{
    private String method = "GET";
    private String[] path = {"", "tags"};
    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws Exception {
        String s = "select * from tag";
        PreparedStatement ps = con.prepareStatement(s);
        ResultSet rs = ps.executeQuery();
        LinkedList<Tag> tagLinkedList = new LinkedList<>();
        while(rs.next()){
            tagLinkedList.add(new Tag(rs.getInt(1), rs.getString(2), rs.getString(3)));
        }
        return tagLinkedList;
    }

    @Override
    public String getMethod() { return method; }

    @Override
    public String[] getPath() {
        return path;
    }

    @Override
    public LinkedList<HashMap<String, String[]>> prepareForTransformartion() {
        return null;
    }
}
