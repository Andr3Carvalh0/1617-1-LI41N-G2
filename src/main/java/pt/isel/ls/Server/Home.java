package pt.isel.ls.Server;

import pt.isel.ls.Utils.Output.CustomPrinter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.HashMap;

public class Home extends HttpServlet{
    private static CustomPrinter cPrinter = new CustomPrinter();

    private Connection con;

    public Home(Connection con){
        this.con = con;

    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Charset utf8 = Charset.forName("utf-8");
        resp.setContentType(String.format("text/html; charset=%s",utf8.name()));
        HashMap<String, String> map = new HashMap<>();
        map.put("accept", "text/html");

        try {
            String respBody = cPrinter.print("main", map, req.getRequestURI());
            byte[] respBodyBytes = respBody.getBytes(utf8);
            resp.setStatus(200);
            resp.setContentLength(respBodyBytes.length);
            OutputStream os = resp.getOutputStream();
            os.write(respBodyBytes);
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
