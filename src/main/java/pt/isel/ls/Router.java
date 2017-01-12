package pt.isel.ls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.ls.Commands.Command;
import pt.isel.ls.Server.Service;
import pt.isel.ls.Utils.GetConnection;
import pt.isel.ls.Utils.Routing.CommandNode;
import pt.isel.ls.Utils.Routing.TreeCommands;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class Router {
    private String method;
    private String[] path;
    private HashMap<String, String> params;

    private Logger logger = LoggerFactory.getLogger(Router.class);

    public Router(String method, String[] path, HashMap<String, String> params) {
        this.method = method;
        this.path = path;
        this.params = params;
    }

    public Command Route() throws SQLException {

        CommandNode tree = new TreeCommands().getTree();
        CommandNode current;
        if ((current = searchNode_Method(tree, method)) == null) return null;

        //This is the Options Command
        if (current.getChildren() == null) return current.getCommand();

        return searchNode_Path(current, path);

    }

    private CommandNode searchNode_Method(CommandNode node, String cmp) {
        for (CommandNode child : node.getChildren()) {
            if (child.getName().equals(cmp)) {
                return child;
            }
        }
        return null;
    }

    private Command searchNode_Path(CommandNode node, String[] cmp) {
        for (CommandNode c_node : node.getChildren()) {
            Command cmd = c_node.getCommand();
            int i = 1;
            for (; i < cmd.getPath().length; i++) {
                if (!(match(path[i], cmd.getPath()[i]))) {
                    break;
                }
            }
            if (path.length == i) {
                return cmd;
            }
        }
        return null;
    }

    private boolean match(String s, String s1) {
        if (s.equals(s1)) return true;
        else if (s1.contains("id") && isNumeric(s)) {
            params.put(s1, s);
            return true;
        }
        return false;
    }

    public Object run(Command c) throws Exception {
        Connection con = GetConnection.connect(false);
        con.setAutoCommit(false);
        Object obj = null;
        try {
            if (c != null) {
                obj = c.execute(params, con);
                con.commit();
                logger.info("The" + c + "was committed!");
            }
        } catch (SQLException e) {
            logger.info("The" + c + "exit with error: " + e.getMessage());
            logger.info("Rolling back...");
            con.rollback();

        } finally {
            try {
                if (!con.isClosed()) {
                    con.close();
                    logger.info("Connection closed!");
                }
            } catch (SQLException e) {
                logger.info("There was an error when closing the connection");
            }
        }
        return obj;
    }

    private boolean isNumeric(String obj) {
        try {
            int i = Integer.parseInt(obj);
            return true;

        } catch (Exception e) {
            return false;

        }
    }
}
