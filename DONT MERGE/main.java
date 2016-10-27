 import java.util.*;
 import java.io.*;

public class main{

    public static void main(String args[]){
        HashMap<String, String[]> map = new HashMap<String, String[]>();
        HashMap<String, String[]> map1 = new HashMap<String, String[]>();
        
        String main[] = {"Andre's Name"};
        map.put("page_title", main);  

        String fors[] = {"2"};
        map.put("NUMBER_table", fors);  

        String header[] = {"HEADER1", "HEADER2"};
        map.put("table_header", header);    

        String body[] = {"BODY1", "BODY2", "BODY3"};
        map.put("table_value", body);   

//////////////////////////////////////////////////////////////////
        String main1[] = {"Diogo's Name"};
        map1.put("page_title", main);  

        String fors1[] = {"2"};
        map1.put("NUMBER_table", fors);  


        String header1[] = {"Cabecalho1", "Cabecalho2"};
        map1.put("table_header", header);    

        String body1[] = {"Corpo1", "Corpo2"};
        map1.put("table_value", body);  


        LinkedList<HashMap<String, String[]>> list = new LinkedList<HashMap<String, String[]>>();

        list.add(map);
        list.add(map1);

		try{
        	HTMLConverter util = new HTMLConverter("index.html", "template.html");
        	util.allocate();
        	util.compile(map);
        	util.commit();

            System.out.println("ENTERING MULTIPLE TABLES");
            HTMLConverter util1 = new HTMLConverter("index_complex.html", "template.html");
            util1.allocate();
            util1.compile(list);
            util1.commit();

        }catch(Exception e){

        }

    }
}