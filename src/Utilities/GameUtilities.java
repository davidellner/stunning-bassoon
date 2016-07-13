/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utilities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author davidellner
 */
public class GameUtilities {
ResultSet r;  
String team;
    public GameUtilities(String teamName){
        this.team = teamName;
        String sql = "SELECT * FROM nhl.gameresultview WHERE team = '" + teamName + "' ORDER BY gameid ASC;";
        try {
            r = DbUtilities.getResultSet(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void printWeightedAverages(){
        System.out.println(team);
        System.out.println("Goals: " + this.getWeightedGoals());
        System.out.println("Goals Against: " + this.getWeightedAverageByColumn("goalsagainst"));
        System.out.println("Assists: " + this.getWeightedAverageByColumn("assists"));
        System.out.println("Hits: " + this.getWeightedAverageByColumn("hits"));
        System.out.println("PIM: " + this.getWeightedAverageByColumn("pim"));
        System.out.println("Shifts: " + this.getWeightedAverageByColumn("shifts"));
        System.out.println("Shots: " + this.getWeightedAverageByColumn("shots"));
        System.out.println("Shots against: " + this.getWeightedAverageByColumn("shotsagainst"));
        System.out.println("Attempts Blocked: " + this.getWeightedAverageByColumn("attemptsblocked"));
        System.out.println("Missed Shots: " + this.getWeightedAverageByColumn("missedshots"));
        System.out.println("Giveaways: " + this.getWeightedAverageByColumn("giveaways"));
        System.out.println("Takeaways " + this.getWeightedAverageByColumn("takeaways"));
        System.out.println("Blocked Shots: " + this.getWeightedAverageByColumn("blockedshots"));
        System.out.println("Faceoffs Won: " + this.getWeightedAverageByColumn("faceoffswon"));
        System.out.println("Faceoffs Lost: " + this.getWeightedAverageByColumn("faceoffslost"));
    }
    public double getWeightedGoals(){
        double divisor = 0;
        int iterator = 0;
        double goalstemp = 0;
    try {
        while(r.next()){
            int goals = r.getInt("goals");
            //System.out.println("Goals:" + goals);
            double multiplier = getLog2(iterator + 1);
            divisor += multiplier;
            goalstemp += goals * multiplier;
            iterator++;
        }
        boolean b = r.first();
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    
    return goalstemp / divisor;
    }
    public static double getLog2(double x){
        return Math.log(x) / Math.log(2);
    } 
    public double getWeightedAverageByColumn(String columnName){
        double divisor = 0;
        int iterator = 0;
        double resulttemp = 0;
    try {
        while(r.next()){
            int goals = r.getInt(columnName);
            
            double multiplier = getLog2(iterator + 1);
          //  System.out.println(columnName + " " + multiplier + " - " + goals);
            divisor += multiplier;
            resulttemp += goals * multiplier;
            iterator++;
        }
        boolean b = r.first();
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return resulttemp / divisor;
    }
    
    
}
