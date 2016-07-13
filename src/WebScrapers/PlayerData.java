/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package WebScrapers;

import Utilities.DbUtilities;


/**
 *
 * @author davidellner
 */
public class PlayerData {
    //this class pretty much exclusively writes to the db
    private int jerseyNumber;
    private String position;
    private String lastName;
    private String firstName;
    private int goals;
    private int assists;
    private int points;
    private int plusMinus;
    private int penalties;
    private int penaltyminutes;
    private String totalTimeOnTime;
    private int shifts;
    private String averageShiftLength;
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
    private String team;
     int gameId;
    
    public PlayerData(
    int jerseyNumber,
        String position,
        String lastName,
        String firstName,
        int goals,
        int assists,
        int points,
        int plusMinus,
        int penalties,
        int penaltyminutes,
        String totalTimeOnTime,
        int shifts,
        String averageShiftLength,
        String powerPlayTime,
        String shortHandedTime,
        String evenStrengthTime,
        int shots,
        int attemptsBlocked,
        int missedShots,
        int hits,
        int giveAways,
        int takeAways,
        int blockedShots,
        int faceoffsWon,
        int faceoffsLost,
        int faceoffPercent,
        String team,
        int gameId)
    {
        this.jerseyNumber = jerseyNumber;
        this.position = position;
        this.lastName = lastName;
        this.firstName = firstName;
        this.goals = goals;
        this.assists = assists;
        this.points = points;
        this.plusMinus = plusMinus;
        this.penalties = penalties;
        this.penaltyminutes = penaltyminutes;
        this.totalTimeOnTime = totalTimeOnTime;
        this.shifts = shifts;
        this.averageShiftLength = averageShiftLength;
        this.powerPlayTime = powerPlayTime;
        this.shortHandedTime = shortHandedTime;
        this.evenStrengthTime = evenStrengthTime;
        this.shots = shots;
        this.attemptsBlocked = attemptsBlocked;
        this.missedShots = missedShots;
        this.hits = hits;
        this.giveAways = giveAways;
        this.takeAways = takeAways;
        this.blockedShots = blockedShots;
        this.faceoffsWon = faceoffsWon;
        this.faceoffsLost = faceoffsLost;
        this.faceoffPercent = faceoffPercent;
        this.team = team;
        this.gameId = gameId;
    }
    
    public void writeToDB() {
        String sql = "INSERT INTO `nhl`.`playereventdata`\n" +
                                            "(`gameid`,\n" +
                                            "`jerseynumber`,\n" +
                                            "`position`,\n" +
                                            "`lastname`,\n" +
                                            "`firstname`,\n" +
                                            "`goals`,\n" +
                                            "`assists`,\n" +
                                            "`points`,\n" +
                                            "`plusminus`,\n" +
                                            "`penalties`,\n" +
                                            "`penaltyminutes`,\n" +
                                            "`totaltimeonice`,\n" +
                                            "`shifts`,\n" +
                                            "`averageshiftlength`,\n" +
                                            "`powerplaytime`,\n" +
                                            "`shorthandedtime`,\n" +
                                            "`evenstrengthtime`,\n" +
                                            "`shots`,\n" +
                                            "`attemptsblocked`,\n" +
                                            "`missedshots`,\n" +
                                            "`hits`,\n" +
                                            "`giveaways`,\n" +
                                            "`takeaways`,\n" +
                                            "`blockedshots`,\n" +
                                            "`faceoffswon`,\n" +
                                            "`faceoffslost`,\n" +
                                            "`faceoffpercent`,\n" +
                                            "`team`)\n" +
                                            "VALUES (" +
                this.gameId + "," +
                "'" + this.getJerseyNumber() + "'," + 
                "'" + this.getPosition() + "'," +
                "'" + this.getLastName() + "'," +
                "'" + this.getFirstName() + "'," +
                this.getGoals() + "," +
                this.getAssists() + "," +
                this.getPoints() + "," +
                this.getPlusMinus() + "," +
                this.getPenalties() + "," +
                this.getPenaltyminutes() + "," +
                "'" + this.getTotalTimeOnTime() + "'," +
                this.getShifts() + "," +
                "'" + this.getAverageShiftLength() + "'," +
                "'" + this.getPowerPlayTime() + "'," +
                "'" + this.getShortHandedTime() + "'," +
                "'" + this.getEvenStrengthTime() + "'," +
                this.getShots() + "," + 
                this.getAttemptsBlocked() + "," +
                this.getMissedShots()  + "," +
                this.getHits()  + "," +
                this.getGiveAways()  + "," +
                this.getTakeAways()  + "," +
                this.getBlockedShots()  + "," +
                this.getFaceoffsWon()  + "," +
                this.getFaceoffsLost()  + "," +
                this.getFaceoffPercent()  + "," +
                "'" + this.team + "')";
                
                try {
                 DbUtilities.executeQuery(sql);
                } 
                catch(Exception e){
                    e.printStackTrace();
                }
                
                
    }
    public String toString() {
        return this.jerseyNumber + " " + this.lastName + ", " + this.firstName + " - " 
                + this.team;
        
    }
    /**
     * @return the jerseyNumber
     */
    public int getJerseyNumber() {
        return jerseyNumber;
    }

    /**
     * @param jerseyNumber the jerseyNumber to set
     */
    public void setJerseyNumber(int jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }
    public String getTeam(){
        return this.team;
    }
    /**
     * @return the position
     */
    public String getPosition() {
        return position;
    }

