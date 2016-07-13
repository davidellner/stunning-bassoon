/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.sql.Date;

/**
 *
 * @author davidellner
 */
public class DateUtils {
    
    
    public static Date strToDate(String datestr) {
        //System.out.println("nuts");
        Date date = null;
        short month;
        String day;
        
        String[] dt = datestr.split(",");
        int offset = 3 - dt.length;
        String[] monthandday = dt[1 - offset].trim().split(" ");
        
        month = monthToDigit(monthandday[0]);
        day = monthandday[1];
        //yyyy-mm-dd
        String s = dt[2 - offset] + "-" + Short.toString(month) + "-" + day;
        
        date = Date.valueOf(s.trim());
        
        return date;
    }
    
    public static short monthToDigit(String month){
        short m = 0;
        switch(month){
            case "January": m = 1; break;
            case "February": m = 2; break;
            case "March": m = 3; break;
            case "April": m = 4; break;
            case "May": m = 5; break;
            case "June": m = 6; break;
            case "July": m = 7; break;
            case "August": m = 8; break;
            case "September": m = 9; break;
            case "October": m = 10; break;
            case "November": m = 11; break;
            case "December": m = 12; break;
            
        }
        return m;
    }
    
}
