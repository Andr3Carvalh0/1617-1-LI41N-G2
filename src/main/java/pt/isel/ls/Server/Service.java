package pt.isel.ls.Server;


import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.isel.ls.CommandParser;
import pt.isel.ls.Commands.Command;
import pt.isel.ls.Dtos.Checklist;
import pt.isel.ls.Router;
import pt.isel.ls.Utils.Output.CustomPrinter;

public class Service extends HttpServlet {
    private static CustomPrinter cPrinter = new CustomPrinter();

    private final String path = "./views";

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Charset utf8 = Charset.forName("utf-8");

            resp.setContentType(String.format(getContentType(req.getHeader("accept")) + "; charset=%s", utf8.name()));

            String respBody;
            byte[] respBodyBytes;
            HashMap<String, String> map = new HashMap<>();

            map.put("accept", getContentType(req.getHeader("accept")));

            //This is useless but you never know...
            map.put("file-name", req.getHeader("file-name"));

            if (req.getPathInfo().equals("/") || req.getPathInfo().equals("/about")) {
                respBody = cPrinter.print(req.getPathInfo(), map, req.getRequestURI());
                respBodyBytes = respBody.getBytes(utf8);
                resp.setStatus(200);

            } else {
                CommandParser cparser = new CommandParser(new String[]{req.getMethod(), req.getPathInfo()});
                Router r = new Router(cparser.getMethod(), cparser.getPath(), cparser.getParams());
                Command c = r.Route();
                Object obj = r.run(c);

                if (obj == null) {
                    respBody = cPrinter.print(null, map, req.getRequestURI());

                    respBodyBytes = respBody.getBytes(utf8);
                    resp.setStatus(404);

                } else {
                    if (obj instanceof LinkedList && ((LinkedList) obj).size() > 0 && ((LinkedList) obj).get(0) instanceof Checklist) {
                        int active;

                        if (req.getPathInfo().equals("/checklists")) {
                            active = 0;
                        } else if (req.getPathInfo().contains("closed")) {
                            active = 1;
                        } else if (req.getPathInfo().contains("duedate")) {
                            active = 2;
                        } else {
                            active = 3;
                        }
                        respBody = cPrinter.print(new WrapperChecklistView((LinkedList) obj, active), map, req.getRequestURI());
                    } else {
                        respBody = cPrinter.print(obj, map, req.getRequestURI());

                    }
                    respBodyBytes = respBody.getBytes(utf8);
                    resp.setStatus(200);
                }
            }

            resp.setContentLength(respBodyBytes.length);
            OutputStream os = resp.getOutputStream();
            os.write(respBodyBytes);
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getContentType(String accept){

        if(accept.contains("text/html"))
            return "text/html";

        if(accept.contains("application/json"))
            return "application/json";

        if(accept.contains("text/plain"))
            return "text/plain";

        return "text/html";
    }
}
