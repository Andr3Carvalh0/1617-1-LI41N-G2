package pt.isel.ls.http;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sun.net.httpserver.HttpExchange;

public class FormUrlEncoded {

    // TODO assumes Content-Length present and UTF-8 charset
    // TODO does not handle repeated names
    public static Map<String,String> retrieveFrom(HttpServletRequest req) throws IOException{
        Map<String, String> map = new HashMap<String,String>();
        byte[] bytes = new byte[req.getContentLength()];
        req.getInputStream().read(bytes);
        String content = new String(bytes);
        String[] pairs = content.split("&");
        for(String pair : pairs) {
            String[] kvp = pair.split("=");
            map.put(URLDecoder.decode(kvp[0], "UTF-8"),
                    URLDecoder.decode(kvp[1], "UTF-8"));
        }
        return map;
    }
}
