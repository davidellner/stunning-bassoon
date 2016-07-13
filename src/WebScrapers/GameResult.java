/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package WebScrapers;

import Utilities.DbUtilities;
import Utilities.GameAverages;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author davidellner
 */
public class GameResult {
    
    private int goals;
    private int assists;
    private int points;
    private String result;
    private String homeoraway;
    private int gameid;
    private String team;
    private int penalties;
    private int penaltyminutes;
     //private int plusMinus;
    private String totalTimeOnTime;
    private int shifts;
    private String totAverageShiftLength;
    private String powerPlayTime;
    private String shortHandedTime;
    private String evenStrengthTime;
    private int shots;
    private int attemptsBlocked;
    private int missedShots;
    private int hits;
    private int giveAways;
    private int takeAways;
    private int blockedShots;
    private int faceoffsWon;
    private int faceoffsLost;
    private float faceoffPercent;
    private String opponent;
    double averagegoals;
    private int corsiFor;
    private double corsiForPercent;

    
    public GameResult(String gameResultId){
        String sql = "SELECT * FROM gameresultview WHERE gameresultid = '" + gameResultId + "';";
        try {
            ResultSet rs = DbUtilities.getResultSet(sql);
            if (rs.next()){
            this.goals = rs.getInt("goals");
            this.assists = rs.getInt("assists");
            this.points = rs.getInt("points");
            this.result = rs.getString("result");
            this.homeoraway = rs.getString("homeoraway");
            this.gameid = rs.getInt("gameid");
            this.team = rs.getString("team");
            this.penalties = rs.getInt("penalties");
            this.penaltyminutes = rs.getInt("pim");
            //this.plusMinus = rs.getInt("plusMinus");
            this.shifts = rs.getInt("shifts");
            this.totAverageShiftLength = rs.getString("totAvgShiftLength");
            this.powerPlayTime = rs.getString("powerPlayTime");
            this.shortHandedTime = rs.getString("shortHandedTime");
            this.evenStrengthTime = rs.getString("evenStrengthTime");
            this.shots = rs.getInt("shots");
            this.attemptsBlocked = rs.getInt("attemptsBlocked");
            this.missedShots = rs.getInt("missedShots");
            this.hits = rs.getInt("hits");
            this.giveAways = rs.getInt("giveAways");
            this.takeAways = rs.getInt("takeAways");
            this.blockedShots = rs.getInt("blockedShots");
            this.faceoffsWon = rs.getInt("faceoffsWon");
            this.faceoffsLost = rs.getInt("faceoffsLost");
            this.opponent = rs.getString("opponent");
            this.corsiFor = rs.getInt("corsifor");
            this.corsiForPercent = rs.getDouble("corsiforpercent");
            this.faceoffPercent = (this.faceoffsWon / (this.faceoffsWon + this.faceoffsLost));
            }
            
        } catch (Exception ex) {
            System.out.println("Error retrieving gameresultid: " + gameResultId);
            ex.printStackTrace();
        }
        
    }
    /**
     * These methods return different variances
     */
    public double getGoalVariance(){
      return Math.pow((GameAverages.getAvggoals() - this.goals),2);
    }
    public double getAssistVariance(){
      return Math.pow((GameAverages.getAvgassists() - this.assists),2);
    }
    public double getPointsVariance(){
      return Math.pow((GameAverages.getAvgpoints() - this.points),2);
    }
    public double getShotsVariance(){
      return Math.pow((GameAverages.getAvgshots() - this.shots),2);
    }
    public double getHitsVariance(){
      return Math.pow((GameAverages.getAvghits() - this.hits),2);
    }
    public double getCorsiForVariance(){
      return Math.pow((GameAverages.getAvgcorsifor() - this.corsiFor),2);
    }
    public double getCorsiForPercentVariance(){
      return Math.pow((GameAverages.getAvgcorsiforpercent() - this.corsiForPercent),2);
    }
    public double getFaceoffPercentVariance(){
      return Math.pow((GameAverages.getAvgshots() - this.faceoffPercent),2);
    }
    public double getTakeAwaysVariance(){
      return Math.pow((GameAverages.getAvgtakeaways() - this.takeAways),2);
    }
    public double getGiveAwaysVariance(){
      return Math.pow((GameAverages.getAvgshots() - this.giveAways),2);
    }
    public double getPIMVariance(){
      return Math.pow((GameAverages.getAvgpim() - this.penaltyminutes),2);
    }
    public int getGoals() {
        return goals;
    }

    /**
     * @return the assists
     */
    public int getAssists() {
        return assists;
    }

    /**
     * @return the points
     */
    public int getPoints() {
        return points;
    }

    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }

    /**
     * @return the homeoraway
     */
    public String getHomeoraway() {
        return homeoraway;
    }

    /**
     * @return the gameid
     */
    public int getGameid() {
        return gameid;
    }

    /**
     * @return the team
     */
    public String getTeam() {
        return team;
    }

    /**
     * @return the penalties
     */
    public int getPenalties() {
        return penalties;
    }

    /**
     * @return the penaltyminutes
     */
    public int getPenaltyminutes() {
        return penaltyminutes;
    }

    /**
     * @return the plusMinus
     */
    

    /**
     * @return the totalTimeOnTime
     */
    public String getTotalTimeOnTime() {
        return totalTimeOnTime;
    }

    /**
     * @return the shifts
     */
    public int getShifts() {
        return shifts;
    }

    /**
     * @return the totAverageShiftLength
     */
    public String getTotAverageShiftLength() {
        return totAverageShiftLength;
    }

    /**
     * @return the powerPlayTime
     */
    public String getPowerPlayTime() {
        return powerPlayTime;
    }

    /**
     * @return the shortHandedTime
     */
    public String getShortHandedTime() {
        return shortHandedTime;
    }

    /**
     * @return the evenStrengthTime
     */
    public String getEvenStrengthTime() {
        return evenStrengthTime;
    }

    /**
     * @return the shots
     */
    public int getShots() {
        return shots;
    }

    /**
     * @return the attemptsBlocked
     */
    public int getAttemptsBlocked() {
        return attemptsBlocked;
    }

    /**
     * @return the missedShots
     */
    public int getMissedShots() {
        return missedShots;
    }

    /**
     * @return the hits
     */
    public int getHits() {
        return hits;
    }

    /**
     * @return the giveAways
     */
    public int getGiveAways() {
        return giveAways;
    }

    /**
     * @return the takeAways
     */
    public int getTakeAways() {
        return takeAways;
    }

    /**
     * @return the blockedShots
     */
    public int getBlockedShots() {
        return blockedShots;
    }

    /**
     * @return the faceoffsWon
     */
    public int getFaceoffsWon() {
        return faceoffsWon;
    }

    /**
     * @return the faceoffsLost
     */
    public int getFaceoffsLost() {
        return faceoffsLost;
    }

    /**
     * @return the faceoffPercent
     */
    public float getFaceoffPercent() {
        return faceoffPercent;
    }

    /**
     * @return the opponent
     */
    public String getOpponent() {
        return opponent;
    }

    
    
    
}
