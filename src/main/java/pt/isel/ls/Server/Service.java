package pt.isel.ls.Server;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.HashMap;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.isel.ls.CommandParser;
import pt.isel.ls.Commands.Command;
import pt.isel.ls.Router;
import pt.isel.ls.Server.Utils.GetRootInfo;
import pt.isel.ls.Utils.GetConnection;
import pt.isel.ls.Utils.Output.CustomPrinter;

public class Service extends HttpServlet {
    private static CustomPrinter cPrinter = new CustomPrinter();

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
                } finally {
                    if (!con.isClosed()) {
                        con.close();
                    }
                }
            } else if (req.getPathInfo().equals("/about")) {
                respBody = cPrinter.print(req.getPathInfo(), map, req.getRequestURI());
                respBodyBytes = respBody.getBytes(utf8);
                resp.setStatus(200);

            } else {
                CommandParser cparser = new CommandParser(new String[]{req.getMethod(), req.getPathInfo(), getContentType(req.getHeader("accept"))});
                Router r = new Router(cparser.getMethod(), cparser.getPath(), cparser.getParams());
                Command c = r.Route();
                try{
                    Object obj = r.run(c);

                    if (c == null) {
                        respBody = cPrinter.print("not_found", map, req.getRequestURI());

                        respBodyBytes = respBody.getBytes(utf8);
                        resp.setStatus(404);

                    }else {

                        respBody = cPrinter.print(obj, map, req.getRequestURI());
                        respBodyBytes = respBody.getBytes(utf8);
                        resp.setStatus(200);
                    }
                }
                catch (Exception e){
                    respBody = cPrinter.print(null, map, req.getRequestURI());

                    respBodyBytes = respBody.getBytes(utf8);
                    resp.setStatus(500);

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

    private String getContentType(String accept) {
        if (accept.contains("text/html"))
            return "text/html";

        if (accept.contains("application/json"))
            return "application/json";

        if (accept.contains("text/plain"))
            return "text/plain";

        return "text/html";
    }
}
