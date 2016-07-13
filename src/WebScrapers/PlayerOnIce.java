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
public class PlayerOnIce {
    String position;
    String firstName;
    String lastName;
    int number;
    int gameId;
    int playbyplayid;
    String team;
    boolean isActing;
    boolean isOnActingTeam;

    public PlayerOnIce(String position, String firstName, String lastName, int number, int gameId, int playbyplayid, String team, boolean isActing, boolean isOnActingTeam) {
        this.position = position;
        this.firstName = firstName;
        this.lastName = lastName;
        this.number = number;
        this.gameId = gameId;
        this.playbyplayid = playbyplayid;
        this.team = team;
        this.isActing = isActing;
        this.isOnActingTeam = isOnActingTeam;
    }
    
    public static void writeToDbStatic(String position, String firstName, String lastName, int number, int gameId, int playbyplayid, String team, boolean isActing, boolean isOnActingTeam){
        int playerid;
        String sql;
    
        try {
            //System.out.println("SELECT playerid FROM nhl.player WHERE firstname ='" + firstName + "' AND lastname = '"+lastName+"';");
            ResultSet rs = DbUtilities.getResultSet("SELECT playerid FROM nhl.player WHERE firstname ='" + firstName.replaceAll("'", "''") + "' AND lastname = '" + lastName.replaceAll("'", "''")+"';");
            while(rs.next()){
              playerid = rs.getInt("playerid");
              if(playerid > 0){
                 sql = "INSERT INTO `nhl`.`playeronice`"
                        +"("
                        + "`playerid`,"
                        + "`gameid`,"
                        + "`playbyplayid`,"
                        + "`team`,"
                        + "`isacting`,"
                        + "`isonactingteam`)"
                        + "VALUES ("
                        + playerid + ","
                        + gameId + ","
                        + playbyplayid + ",'"
                        + team + "',"
                        + isActing + ","
                        + isOnActingTeam + ");";
              }
              else {
                  sql = "INSERT INTO `nhl`.`playeroniceunknown`"
                        +"("
                        + "`playerid`,"
                        + "`gameid`,"
                        + "`playbyplayid`,"
                        + "`team`,"
                        + "`isacting`,"
                        + "`isonactingteam`"
                        + "firstname, lastname)"
                        + "VALUES ("
                        + playerid + ","
                        + gameId + ","
                        + playbyplayid + ",'"
                        + team + "',"
                        + isActing + ","
                        + isOnActingTeam + ",'"
                        + firstName + "','"
                         + lastName + "');";
              }
              
              DbUtilities.executeQuery(sql);
              break;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }
}
