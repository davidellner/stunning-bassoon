/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MarkovChain;

import java.util.ArrayList;
import java.util.Arrays;


/**
 *
 * @author davidellner
 */
public class TeamArray {
    
    private final TeamNode[] teams;
    private int nextavailable = 0;
    
    public TeamArray(){
        teams = new TeamNode[30];
    }
    
    public TeamNode get(int i){
        return this.teams[i];
    }
    
    public void add(TeamNode t){
        teams[nextavailable] = t;
        nextavailable++;
    }
    
    public int size(){
        return nextavailable;
    }
    
    public void visitTeam(String team){       
        this.get(team).visit();  
    }
    
    public TeamNode get(String team){
        TeamNode target = null;
        for(TeamNode t : teams){
            if(t.getTeam().equals(team)){
                return t;
            }
        }
        return target;
    }
    
    public void sort(){
        Arrays.sort(teams);
    }
    
}
