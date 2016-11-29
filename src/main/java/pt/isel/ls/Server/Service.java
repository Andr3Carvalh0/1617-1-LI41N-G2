package pt.isel.ls.Server;


import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.isel.ls.CommandParser;
import pt.isel.ls.Commands.Command;
import pt.isel.ls.Dtos.Checklist;
import pt.isel.ls.Dtos.DtoWrapper;
import pt.isel.ls.Router;
import pt.isel.ls.Utils.Output.CustomPrinter;

public class Service extends HttpServlet {
    private static CustomPrinter cPrinter = new CustomPrinter();

    private final String path = "./views";

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Charset utf8 = Charset.forName("utf-8");
            resp.setContentType(String.format(req.getHeader("accept") + "charset=%s", utf8.name()));
            String respBody;
            byte[] respBodyBytes;
            HashMap<String, String> map = new HashMap<>();
            map.put("accept", req.getHeader("accept"));
            map.put("file-name", req.getHeader("file-name"));

            if (req.getPathInfo().equals("/") || req.getPathInfo().equals("/about")) {
                resp.setContentType(String.format("text/html; charset=%s", utf8.name()));
                respBody = cPrinter.print(req.getPathInfo(), map, req.getRequestURI());
                resp.setStatus(200);

            } else if (req.getPathInfo().contains(".png")) {
                respBody = "";

            } else {
                CommandParser cparser = new CommandParser(new String[]{req.getMethod(), req.getPathInfo()});
                Router r = new Router(cparser.getMethod(), cparser.getPath(), cparser.getParams());
                Command c = r.Route();
                Object obj = r.run(c);

                if (obj == null) {
                    respBody = cPrinter.print(null, map, req.getRequestURI());
                    resp.setStatus(404);

                } else {
                    if (obj instanceof DtoWrapper) {
                        respBody = cPrinter.print(obj, cparser.getHeaders(), req.getRequestURI());

                    } else {
                        if (((LinkedList) obj).size() > 0) {
                            if(((LinkedList) obj).get(0) instanceof Checklist){
                                int active;

                                if(req.getPathInfo().equals("/checklists")) {
                                    active = 0;
                                }else if(req.getPathInfo().contains("closed")) {
                                    active = 1;
                                }else if(req.getPathInfo().contains("duedate")) {
                                    active = 2;
                                }else{
                                    active = 3;
                                }
                                respBody = cPrinter.print(new WrapperChecklistView((LinkedList) obj, active), cparser.getHeaders(), req.getRequestURI());

                            }else{
                                respBody = cPrinter.print(obj, cparser.getHeaders(), req.getRequestURI());
                            }
                        }else{
                            respBody = cPrinter.print(obj, cparser.getHeaders(), req.getRequestURI());
                        }
                    }
                    resp.setStatus(200);
                }
            }

            respBodyBytes = respBody.getBytes(utf8);
            resp.setContentLength(respBodyBytes.length);
            OutputStream os = resp.getOutputStream();
            os.write(respBodyBytes);
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
