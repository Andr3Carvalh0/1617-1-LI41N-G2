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
import pt.isel.ls.Commands.GetChecklists;
import pt.isel.ls.Router;
import pt.isel.ls.Utils.Output.CustomPrinter;

public class Service extends HttpServlet {
    private static CustomPrinter cPrinter = new CustomPrinter();

    private Connection con;

    public Service(Connection con) {
        this.con = con;

    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Charset utf8 = Charset.forName("utf-8");
            resp.setContentType(String.format("text/html; charset=%s", utf8.name()));
            String respBody = "";
            byte[] respBodyBytes;

            if(req.getPathInfo().equals("/")) {
                HashMap<String, String> map = new HashMap<>();
                map.put("accept", "text/html");
                respBody = cPrinter.print("main", map, req.getRequestURI());
                respBodyBytes = respBody.getBytes(utf8);
                resp.setStatus(200);

            }else if(req.getPathInfo().equals("/about")){
                HashMap<String, String> map = new HashMap<>();
                map.put("accept", "text/html");
                respBody = cPrinter.print("about", map, req.getRequestURI());
                respBodyBytes = respBody.getBytes(utf8);
                resp.setStatus(200);

            }
            else{
                CommandParser cparser = new CommandParser(new String[]{req.getMethod(), req.getPathInfo()});
                Router r = new Router(cparser.getMethod(), cparser.getPath(), cparser.getParams());
                Command c = r.Route();
                Object obj = r.run(c);

                if(obj == null){
                    respBody = cPrinter.print(null, new HashMap<>(), req.getRequestURI());
                    respBodyBytes = respBody.getBytes(utf8);
                    resp.setStatus(404);

                }else {
                    respBody = cPrinter.print(obj, cparser.getHeaders(), req.getPathInfo());
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
}
