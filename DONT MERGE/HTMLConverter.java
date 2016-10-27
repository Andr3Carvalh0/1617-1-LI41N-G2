 import java.util.*;
 import java.io.*;

public class HTMLConverter{
    private String outputName;
    private String baseFile;
    private LinkedList<String> message = new LinkedList();

    private final String lookFor_begin = "{{";
    private final String lookFor_end = "}}";
    private final String[] SUPPORTED_MARKERS = {"{{#FOR}}","{{#END}}"};
    private final String[] SPECIAL_WORDS = {"NUMBER"};

    public HTMLConverter(String out, String baseFile){
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

    //Works only for when the marker is only sigle line.
    //Eg:<th>{{table_header}}</th> or 
    //   {{#FOR}}<th>{{table_header}}</th>
    //
    //For more complex stuff like multiple line replacement use the other compile method
    public void compile(HashMap<String, String[]> map){
        for(int i = 0; i < message.size(); i++){
            String line = message.get(i);

            for(String key : map.keySet()){
                String flag = lookFor_begin + key + lookFor_end;
                int loop = 0;
                
                if(line.contains(flag)){
                    String result = "";

                    //Remove flags that arent use in this context
                    if(contains(line, SPECIAL_WORDS) && contains(line, SUPPORTED_MARKERS)){
                        result += line.replace(key, "");
                        
                        for(int j = 0; j < SUPPORTED_MARKERS.length; j++){ result = result.replace(SUPPORTED_MARKERS[j], ""); }
                        
                        result = result.replace(lookFor_begin + lookFor_end, "");
                    }

                    //Situation where we will have a for loop
                    else if(line.contains(SUPPORTED_MARKERS[0])){
                        String aux = line.replace(SUPPORTED_MARKERS[0], "");
                        result += copyValues(aux, flag, map.get(key));

                    }
                    else{ result += copyValues(line, flag, map.get(key)); }

                    message.set(i, result);
                }
            }
        }
    }

    //Usefull only when you want to create multiple(multiple lines) "objects" like:
    //{{#FOR}}{{NUMBER_table}}<table cellpadding="3">
    //  <tr>
    //      {{#FOR}}<th>{{table_header}}</th>
    //  </tr>
    //</table>{{#END}}{{NUMBER_table}}
    public void compile(LinkedList<HashMap<String, String[]>> list){
		for(int i = 0; i < message.size(); i++){
            String line = message.get(i);

            for(HashMap<String, String[]> map : list){
            	for(String key : map.keySet()){
                	String flag = lookFor_begin + key + lookFor_end;
                	int loop = 0;
                
                	if(line.contains(flag)){
                    	String result = "";
                    	String arr[] = new String[message.size() - i];
                    	int pos = 0;


                    	if(contains(line, SPECIAL_WORDS) && contains(line, SUPPORTED_MARKERS)){
                    		for(int j = i; j < message.size(); j++){
                    			line = message.get(i);
                    			if(line.contains(SUPPORTED_MARKERS[1])){
                    				//arr[pos] = line;
                    				break;
                    			}
                    			arr[pos] = line;
                    			pos++;
                    			i++;
                    		}
                    
                    		for(int h = 0; h < arr.length; h++){
                    			System.out.println(arr[h]);
                    		}
                    	}
                    	//Situation where we will have a for loop
                    	else if(line.contains(SUPPORTED_MARKERS[0])){
                        	String aux = line.replace(SUPPORTED_MARKERS[0], "");
                        	result += copyValues(aux, flag, map.get(key));

                    	}
                    	else{ result += copyValues(line, flag, map.get(key)); }

                    	message.set(i, result);
                	}
				}
            }
        }
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

        for(int i = 0; i < values.length; i++){ res += line.replace(flag, values[i]); }

        return res;
    }

    private String generateMessage(){
        String res = "";

        for(String line : message){ res += line; }

        return res;
    }

    private boolean contains(String line, String[] words){
        for(int i = 0; i < words.length; i++){
            if(line.contains(words[i])) 
                return true;
        }
        return false;
    }
}