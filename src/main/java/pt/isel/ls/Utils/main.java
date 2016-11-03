/*
**
* SPANISH INQUISITION
* 666
*
* DONT USE THIS CLASS.THIS WAS JUST FOR THE DEVELOPMENT OF THE CONVERTER CLASS!
* DONT USE THIS CLASS.THIS WAS JUST FOR THE DEVELOPMENT OF THE CONVERTER CLASS!
* DONT USE THIS CLASS.THIS WAS JUST FOR THE DEVELOPMENT OF THE CONVERTER CLASS!
* DONT USE THIS CLASS.THIS WAS JUST FOR THE DEVELOPMENT OF THE CONVERTER CLASS!
* DONT USE THIS CLASS.THIS WAS JUST FOR THE DEVELOPMENT OF THE CONVERTER CLASS!
* DONT USE THIS CLASS.THIS WAS JUST FOR THE DEVELOPMENT OF THE CONVERTER CLASS!
*
*
*/

package pt.isel.ls.Utils;

import pt.isel.ls.Commands.Command;
import pt.isel.ls.Commands.GetChecklists;
import pt.isel.ls.Dtos.BaseDTO;
import pt.isel.ls.Router;

import java.util.HashMap;
import java.util.LinkedList;

public class main {
    public static void main(String args[]) throws Exception {

        Router r = new Router(null, null, null);
        Command gc = new GetChecklists();

        Object obj = r.run(gc);

        LinkedList<HashMap<String, String[]>> listHTML = new LinkedList<>();
        LinkedList<HashMap<String, String[]>> listJSON = new LinkedList<>();

        if (obj instanceof LinkedList) {

            //HTML
            for (Object e : (LinkedList) obj) {
                HashMap<String, String[]> map = new HashMap<>();
                String title[] = {"Result"};
                map.put("page_title", title);

                map.put("table_header", ((BaseDTO) e).getProperties());

                map.put("table_value", ((BaseDTO) e).getPropertiesValues());

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
                int numberOfProperties = ((BaseDTO) e).getProperties().length;

                props = new String[numberOfProperties];

                String name[] = {"\"" + ((BaseDTO) e).getDTOName() + "\""};
                map.put("class_name", name);

                for (int i = 0; i < numberOfProperties; i++) {

                    String begin = "\"" + ((BaseDTO) e).getProperties()[i] + "\" : ";

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
            System.out.println(main.class.getClassLoader().getResource("."));
            System.out.println("<---- BEGIN HTML TEST ---->");
            Converter c = new Converter("index.html", main.class.getClassLoader().getResource("./views/template.html").getPath(), true);
            c.compile(listHTML);

            System.out.println("<---- BEGIN JSON TEST ---->");
            Converter c1 = new Converter("index.json", main.class.getClassLoader().getResource("./views/template.json").getPath(), false);
            c1.compile(listJSON);


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