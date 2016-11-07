package pt.isel.ls;

import org.junit.Test;
import pt.isel.ls.Commands.Command;
import pt.isel.ls.Commands.GetChecklists;
import pt.isel.ls.Commands.GetChecklistsCid;
import pt.isel.ls.Commands.PostTemplatesTidCreate;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;

public class RouterTest {


    @Test
    public void validRoutingTest() throws Exception {
        String args[] = {"GET", "/checklists"};

        CommandParser cp = new CommandParser(args);
        Router r = new Router(cp.getMethod(), cp.getPath(), cp.getParams());
        Command c = r.Route();

        assertEquals(true, c instanceof GetChecklists);
    }


    @Test
    public void nonExistingPathTest() throws Exception {
        String args[] = {"GET", "/Lunch"};

        CommandParser cp = new CommandParser(args);
        Router r = new Router(cp.getMethod(), cp.getPath(), cp.getParams());
        Command c = r.Route();

        assertEquals(true, c == null);
    }

    @Test
    public void pathWithIDS() throws Exception {
        String args[] = {"POST", "/templates/1/create"};

        CommandParser cp = new CommandParser(args);
        Router r = new Router(cp.getMethod(), cp.getPath(), cp.getParams());
        Command c = r.Route();
        assertEquals(true, c instanceof PostTemplatesTidCreate);
    }

    @Test
    public void headersTest() throws Exception {
        String args[] = {"GET", "/checklists/0", "accept:text/html"};

        CommandParser cp = new CommandParser(args);
        Router r = new Router(cp.getMethod(), cp.getPath(), cp.getParams());
        Command c = r.Route();
        assertEquals(true, c instanceof GetChecklistsCid);
        HashMap<String,String> hm = cp.getHeaders();
        assertEquals(true, hm.get("accept").equals("text/html"));
    }
}
