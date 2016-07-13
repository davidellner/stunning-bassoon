/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MarkovChain;

/**
 *
 * @author davidellner
 */
public class TeamNode implements Comparable<TeamNode>{
    
   
    String team;
    int visits;
    
    public TeamNode(String team){
        this.team = team;
        this.visits = 0;
    }
    
    public String getTeam(){
        return team;
    }
    
    public void visit(){
        visits++;
    }
    
    public int getVisits(){
        return this.visits;
    }
    
    @Override
    public int hashCode(){
        return this.team.hashCode();
    }
    
    public boolean equals(TeamNode t){
        return this.team.equals(t.team);
    }
    
    public boolean equals(String t){
        return this.team.equals(t);
    }
    
    public int compareTo(TeamNode other){
        return this.getVisits() - other.getVisits();
    }
    
}
