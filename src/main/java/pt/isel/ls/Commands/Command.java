package pt.isel.ls.Commands;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public abstract class Command {
    public abstract Object execute(HashMap<String, String> params, Connection con) throws Exception;
    public abstract String getMethod();
    public abstract String[] getPath();


    public boolean validDate(String date){
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String current_date = dateFormat.format(new Date());

        if(date == null)
            return true;

        String[] user = date.split("-|//.|/");
        String[] current = current_date.split("-");

        if(user.length != current.length){
            return false;
        }

        if(Integer.valueOf(user[2]) > Integer.valueOf(current[2])){
           return true;
        }
        if(Integer.valueOf(user[1]) > Integer.valueOf(current[1]) && Integer.valueOf(user[1]) > 0 && Integer.valueOf(user[1]) < 13){
            return true;
        }

        return Integer.valueOf(user[0]) > Integer.valueOf(current[0]) && Integer.valueOf(user[0]) > 0 && Integer.valueOf(user[0]) < 32;

    }
    public String formatDate(String date) throws Exception {
        if(date == null)
            return null;

        String[] user = date.split("-|//.|/");
        String day = "";
        String month = "";
        String year = "";

        for (int i = 0; i < 3; i++) {
            if(Integer.valueOf(user[i]) > 1970){
                year = user[i];
            }

            //Handle month
            if(Integer.valueOf(user[i]) < 12 && Integer.valueOf(user[i]) > 0){

                //We know the year is populated, so the i+1 we will assume that is the day
                if(!year.equals("")){
                    if(Integer.valueOf(user[i+1]) > 12 && Integer.valueOf(user[i+1]) < 32 ){
                        day = user[i+1];
                        month = user[i];
                    }else if (Integer.valueOf(user[i+1]) < 12 && Integer.valueOf(user[i+1]) > 0){
                        if(Integer.valueOf(user[i+1]) < Integer.valueOf(user[i])) {
                            day = user[i];
                            month = user[i+1];
                        }else{
                            day = user[i+1];
                            month = user[i];
                        }
                    }
                }else{
                    if(month.equals("")){
                        month = user[i];
                    }else if(day.equals("")){
                        day = user[i];
                    }
                }
            }

            //Handle day
            if(Integer.valueOf(user[i]) > 0 && Integer.valueOf(user[i]) < 32 && day.equals("")){
                day = user[i];
            }
        }

        if(year.equals("") || month.equals("") || day.equals("")){
            throw new Exception("Invalid date format");
        }


        return year + "-" + month + "-" + day;
    }


}
