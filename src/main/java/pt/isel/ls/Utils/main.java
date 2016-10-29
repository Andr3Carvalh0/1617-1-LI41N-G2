/*
**
* DONT USE THIS CLASS.THIS WAS JUST FOR THE DEVELOPMENT OF THE CONVERTER CLASS!
* DONT USE THIS CLASS.THIS WAS JUST FOR THE DEVELOPMENT OF THE CONVERTER CLASS!
* DONT USE THIS CLASS.THIS WAS JUST FOR THE DEVELOPMENT OF THE CONVERTER CLASS!
* DONT USE THIS CLASS.THIS WAS JUST FOR THE DEVELOPMENT OF THE CONVERTER CLASS!
* DONT USE THIS CLASS.THIS WAS JUST FOR THE DEVELOPMENT OF THE CONVERTER CLASS!
* DONT USE THIS CLASS.THIS WAS JUST FOR THE DEVELOPMENT OF THE CONVERTER CLASS!
*
*/
package pt.isel.ls.Utils;

import java.util.*;
import java.io.*;

public class main{
    public static void main(String args[]){
        LinkedList<HashMap<String, String[]>> listHTML = new LinkedList<>();
        LinkedList<HashMap<String, String[]>> listJSON= new LinkedList<>();

        HashMap<String, String[]> map = new HashMap<>();
        HashMap<String, String[]> map1 = new HashMap<>();

        //HTML
        String title[] = {"ANDRE PAGE"};
        map.put("page_title", title);

        String head[] = {"HEAD"};
        map.put("table_header", head);

        String body[] = {"BODY", "BODY1"};
        map.put("table_value", body);


        String cab[] = {"Cabeca"};
        map1.put("table_header", cab);

        String corppo[] = {"Corpo", "Corpo"};
        map1.put("table_value", corppo);

        listHTML.add(map);
        listHTML.add(map1);


        map = new HashMap<>();
        map1 = new HashMap<>();
        
        //JSON

        String class_types[] = {"\"checklist\"", "\"collection\""};
        map.put("class_types", class_types);

        String props[] = {"\"name\" : \"LS Phase 2\"", "\"description\" : \"complete LS phase 2\""};
        map.put("prop_header", props);




        String class_name[] = {"\"checklist0\"", "\"collection\""};
        map.put("class_name", class_name);

        String name[] = {"\"checklist1\""};
        map1.put("class_name", name);

        String props1[] = {"\"name\" : \"LS Phase 2\"", "\"description\" : \"complete LS phase 2\""};
        map.put("prop_body", props1);

        String props12[] = {"\"nome\" : \"Fase 2 LS\"", "\"descricao\" : \"Fase 2 de LS Completa\""};
        map1.put("prop_body", props12);

        listJSON.add(map);
        listJSON.add(map1);



        try{




            InputStream a = main.class.getClassLoader().getResourceAsStream("./main/views/template.html");


            BufferedReader reader = new BufferedReader(new InputStreamReader(a));
            String line = null;
            while ( (line = reader.readLine()) != null) {
                // do something with the line here
                System.out.println("Line read: " + line);
            }
            /*
            System.out.println("<---- BEGIN HTML TEST ---->");
            Converter c = new Converter("index.html", main.class.getClassLoader().getResource("main/views/template.html").getPath(), true);
            c.allocate();
            c.compile(listHTML);
            c.commit();

            System.out.println("<---- BEGIN JSON TEST ---->");
                Converter c1 = new Converter("index.json",null, false);
                c1.allocate();
                c1.compile(listJSON);
                c1.commit();
            */
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}