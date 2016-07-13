/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package WebScrapers;

import Utilities.DbUtilities;
import java.util.ArrayList;

/**
 *
 * @author davidellner
 */
public class GameSummary {
    final String RESULT_WON = "W";
    final String RESULT_LOSS = "L";
    String homeTeamName;
    String awayTeamName;
    int gameId;
    ArrayList<PlayerData> homeTeam;
    ArrayList<PlayerData> awayTeam;
    String homeResult;
    String awayResult;
    int homeGoals;
    int awayGoals;
    
    
    public GameSummary(ArrayList<PlayerData> homeTeam, ArrayList<PlayerData> awayTeam){
    
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        
        //Grab first playerdata
        PlayerData homePlayer = homeTeam.get(1);
        PlayerData awayPlayer = awayTeam.get(1);
        
        this.homeTeamName = homePlayer.getTeam();
        this.awayTeamName = awayPlayer.getTeam();
        
        this.gameId = homePlayer.gameId;
        
        for(int i = 0; i < homeTeam.size(); i++) homeGoals += homeTeam.get(i).getGoals();
        for(int i = 0; i < awayTeam.size(); i++) awayGoals += awayTeam.get(i).getGoals();
        
        if(homeGoals > awayGoals){
            this.homeResult = RESULT_WON;
            this.awayResult = RESULT_LOSS;
        } else if (awayGoals > homeGoals){
            this.homeResult = RESULT_LOSS;
            this.awayResult = RESULT_WON;
        } 
    }
    public void writeToDb(){
        String homesql = "INSERT INTO nhl.gamesummary ( "+
        "gameid, team, goals, result, homeoraway)" +
                "VALUES ("
                + gameId + ", '" + homeTeamName + "', " + homeGoals + ",'" + homeResult + "', 'HOME');";
        
        String awaysql = "INSERT INTO nhl.gamesummary ( "+
        "gameid, team, goals, result, homeoraway)" +
                "VALUES ("
                + gameId + ", '" + awayTeamName + "', " + awayGoals + ",'" + awayResult + "', 'AWAY');";
        //System.out.println(homesql);
        //System.out.println(awaysql);
        DbUtilities.executeQuery(homesql);
        DbUtilities.executeQuery(awaysql);
        
    }
    
    
}
