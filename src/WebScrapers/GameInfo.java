/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebScrapers;

import Utilities.DbUtilities;
import java.sql.Date;

/**
 *
 * @author davidellner
 */
public class GameInfo {
    
    private int gameId;
    private Date date;
    private int attendance;
    private int gameNumber;

    public void writeToDb(){
        String sql = "INSERT INTO nhl.gameinfo(gameid, gamedate, attendance, gamenumber) VALUES (" 
                + this.getGameId() + ", "
                + "'" + this.getDate() + "', "
                + this.getAttendance() + ", "
                + this.getGameNumber() + ");";
        try {
            DbUtilities.executeQuery(sql);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
   
    public GameInfo(int gameId, Date date, int attendance, int gameNumber) {
        this.gameId = gameId;
        this.date = date;
        this.attendance = attendance;
        this.gameNumber = gameNumber;
    }
    /**
     * @return the gameId
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * @param gameId the gameId to set
     */
    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the attendance
     */
    public int getAttendance() {
        return attendance;
    }

    /**
     * @param attendance the attendance to set
     */
    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    /**
     * @return the gameNumber
     */
    public int getGameNumber() {
        return gameNumber;
    }

    /**
     * @param gameNumber the gameNumber to set
     */
    public void setGameNumber(int gameNumber) {
        this.gameNumber = gameNumber;
    }
    
}
