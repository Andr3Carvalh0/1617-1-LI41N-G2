package pt.isel.ls.Server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.ls.CommandParser;
import pt.isel.ls.Commands.Command;
import pt.isel.ls.Router;
import pt.isel.ls.Server.Utils.GetRootInfo;
import pt.isel.ls.Utils.GetConnection;
import pt.isel.ls.Utils.Output.CustomPrinter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.io.ByteStreams.toByteArray;

public class Service extends HttpServlet {
    private static CustomPrinter cPrinter = new CustomPrinter();
    private Logger logger = LoggerFactory.getLogger(Service.class);
    private ConcurrentHashMap<String, byte[]> file_cache = new ConcurrentHashMap<>();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            logger.info("GET: request to: " + req.getPathInfo());
            Charset utf8 = Charset.forName("utf-8");

            resp.setContentType(String.format(getContentType(req.getHeader("accept")) + "; charset=%s", utf8.name()));

            String respBody;
            byte[] respBodyBytes;
            HashMap<String, String> map = new HashMap<>();

            map.put("accept", getContentType(req.getHeader("accept")));

            map.put("file-name", req.getHeader("file-name"));

            //Handles root view(/)
            if (req.getPathInfo().equals("/")) {
                logger.info("GET: Attempting to access main view!");
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
            }
            //Static files
            else if(req.getPathInfo().contains("/js/") || req.getPathInfo().equals("/about") || req.getPathInfo().contains("/images/")){
                logger.info("GET: Attempting to access static file!");
                String path = req.getPathInfo().substring(1, req.getPathInfo().length());

                if(req.getPathInfo().equals("/about")){
                    path ="views/html/about.html";
                }

                if(req.getPathInfo().contains("/images/")){
                    resp.setContentType("image/" + path.split("\\.")[1]);
                }else if(req.getPathInfo().contains("/js/")){
                    resp.setContentType("application/javascript");
                }
                else{
                    resp.setContentType(String.format(getContentType(req.getHeader("accept")) + "; charset=%s", utf8.name()));
                }

                logger.info("GET: The static file have this path: " + path);

                respBodyBytes = file_cache.computeIfAbsent(path, s -> {
                    try {
                        return toByteArray(ClassLoader.getSystemResourceAsStream(s));
                    } catch (IOException e) {
                        return null;
                    }
                });


            }
            //The others...
            else {
                logger.info("GET: Attempting to access dynamic file!");
                CommandParser cparser = new CommandParser(new String[]{req.getMethod(), req.getPathInfo(), getContentType(req.getHeader("accept"))});
                Router r = new Router(cparser.getMethod(), cparser.getPath(), cparser.getParams());
                Command c = r.Route();
                logger.info("Command to run: " + c);
                try {
                    Object obj = r.run(c);
                    if (c == null) {
                        respBody = cPrinter.print("not_found", map, req.getRequestURI());
                        logger.info("GET: The path provider isnt valid.404 Error!");
                        respBodyBytes = respBody.getBytes(utf8);
                        resp.setStatus(404);

                    } else {

                        logger.info("GET: Attempting to construct the file, with the info from the DB!");
                        respBody = cPrinter.print(obj, map, req.getRequestURI());
                        respBodyBytes = respBody.getBytes(utf8);
                        resp.setStatus(200);
                    }
                } catch (Exception e) {
                    logger.info("GET: Server Error: " + e.getMessage());
                    respBody = cPrinter.print(null, map, req.getRequestURI());

                    respBodyBytes = respBody.getBytes(utf8);
                    resp.setStatus(500);

                }
            }

            logger.info("GET: The request will be in the following format: " + resp.getContentType());
            resp.setContentLength(respBodyBytes.length);
            OutputStream os = resp.getOutputStream();
            os.write(respBodyBytes);
            os.close();

        } catch (Exception e) {
            logger.info("GET: Something went wrong: " + e.getMessage());
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            logger.info("POST request to: " + req.getPathInfo());
            Map<String, String[]> col = req.getParameterMap();
            Charset utf8 = Charset.forName("utf-8");

            resp.setContentType(String.format(getContentType(req.getHeader("accept")) + "; charset=%s", utf8.name()));
            logger.info("POST: The request will be in the following format: " + resp.getContentType());

            String respBody;
            byte[] respBodyBytes;
            HashMap<String, String> map = new HashMap<>();

            map.put("accept", getContentType(req.getHeader("accept")));

            //This is useless but you never know...
            map.put("file-name", req.getHeader("file-name"));

            String params = formatParams(col, req.getPathInfo());
            CommandParser cparser = new CommandParser(new String[]{req.getMethod(), req.getPathInfo(), params});
            Router r = new Router(cparser.getMethod(), cparser.getPath(), cparser.getParams());
            Command c = r.Route();
            logger.info("Command to run: " + c);
            try {
                Object obj = r.run(c);
                if (c == null) {
                    respBody = cPrinter.print("not_found", map, req.getRequestURI());
                    logger.info("POST: The path provider isnt valid.404 Error!");
                    respBodyBytes = respBody.getBytes(utf8);
                    resp.setStatus(404);

                } else {
                    logger.info("POST: result from executing the command: " + obj);
                    if (!req.getPathInfo().matches(".*\\d+.*")) {
                        resp.sendRedirect(req.getPathInfo() + "/" + obj);
                    } else {
                        resp.sendRedirect(rebuildURL(req.getPathInfo()));
                    }
                    return;
                }
            } catch (Exception e) {
                respBody = cPrinter.print(null, map, req.getRequestURI());
                logger.info("POST: Server Error: " + e.getMessage());
                respBodyBytes = respBody.getBytes(utf8);
                resp.setStatus(500);

            }

            resp.setContentLength(respBodyBytes.length);
            OutputStream os = resp.getOutputStream();
            os.write(respBodyBytes);
            os.close();

        } catch (Exception e) {
            logger.info("POST: Couldn't send the response: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
        //Maybe add support for deletion if we have
    }

    private String rebuildURL(String pathInfo) {
        String[] p = pathInfo.split("/");
        return "/" + p[1] + "/" + p[2] + "/";
    }

    private String formatParams(Map<String, String[]> col, String req) {
        String toReturn = "";

        if (col.size() == 0 && checkIfTasksPost(req)) {
            toReturn = "isClosed=false";

        } else {
            for (String key : col.keySet()) {
                if (!col.get(key)[0].equals("")) {
                    String value = col.get(key)[0].replace(' ', '+');

                    if (key.equals("isClosed")) {
                        value = "true";
                    }

                    toReturn += key + "=" + value + "&";
                }
            }
        }
        return toReturn.substring(0, toReturn.length() - 1);
    }

    private boolean checkIfTasksPost(String req) {
        String[] res = req.split("/");
        return res.length == 5 && (res[1].equals("checklists") && res[2].matches(".*\\d+.*") && res[3].equals("tasks") && res[4].matches(".*\\d+.*"));

    }
}
