/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utilities;

import java.sql.ResultSet;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Utility class to resolve data quality issues while scraping
 * @author davidellner
 */
public class MrFixerUpper {
    
   
    public static void main(String[] args){
        
    }
    
    public static void orderGoalRelatedPlays(){
    
        
        
    }
    
    public static void setGameDates(){
        
    }
    public static void fixPlayerNames(){
        String sql = "SELECT playbyplayid, actingplayername, actingplayernumber, gameid FROM nhl.playbyplay WHERE actingplayername LIKE '%('";
        try{
            ResultSet rs = DbUtilities.getResultSet(sql);
            String actingplayername = "";
            int gameid =0;
            int actingnumber = 0;
            String findPlayer;
            ResultSet targetplayer;
            int targetplayerid= -100;
            int playbyplayid;
            int count = 0;
            while(rs.next()){
                count++;
                actingplayername = rs.getString("actingplayername");
                actingplayername = actingplayername.substring(0, actingplayername.length()-1);
                actingnumber = rs.getInt("actingplayernumber");
                gameid = rs.getInt("gameid");
                playbyplayid = rs.getInt("playbyplayid");
                
                findPlayer = "SELECT playerid from playereventdata e "
                           + "JOIN nhl.team t ON e.team = t.teamname WHERE lastname = '" + actingplayername.replaceAll("'", "''") + 
                           "' AND jerseynumber = " + actingnumber + " AND gameid = " + gameid;
                   
                   targetplayer = DbUtilities.getResultSet(findPlayer);
                   if(targetplayer.next()) targetplayerid = targetplayer.getInt("playerid");
                   
                   String update = "UPDATE nhl.playeronice SET isacting = 1 WHERE playbyplayid = " 
                           + playbyplayid + " AND playerid = " + targetplayerid;
                //   System.out.println(update);     
                   DbUtilities.executeQuery(update);
                   
                   update = "UPDATE nhl.playbyplay SET actingplayername = '" + actingplayername.replaceAll("'", "''")
                           + "' WHERE playbyplayid = " 
                           + playbyplayid;
                   
                   DbUtilities.executeQuery(update);
                System.out.printf("Working...Player Name: %s; ID: %s; iteration # %s", actingplayername, targetplayerid, count);
                System.out.println();
            }
            
        }catch(Exception e){
            
        e.printStackTrace();
        return;
        }
    
    }
    
    public static void dep(String[] args){
        int targetgameid;
        
        try{
           String sql = "SELECT gameid, playbyplayid, actingteam, description FROM nhl.playbyplay WHERE eventtype = 'FAC';";
           ResultSet rs = DbUtilities.getResultSet(sql);
           
           String actingTeam;
           String againstTeam;
           String actingPlayerName;
           String againstPlayerName;
           int vsIndex;
           int dashIndex;
           String description;
           String teama;
           String teamb;
           
           int actingNumber;
           int againstNumber;
           int targetplayerid;
           int gameid;
           int playbyplayid;
           
           ResultSet targetPlayer;
        
           String findPlayer;
           String update;
           
           while(rs.next()){
               
               description = rs.getString("description");
               gameid = rs.getInt("gameid");
               vsIndex = description.indexOf("vs");
               dashIndex = description.indexOf("-");
               actingTeam = description.substring(0, 3);
               playbyplayid = rs.getInt("playbyplayid");
               
               teama = description.substring(vsIndex + 2, vsIndex + 6).trim();
               teamb = description.substring(dashIndex + 2, dashIndex + 5).trim();
               
               //find acting player name & #
               if(teama.equals(actingTeam)){
                   actingPlayerName = description.substring(dashIndex + 9, vsIndex).trim();
                   actingNumber = NumberUtils.toInt(description.substring(dashIndex + 7, dashIndex + 9).trim());
                   
                   againstPlayerName = description.substring(vsIndex + 10).trim();
                   againstNumber = NumberUtils.toInt(description.substring(vsIndex + 8, vsIndex + 10).trim());
                   //find target player id
                   findPlayer = "SELECT playerid from playereventdata e "
                           + "JOIN nhl.team t ON e.team = t.teamname WHERE lastname = '" + actingPlayerName + 
                           "' AND jerseynumber = " + actingNumber + " AND gameid = " + gameid;
                   
                   targetPlayer = DbUtilities.getResultSet(findPlayer);
                   if(targetPlayer.next()) targetplayerid = targetPlayer.getInt("playerid");
                   
                   update = "UPDATE nhl.playbyplay "
                           + "SET againstteam = '" + teamb + "',"
                           + "actingplayername = '" + actingPlayerName.replaceAll("'", "''") + "',"
                           + "againstplayername = '" + againstPlayerName.replaceAll("'", "''") + "' ,"
                           + "actingplayernumber = " + actingNumber + ","
                           + "againstplayernumber = " + againstNumber
                           + " WHERE playbyplayid = " + playbyplayid;
                   
                   DbUtilities.executeQuery(update);
                   
               }
               else if(teamb.equals(actingTeam)){
                   actingPlayerName = description.substring(vsIndex + 10).trim();
                   actingNumber = NumberUtils.toInt(description.substring(vsIndex + 8, vsIndex + 10).trim());
                   
                   againstPlayerName = description.substring(dashIndex + 9, vsIndex).trim();
                   againstNumber = NumberUtils.toInt(description.substring(dashIndex + 7, dashIndex + 9).trim());
                   
                   findPlayer = "SELECT playerid from playereventdata e "
                           + "JOIN nhl.team t ON e.team = t.teamname WHERE lastname = '" + actingPlayerName + 
                           "' AND jerseynumber = " + actingNumber + " AND gameid = " + gameid;
                   
                   targetPlayer = DbUtilities.getResultSet(findPlayer);
                   if(targetPlayer.next()) targetplayerid = targetPlayer.getInt("playerid");
                   
                   update = "UPDATE nhl.playbyplay "
                           + "SET againstteam = '" + teama + "',"
                           + "actingplayername = '" + actingPlayerName.replaceAll("'", "''") + "',"
                           + "againstplayername = '" + againstPlayerName.replaceAll("'", "''") + "' ,"
                           + "actingplayernumber = " + actingNumber + ","
                           + "againstplayernumber = " + againstNumber
                           + " WHERE playbyplayid = " + playbyplayid;
                   
                   DbUtilities.executeQuery(update);
               }
               break;
           }
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }
    
}
