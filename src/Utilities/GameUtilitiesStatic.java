/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utilities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author davidellner
 */
public class GameUtilitiesStatic {
    
    public final static String RESULT_WIN = "W";
    public final static String RESULT_LOSS = "L";
    
    public static String getRandomGameResultId(String result){
        int count = 0;
        Random r;
        String gameresultid = "";
        try{
            String sql = "SELECT gameresultid FROM gameresultview WHERE result = '" + result + "';";
            ResultSet rs = DbUtilities.getResultSet(sql);
            rs.last();
            count = rs.getRow();
            rs.first();
            
            r = new Random();
            int dex = r.nextInt(count);
            
            for(int i = 0; i <= dex; i++){
                if(i == dex) {
                    gameresultid = rs.getString("gameresultid");
                    break;
                }
                else rs.next();
               
             
            }
            
        } catch(Exception e){e.printStackTrace(); }
        return gameresultid;
    }
    
    
    
}