    /**
     * @param postion the position to set
     */
    public void setPosition(String postion) {
        this.position = postion;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the goals
     */
    public int getGoals() {
        return goals;
    }

    /**
     * @param goals the goals to set
     */
    public void setGoals(int goals) {
        this.goals = goals;
    }

    /**
     * @return the assists
     */
    public int getAssists() {
        return assists;
    }

    /**
     * @param assists the assists to set
     */
    public void setAssists(int assists) {
        this.assists = assists;
    }

    /**
     * @return the points
     */
    public int getPoints() {
        return points;
    }

    /**
     * @param points the points to set
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * @return the plusMinus
     */
    public int getPlusMinus() {
        return plusMinus;
    }

    /**
     * @param plusMinus the plusMinus to set
     */
    public void setPlusMinus(int plusMinus) {
        this.plusMinus = plusMinus;
    }

    /**
     * @return the penalties
     */
    public int getPenalties() {
        return penalties;
    }

    /**
     * @param penalties the penalties to set
     */
    public void setPenalties(int penalties) {
        this.penalties = penalties;
    }

    /**
     * @return the penaltyminutes
     */
    public int getPenaltyminutes() {
        return penaltyminutes;
    }

    /**
     * @param penaltyminutes the penaltyminutes to set
     */
    public void setPenaltyminutes(int penaltyminutes) {
        this.penaltyminutes = penaltyminutes;
    }

    /**
     * @return the totalTimeOnTime
     */
    public String getTotalTimeOnTime() {
        return totalTimeOnTime;
    }

    /**
     * @param totalTimeOnTime the totalTimeOnTime to set
     */
    public void setTotalTimeOnTime(String totalTimeOnTime) {
        this.totalTimeOnTime = totalTimeOnTime;
    }

    /**
     * @return the shifts
     */
    public int getShifts() {
        return shifts;
    }

    /**
     * @param shifts the shifts to set
     */
    public void setShifts(int shifts) {
        this.shifts = shifts;
    }

    /**
     * @return the averageShiftLength
     */
    public String getAverageShiftLength() {
        return averageShiftLength;
    }

    /**
     * @param averageShiftLength the averageShiftLength to set
     */
    public void setAverageShiftLength(String averageShiftLength) {
        this.averageShiftLength = averageShiftLength;
    }

    /**
     * @return the powerPlayTime
     */
    public String getPowerPlayTime() {
        return powerPlayTime;
    }

    /**
     * @param powerPlayTime the powerPlayTime to set
     */
    public void setPowerPlayTime(String powerPlayTime) {
        this.powerPlayTime = powerPlayTime;
    }

    /**
     * @return the shortHandedTime
     */
    public String getShortHandedTime() {
        return shortHandedTime;
    }

    /**
     * @param shortHandedTime the shortHandedTime to set
     */
    public void setShortHandedTime(String shortHandedTime) {
        this.shortHandedTime = shortHandedTime;
    }

    /**
     * @return the evenStrengthTime
     */
    public String getEvenStrengthTime() {
        return evenStrengthTime;
    }

    /**
     * @param evenStrengthTime the evenStrengthTime to set
     */
    public void setEvenStrengthTime(String evenStrengthTime) {
        this.evenStrengthTime = evenStrengthTime;
    }

    /**
     * @return the shots
     */
    public int getShots() {
        return shots;
    }

    /**
     * @param shots the shots to set
     */
    public void setShots(int shots) {
        this.shots = shots;
    }

    /**
     * @return the attemptsBlocked
     */
    public int getAttemptsBlocked() {
        return attemptsBlocked;
    }

    /**
     * @param attemptsBlocked the attemptsBlocked to set
     */
    public void setAttemptsBlocked(int attemptsBlocked) {
        this.attemptsBlocked = attemptsBlocked;
    }

    /**
     * @return the missedShots
     */
    public int getMissedShots() {
        return missedShots;
    }

    /**
     * @param missedShots the missedShots to set
     */
    public void setMissedShots(int missedShots) {
        this.missedShots = missedShots;
    }

    /**
     * @return the hits
     */
    public int getHits() {
        return hits;
    }

    /**
     * @param hits the hits to set
     */
    public void setHits(int hits) {
        this.hits = hits;
    }

    /**
     * @return the giveAways
     */
    public int getGiveAways() {
        return giveAways;
    }

    /**
     * @param giveAways the giveAways to set
     */
    public void setGiveAways(int giveAways) {
        this.giveAways = giveAways;
    }

    /**
     * @return the takeAways
     */
    public int getTakeAways() {
        return takeAways;
    }

    /**
     * @param takeAways the takeAways to set
     */
    public void setTakeAways(int takeAways) {
        this.takeAways = takeAways;
    }

    /**
     * @return the blockedShots
     */
    public int getBlockedShots() {
        return blockedShots;
    }

    /**
     * @param blockedShots the blockedShots to set
     */
    public void setBlockedShots(int blockedShots) {
        this.blockedShots = blockedShots;
    }

    /**
     * @return the faceoffsWon
     */
    public int getFaceoffsWon() {
        return faceoffsWon;
    }

    /**
     * @param faceoffsWon the faceoffsWon to set
     */
    public void setFaceoffsWon(int faceoffsWon) {
        this.faceoffsWon = faceoffsWon;
    }

    /**
     * @return the faceoffsLost
     */
    public int getFaceoffsLost() {
        return faceoffsLost;
    }

    /**
     * @param faceoffsLost the faceoffsLost to set
     */
    public void setFaceoffsLost(int faceoffsLost) {
        this.faceoffsLost = faceoffsLost;
    }

    /**
     * @return the faceoffPercent
     */
    public float getFaceoffPercent() {
        return faceoffPercent;
    }

    /**
     * @param faceoffPercent the faceoffPercent to set
     */
    public void setFaceoffPercent(int faceoffPercent) {
        this.faceoffPercent = faceoffPercent;
    }
    
    
    
    
    
    
}
