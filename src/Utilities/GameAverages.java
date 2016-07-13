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
public class GameAverages {
    private static double avggoals;
    private static double avgassists;
    private static double avgpoints;
    private static double avgpenalties;
    private static double avgpim;
    private static double avgshifts;
    private static double avgshots;
    private static double avgattemptsblocked;
    private static double avgmissedshots;
    private static double avghits;
    private static double avggiveaways;
    private static double avgtakeaways;
    private static double avgblockedshots;
    private static double avgfaceoffswon;
    private static double avgfaceoffslost;
    private static double avgfaceoffpercent;
    private static double avgcorsifor;
    private static double avgcorsiagainst;
    private static double avgcorsiforpercent;
    private static double avgcorsiagainstpercent;
    private static double goalratio;
    
    static{
        String sql = "SELECT * FROM nhl.winaverageview";
        try {
            ResultSet rs = DbUtilities.getResultSet(sql);
            if(rs.next()){
                avggoals = rs.getDouble("avggoals");
                avgassists = rs.getDouble("avgassists");
                avgpoints = rs.getDouble("avgpoints");
                avgpenalties = rs.getDouble("avgpenalties");
                avgpim = rs.getDouble("avgpim");
                avgshifts = rs.getDouble("avgshifts");
                avgshots = rs.getDouble("avgshots");
                avgattemptsblocked = rs.getDouble("avgattemptsblocked");
                avgmissedshots = rs.getDouble("avgmissedshots");
                avghits = rs.getDouble("avghits");
                avggiveaways = rs.getDouble("avggiveaways");
                avgtakeaways = rs.getDouble("avgtakeaways");
                avgblockedshots = rs.getDouble("avgblockedshots");
                avgfaceoffswon = rs.getDouble("avgfaceoffswon");
                avgfaceoffslost = rs.getDouble("avgfaceoffslost");
                avgcorsifor = rs.getDouble("avgcorsifor");
                avgcorsiagainst = rs.getDouble("avgcorsiagainst");
                avgcorsiforpercent = rs.getDouble("avgcorsiforpercent");
                avgcorsiagainstpercent = rs.getDouble("avgcorsiagainstpercent");
                goalratio = rs.getDouble("goalratio");
                avgfaceoffpercent = 100 * (avgfaceoffswon / (avgfaceoffswon + avgfaceoffslost));
            }
            System.out.println("Static block in game averages");
        } catch (SQLException ex) {
            System.out.println("Error in Game Averages.");
            ex.printStackTrace();
        }
    }
    public static double getAvgfaceoffpercent() {
        return avgfaceoffpercent;
    }
    /**
     * @return the avggoals
     */
    public static double getAvggoals() {
        return avggoals;
    }

    /**
     * @return the avgassists
     */
    public static double getAvgassists() {
        return avgassists;
    }

    /**
     * @return the avgpoints
     */
    public static double getAvgpoints() {
        return avgpoints;
    }

    /**
     * @return the avgpenalties
     */
    public static double getAvgpenalties() {
        return avgpenalties;
    }

    /**
     * @return the avgpim
     */
    public static double getAvgpim() {
        return avgpim;
    }

    /**
     * @return the avgshifts
     */
    public static double getAvgshifts() {
        return avgshifts;
    }

    /**
     * @return the avgshots
     */
    public static double getAvgshots() {
        return avgshots;
    }

    /**
     * @return the avgattemptsblocked
     */
    public static double getAvgattemptsblocked() {
        return avgattemptsblocked;
    }

    /**
     * @return the avgmissedshots
     */
    public static double getAvgmissedshots() {
        return avgmissedshots;
    }

    /**
     * @return the avghits
     */
    public static double getAvghits() {
        return avghits;
    }

    /**
     * @return the avggiveaways
     */
    public static double getAvggiveaways() {
        return avggiveaways;
    }

    /**
     * @return the avgtakeaways
     */
    public static double getAvgtakeaways() {
        return avgtakeaways;
    }

    /**
     * @return the avgblockedshots
     */
    public static double getAvgblockedshots() {
        return avgblockedshots;
    }

    /**
     * @return the avgfaceoffswon
     */
    public static double getAvgfaceoffswon() {
        return avgfaceoffswon;
    }

    /**
     * @return the avgfaceoffslost
     */
    public static double getAvgfaceoffslost() {
        return avgfaceoffslost;
    }

    /**
     * @return the avgcorsifor
     */
    public static double getAvgcorsifor() {
        return avgcorsifor;
    }

    /**
     * @return the avgcorsiagainst
     */
    public static double getAvgcorsiagainst() {
        return avgcorsiagainst;
    }

    /**
     * @return the avgcorsiforpercent
     */
    public static double getAvgcorsiforpercent() {
        return avgcorsiforpercent;
    }

    /**
     * @return the avgcorsiagainstpercent
     */
    public static double getAvgcorsiagainstpercent() {
        return avgcorsiagainstpercent;
    }

    /**
     * @return the goalratio
     */
    public static double getGoalratio() {
        return goalratio;
    }
    
}
