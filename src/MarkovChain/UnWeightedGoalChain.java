/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MarkovChain;

import Utilities.DbUtilities;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author davidellner
 */
public class UnWeightedGoalChain {
    
    TeamArray teams;
    EdgeArray edges;
    
    public UnWeightedGoalChain(){
        this.getTeams();
        this.getEdges();
    }
    // get all team names from database and set the in - memory team array
    private void getTeams(){
        teams = new TeamArray();
        
        String sql = "SELECT DISTINCT playbyplaycode FROM nhl.team";

        try {
            ResultSet rs = DbUtilities.getResultSet(sql);
            while(rs.next()){
                teams.add(new TeamNode(rs.getString("playbyplaycode")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }
       
    private void getEdges(){
        
        String sql = "SELECT actingteam AS targetteam, againstteam AS sourceteam "
                + "FROM nhl.playbyplay "
                + "WHERE eventtype = 'GOAL' AND strength = 'EV' AND period < 4 "
                + "AND (actingsituation < 2 OR actingsituation > -2) "
                + "AND goaliepulled IS NULL "
                + "AND gameid > 20660;";
        //System.out.println(sql);
        edges = new EdgeArray();
        try{
            ResultSet rs = DbUtilities.getResultSet(sql);
            while(rs.next()){
                edges.addEdge(
                        new UnWeightedEdge(rs.getString("sourceteam"), rs.getString("targetteam"))
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    //return a string with 
    private String concatTeamCodeString(){
        if(teams.size() < 30){
            System.err.println("Error - Less than 30 teams in team node array");
            return "";
        }
        else {
            String teamstr = "(";
            for(int i = 0; i < teams.size(); i++){
                teamstr += ", ";
                teamstr += "'" + teams.get(i).getTeam() + "'";
                
            }
        
            teamstr += ")";
            return teamstr.replaceFirst(",", "");
        }
    }
    
    //randomly select a team. 
    public void traverse(int maxruns){
        //get a random team
        Random random = new Random();
        TeamNode t = teams.get(random.nextInt(30));
        String nextTeam;
        ArrayList<String> targets;
        //main loop to iterate
        for(int i = 0; i < maxruns; i++){
            
            targets = edges.getTargetTeamsBySourceTeam(t.getTeam());
            nextTeam = targets.get(random.nextInt(targets.size()));
            
            t = teams.get(nextTeam);
            t.visit();
            
        }
    
    }
    
    public static void main(String[] args){
        UnWeightedGoalChain c = new UnWeightedGoalChain();
        c.traverse(1000000);
        c.teams.sort();
        for(int i = 0; i < c.teams.size(); i++){
            
            System.out.println(c.teams.get(i).getTeam() + ": " + c.teams.get(i).getVisits());
        }
    }
}
