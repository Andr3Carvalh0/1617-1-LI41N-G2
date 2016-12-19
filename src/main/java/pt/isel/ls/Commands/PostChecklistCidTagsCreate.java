package pt.isel.ls.Commands;

import java.sql.Connection;
import java.util.HashMap;

public class PostChecklistCidTagsCreate extends Command{
    private final String[] path = {"", "checklists", "{cid}", "tasks", "create"};

    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws Exception {

        //Create Tag
        String tag_id = (String) new PostTags().execute(params, con);

        HashMap<String, String> map = new HashMap<>();

        map.put("{cid}", params.get("{cid}"));
        map.put("gid", tag_id);

        return new PostChecklistsCidTags().execute(map, con);
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
