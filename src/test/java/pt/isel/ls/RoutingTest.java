package pt.isel.ls;

import org.junit.Test;
import pt.isel.ls.Commands.Command;
import pt.isel.ls.Commands.GetChecklists;

import static junit.framework.Assert.assertEquals;

public class RoutingTest {


    @Test
    public void validRoutingTest() throws Exception {
        String args[] = {"GET", "/checklists"};

        Routing r = new Routing(args);
        Command c = r.Route();

        assertEquals(true, c instanceof GetChecklists);
    }


    @Test
    public void nonExistingPathTest() throws Exception {
        String args[] = {"GET", "/Lunch"};

        Routing r = new Routing(args);
        Command c = r.Route();

        assertEquals(true, c == null);
    }
}
