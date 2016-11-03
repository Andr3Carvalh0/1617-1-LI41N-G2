package pt.isel.ls.Commands;

import pt.isel.ls.Dtos.Template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

public class GetTemplates extends Command {
    private String method = "GET";
    private String[] path = {"", "templates"};
    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws SQLException {
        String s = "select * from template";
        PreparedStatement ps = con.prepareStatement(s);
        ResultSet rs = ps.executeQuery();
        LinkedList<Template> templateLinkedList = new LinkedList<>();
        while(rs.next()){
            templateLinkedList.add(new Template(rs.getInt(1), rs.getString(2), rs.getString(3)));
        }
        return templateLinkedList;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String[] getPath() {
        return path;
    }

}
