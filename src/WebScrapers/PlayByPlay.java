/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package WebScrapers;

import Utilities.DbUtilities;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author davidellner
 */
public class PlayByPlay {
    int playbyplayid;
    int gameid;
    int playnumber;
    int period;
    String timeelapsed;
    String eventtype;
    String description;
    String strength;
    int actingplayernumber;
    String actingplayername;
    String actingteam;
    int againstplayernumber;
    String againstplayername;
    String againstteam;
    String zone;
    int distance;
    String shottype;
    int actingsituation;
    String possesion;
    
    
    public PlayByPlay(int gameid, int playnumber, int period, 
            String strength, String timeelapsed, 
            String eventtype, String description,
            int actingplayernumber,
            String actingplayername,
            String actingteam,
            int againstplayernumber,
            String againstplayername,
            String againstteam,
            String zone,
            int distance,
            String shottype,
            int actingsituation,
            String possesion){ 
        this.gameid = gameid;  
        this.playnumber = playnumber;  
        this.period = period;  
        this.timeelapsed = timeelapsed;  
        this.eventtype = eventtype;  
        this.description = description; 
        this.strength = strength;
        this.actingplayernumber = actingplayernumber;
        this.actingplayername = actingplayername;
        this.actingteam = actingteam;
        this.againstplayernumber = againstplayernumber;
        this.againstplayername = againstplayername;
        this.againstteam = againstteam;
        this.zone = zone;
        this.distance = distance;
        this.shottype = shottype;
        this.actingsituation = actingsituation;
        this.possesion = possesion;
    }
    
    public int writeToDb(){
        String sql = "INSERT INTO `nhl`.`playbyplay`\n" +
"(" +
"`gameid`,\n" +
"`playnumber`,\n" +
"`period`,\n" +
"`timeelapsed`,\n" +
"`eventtype`,\n" +
"`description`, `strength`, actingplayernumber, actingplayername, "
                + "actingteam, againstplayernumber, againstplayername, "
                + "againstteam, zone, distance, shottype, actingsituation, possesion"
                + ") VALUES (" + this.gameid + "," + this.playnumber + "," + this.period + ",'" 
                + this.timeelapsed + "','" + this.eventtype + "','" + this.description.replaceAll("'", "''") 
                + "','" +this.strength + "'," + this.actingplayernumber + ",'" + this.actingplayername.replaceAll("'", "''") 
                + "','" + this.actingteam + "'," + this.againstplayernumber + ",'" + this.againstplayername.replaceAll("'", "''") 
                + "','" + this.againstteam + "','" + this.zone + "'," + this.distance + ",'" + this.shottype 
                + "', "+ this.actingsituation + ",'" + this.possesion + "'" +");";
        
        
        int playbyplayid = -1;
        try {
           //System.out.println(sql);
            DbUtilities.executeQuery(sql);
            ResultSet r = DbUtilities.getResultSet("SELECT last_insert_id() AS id;");
            r.next();
            playbyplayid = r.getInt("id");
        } catch (Exception ex) {
            System.out.println(sql);
            ex.printStackTrace();
            System.out.println("Game ID: " + this.gameid + " PlayNumber: "+ this.playnumber);
        }
        return playbyplayid;
    }
}
