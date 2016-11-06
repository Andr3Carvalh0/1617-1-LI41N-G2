package pt.isel.ls.Utils;

import pt.isel.ls.Commands.Command;
import pt.isel.ls.Commands.GetChecklists;
import pt.isel.ls.Commands.GetChecklistsCid;
import pt.isel.ls.Dtos.BaseDTO;
import pt.isel.ls.Router;

import java.util.HashMap;
import java.util.LinkedList;

/*
*
* This is just a test class.
* If this class remains in the delivered version, please ignore it!
*
 */


public class SIXSIXSIX {

    public static void main(String args[]) throws Exception {
        Router r = new Router(null, null, null);
        Command gc = new GetChecklistsCid();

        Object obj = r.run(gc);

        LinkedList<HashMap<String, String[]>> listHTML = new LinkedList<>();
        LinkedList<HashMap<String, String[]>> listJSON = new LinkedList<>();

        if (obj instanceof LinkedList) {
            int j = 0;
            //HTML
            for (Object e : (LinkedList) obj) {
                HashMap<String, String[]> map = new HashMap<>();
                String title[] = {"Result"};
                map.put("page_title", title);

                if(j == 0)
                    map.put("table_header", ((BaseDTO) e).getPropertiesNames());

                map.put("table_value", ((BaseDTO) e).getPropertiesValues());
                    j++;
                listHTML.add(map);
            }

            //JSON
            //Since we know that the result was a linkedlist we add the Collection attribute
            HashMap<String, String[]> map = new HashMap<>();
            String class_types[] = {"\"" + ((BaseDTO) ((LinkedList) obj).getFirst()).getDTOName() + "\"", "\"Collections\""};
            map.put("class_types", class_types);

            //Since we have more than 1 type, the prop_header will only have the count attribute
            String props[] = {"\"count\" : " + class_types.length + ""};
            map.put("prop_header", props);

            //listJSON.add(map);
            for (Object e : (LinkedList) obj) {
                int numberOfProperties = ((BaseDTO) e).getPropertiesNames().length;

                props = new String[numberOfProperties];

                String name[] = {"\"" + ((BaseDTO) e).getDTOName() + "\""};
                map.put("class_name", name);

                for (int i = 0; i < numberOfProperties; i++) {

                    String begin = "\"" + ((BaseDTO) e).getPropertiesNames()[i] + "\" : ";

                    if (isInteger((((BaseDTO) e).getPropertiesValues()[i]), 10) || isBoolean((((BaseDTO) e).getPropertiesValues()[i]))) {
                        begin += ((BaseDTO) e).getPropertiesValues()[i];

                    } else {
                        begin += "\"" + ((BaseDTO) e).getPropertiesValues()[i] + "\"";

                    }

                    props[i] = begin;

                }
                map.put("prop_body", props);

                listJSON.add(map);
                map = new HashMap<>();
            }

        }

        try {
            System.out.println(SIXSIXSIX.class.getClassLoader().getResource("."));
            System.out.println("<---- BEGIN HTML TEST ---->");

            Converter c = new Converter();
            c.compile(listHTML, true, SIXSIXSIX.class.getClassLoader().getResource("./views/template_list.html").getPath());
            c.commit(c.getMessage(), "index.html");

            System.out.println("<---- BEGIN JSON TEST ---->");
            Converter c1 = new Converter();
            //c1.compile(listJSON,false, SIXSIXSIX.class.getClassLoader().getResource("./views/template_list.json").getPath());
            //c.commit(c.getMessage(), "index.json");


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static boolean isInteger(String s, int radix) {
        if (s == null) return false;

        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) return false;
                else continue;
            }
            if (Character.digit(s.charAt(i), radix) < 0) return false;
        }
        return true;
    }

    private static boolean isBoolean(String s) {
        if (s == null) return false;
        if (s.isEmpty()) return false;

        return s.equals("true") || s.equals("false");
    }
}
