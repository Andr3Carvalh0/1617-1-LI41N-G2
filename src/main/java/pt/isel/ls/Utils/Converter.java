package pt.isel.ls.Utils;

import java.util.*;
import java.io.*;

public class Converter{
    private String outputName;
    private String baseFile;
    private LinkedList<String> message = new LinkedList();

    private final String lookFor_begin_HTML = "{{";
    private final String lookFor_end_HTML = "}}";
    private final String[] SUPPORTED_MARKERS_HTML = {"{{#FOR}}","{{#FOR_M}}","{{#END_M}}"};

    private final String lookFor_begin_JSON = "<<";
    private final String lookFor_end_JSON = ">>";
    private final String[] SUPPORTED_MARKERS_JSON = {"<<#FOR>>","<<#FOR_M>>","<<#END_M>>"};

    public Converter(String out, String baseFile){
        outputName = (out != null) ? out : "index.html";
        this.baseFile = baseFile;
    }

    //Reads the file to memory
    public void allocate() throws Exception{
        Scanner io = null;
        try{
            io = new Scanner(new File(baseFile));
       
            while(io.hasNextLine( )){ 
                message.add(io.nextLine() + "\n");
            }

        }catch(FileNotFoundException e){
            throw new Exception("Cannot read file!");
        }finally{ if(io != null){ io.close(); }}
    }


    //How do this work?
    // 1st - Search the template for the Mark {{#FOR_M}}
    // if we find it...Try to locate the {{#END_M}} mark
    // in beetween save the text form the {{#FOR_M}} until the {{#END_M}} line.
    //
    // 2nd - The string we saved will be repeated multiple times, so we clone it x times, and populate the values depending on the list values
    //
    // 3rd - Concatunate everything to make a valid html file
    // 4rd - Search for not applyed marks, and try to change them
    // 5th - Remove not used marks 
    public void compileHTML(LinkedList<HashMap<String, String[]>> list){
		for(int i = 0; i < message.size(); i++){
            String line = message.get(i);

            for(int j = 0; j < list.size(); j++){
                String result;
                LinkedList<String> aux = new LinkedList<>();
                HashMap<String, String[]> map;
                int initial_pos = i;
                int end_pos = 0;

                if(line.contains(SUPPORTED_MARKERS_HTML[1])){

                    //1st step
                	for(int k = i; k < message.size() && !(line = message.get(k)).contains(SUPPORTED_MARKERS_HTML[2]); k++, i++){
                    	//Remove Special Chars		                
                        for (String marks : SUPPORTED_MARKERS_HTML) {
                            line = line.replace(marks, "");
                        }
	                    line = line.replace(lookFor_begin_HTML + lookFor_end_HTML, "");

                    	aux.add(line);
                    	end_pos = k;
                 	}

                 	LinkedList<String> last = new LinkedList<>();

                    //2nd
                    for (HashMap<String, String[]> aList : list) {
                        for (String aux_values : aux) {
                            line = aux_values;
                            result = "";

                            map = aList;

                            for (String key : map.keySet()) {
                                String flag = lookFor_begin_HTML + key + lookFor_end_HTML;

                                if (line.contains(flag)) {
                                    result += copyValues(line, flag, map.get(key));
                                }

                            }
                            if (!result.equals("")) {
                                last.add(result);
                            } else {
                                last.add(aux_values);
                            }
                        }
                    }

              		//3rd - Concatonate
              		aux = new LinkedList<>();

              		for(int p = 0; p < initial_pos; p++){ aux.add(message.get(p)); }

              		for(String par : last){ aux.add(par); }

              		for(int w = end_pos + 2; w < message.size(); w++){ aux.add(message.get(w)); }

              		message = aux;
				}
            }
        }

        checkLostIDs(list);
        removeNotUsedMarkers();
    }

    // Saves the string message into the outputName file
    public void commit() throws Exception{
        try{
            PrintWriter writer = new PrintWriter(outputName, "UTF-8");
            writer.println(generateMessage());
            writer.close();
        }catch(FileNotFoundException e){
            throw new Exception("Cannot save file!");
        }
    }

    private String copyValues(String line, String flag, String... values){
        String res = "";

        for (String value : values) {
            res += line.replace(flag, value);
        }

        return res;
    }

    private void checkLostIDs(LinkedList<HashMap<String, String[]>> list){
        for(int i = 0; i < message.size(); i++){
            String line = message.get(i);

            for (HashMap<String, String[]> map : list) {
                for (String key : map.keySet()) {
                    String flag = lookFor_begin_HTML + key + lookFor_end_HTML;
                    if (line.contains(flag)) {
                        message.set(i, copyValues(line, flag, map.get(key)));
                    }
                }
            }   
        }
    }

    private void removeNotUsedMarkers(){
        for(int i = 0; i < message.size(); i++){
            for (String mark : SUPPORTED_MARKERS_HTML) {
                if (message.get(i).contains(mark)) {
                    message.set(i, message.get(i).replace(mark, ""));
                }
            }
        }
    }

    private String generateMessage(){
        String res = "";

        for(String line : message){ res += line; }

        return res;
    }
}