package pt.isel.ls.Server;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedList;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pt.isel.ls.CommandParser;
import pt.isel.ls.Commands.Command;
import pt.isel.ls.Dtos.DtoWrapper;
import pt.isel.ls.Dtos.Tag;
import pt.isel.ls.Router;
import pt.isel.ls.Server.Utils.GetRootInfo;
import pt.isel.ls.Server.Utils.WrapperChecklistView;
import pt.isel.ls.Server.Utils.WrapperTagsDetailed;
import pt.isel.ls.Utils.GetConnection;
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

            if (req.getPathInfo().equals("/")) {
                Connection con = GetConnection.connect(false);
                try {
                    respBody = cPrinter.print(new GetRootInfo().execute(null, con), map, req.getRequestURI());
                    respBodyBytes = respBody.getBytes(utf8);
                    resp.setStatus(200);
                }finally {
                    if(!con.isClosed()){
                        con.close();
                    }
                }
            } else if(req.getPathInfo().equals("/about")){
                respBody = cPrinter.print(req.getPathInfo(), map, req.getRequestURI());
                respBodyBytes = respBody.getBytes(utf8);
                resp.setStatus(200);

            } else {
                CommandParser cparser = new CommandParser(new String[]{req.getMethod(), req.getPathInfo(), getContentType(req.getHeader("accept"))});
                Router r = new Router(cparser.getMethod(), cparser.getPath(), cparser.getParams());
                Command c = r.Route();
                Object obj = r.run(c);

                if (obj == null) {
                    respBody = cPrinter.print(null, map, req.getRequestURI());

                    respBodyBytes = respBody.getBytes(utf8);
                    resp.setStatus(404);

                } else {
                    if (cparser.getPath()[1].equals("checklists") && obj instanceof LinkedList && getContentType(req.getHeader("accept")).contains("text/html")) {
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
                    }
                    else if(cparser.getPath()[1].equals("tags") && obj instanceof DtoWrapper && getContentType(req.getHeader("accept")).contains("text/html")){
                        String link = req.getPathInfo().contains("checklists") ? "/tags/"+ ((Tag)((LinkedList)((DtoWrapper) obj).getTag()).get(0)).getTg_id(): "/tags";

                        respBody = cPrinter.print(new WrapperTagsDetailed(link, (DtoWrapper) obj), map, req.getRequestURI());

                    }
                    else {
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
