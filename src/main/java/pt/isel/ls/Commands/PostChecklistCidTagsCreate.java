package pt.isel.ls.Commands;

import java.sql.Connection;
import java.util.HashMap;

public class PostChecklistCidTagsCreate extends Command{
    private final String[] path = {"", "checklists", "{cid}", "tags", "create"};

    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws Exception {

        //Create Tag
        String tag_id = new PostTags().execute(params, con) + "";

        params.put("gid", tag_id);

        return new PostChecklistsCidTags().execute(params, con);
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public String[] getPath() {
        return path;
    }
}
