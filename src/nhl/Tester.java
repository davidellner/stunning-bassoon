/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nhl;

import Utilities.DbUtilities;
import Utilities.GameAverages;
import Utilities.GameUtilities;
import Utilities.GameUtilitiesStatic;
import WebScrapers.GameInfo;
import WebScrapers.GameResult;
import WebScrapers.PlayByPlay;
import WebScrapers.PlayByPlayConnector;
import WebScrapers.PrimaryConnector;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author davidellner
 */
public class Tester {
    static int gameid = 21079; //20708;
    static int lastgameid = 21230; //20888;
    static int die = 0;
    
    
    public static void main(String[] args){
        int gameId = 20675;
        int lastGameId = 21230;
        String gameIdStr;
        Connection c;
        Document doc;
        GameInfo g;
        //Code to fetch from raw file (practice where there is no internet connection)
        //File file = new File("./NHL/testevent2.html");
        //doc = Jsoup.parse(file, "UTF-8");
        
        while(gameId <= lastGameId){
            gameIdStr = "0" + gameId;
            String URL = PrimaryConnector.BASE_URL + PrimaryConnector.START_YEAR + PrimaryConnector.END_YEAR + "/" + PrimaryConnector.EVENT_SUMMARY_TAG + gameIdStr + ".HTM";   

            try {
                c = Jsoup.connect(URL);
                doc = c.timeout(0).get();
                g = PrimaryConnector.getGameInfo(doc, gameId);
                g.writeToDb();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            gameId++;
        }
        
    }
    public static void fetchPlayByPlay() throws SQLException{
        PlayByPlayConnector p;
        long time;
        while(gameid < lastgameid && die < 3){
            gameid++;
            p = new PlayByPlayConnector(gameid);
            time = playByPlay(p);
            
            if(time > 60){
                DbUtilities.close();
                System.gc();
                die++;
                
            }
       }
        System.out.println("Done. Last game: " + gameid);
    }
    public static long playByPlay(PlayByPlayConnector p) throws SQLException{
        
        long starttime; 
        
        
            
            //gameid++;
            starttime = System.currentTimeMillis();
          //  p = new PlayByPlayConnector(gameid);
            try {
                p.fetchPlayByPlay();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
            System.out.println("Working...Game ID: " + gameid + " - Total Time: " + ((System.currentTimeMillis() - starttime) / 1000));
        return ((System.currentTimeMillis() - starttime) / 1000);
    }
    public static void printVariances(){
    try {
            ResultSet rs = DbUtilities.getResultSet("SELECT gameresultid FROM nhl.gameresultview WHERE result <> 'L'");
            String gameresultid = "";
            
            double totalGoalVariance  = 0;
            double totalAssistVariance  = 0;
            double totalPointsVariance  = 0;
            double totalShotsVariance  = 0;
            double totalHitsVariance  = 0;
            double totalCorsiForVariance  = 0;
            double totalCorsiForPercentVariance  = 0;
            double totalFaceoffPercentVariance  = 0;
            double totalTakeAwaysVariance  = 0;
            double totalGiveAwaysVariance  = 0;
            double totalPIMVariance  = 0;

            int gamecount = 0;
            GameResult g;
            
            while(rs.next()){
                System.out.print("\r");
                gamecount++;
                System.out.print("Working. Game #: " + gamecount);
                gameresultid = rs.getString("gameresultid");
                g = new GameResult(gameresultid);
                totalGoalVariance += g.getGoalVariance();
                totalAssistVariance += g.getAssistVariance();
                totalPointsVariance += g.getPointsVariance();
                totalShotsVariance += g.getShotsVariance();
                totalHitsVariance += g.getHitsVariance();
                totalCorsiForVariance += g.getCorsiForVariance();
                totalCorsiForPercentVariance += g.getCorsiForPercentVariance();
                totalFaceoffPercentVariance += g.getFaceoffPercentVariance();
                totalTakeAwaysVariance += g.getTakeAwaysVariance();
                totalGiveAwaysVariance += g.getGiveAwaysVariance();
                totalPIMVariance += g.getPIMVariance();
                
                
            }
            System.out.println();
            System.out.println(gamecount);
            System.out.println("totalGoalVariance: " + totalGoalVariance / gamecount); 
            System.out.println("totalAssistVariance: " + totalAssistVariance / gamecount); 
            System.out.println("totalPointsVariance: " + totalPointsVariance / gamecount); 
            System.out.println("totalShotsVariance: " + totalShotsVariance / gamecount); 
            System.out.println("totalHitsVariance: " + totalHitsVariance / gamecount); 
            System.out.println("totalCorsiForVariance: " + totalCorsiForVariance / gamecount); 
            System.out.println("totalCorsiForPercentVariance: " + totalCorsiForPercentVariance / gamecount); 
            System.out.println("totalFaceoffPercentVariance: " + totalFaceoffPercentVariance / gamecount); 
            System.out.println("totalTakeAwaysVariance: " + totalTakeAwaysVariance / gamecount); 
            System.out.println("totalGiveAwaysVariance: " + totalGiveAwaysVariance / gamecount); 
            System.out.println("totalPIMVariance: " + totalPIMVariance / gamecount); 
            
        } catch (SQLException ex) {
            System.out.println("You suck.");
        }
        
        
    }
    public static double replaceZero(double x){
    if (x == 0) return 1;
    else return x;
    }
}
