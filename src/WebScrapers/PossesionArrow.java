/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package WebScrapers;

/**
 *
 * @author davidellner
 */
public class PossesionArrow {
    public final String POSSESION_UNKNOWN = "UNK";
    private String homeTeam;
    private String awayTeam;
    private String possesion;
    
    public PossesionArrow(String homeTeam, String awayTeam){
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.possesion = POSSESION_UNKNOWN;
    }
    public void flip(){
        if(possesion.equals(homeTeam)) possesion = awayTeam;
        else possesion = homeTeam;
    }
    public void set(String team)  {
        if(team.equals(awayTeam) || team.equals(homeTeam)) possesion = team;
        else {
            System.out.println("Can't find team: " + team);
            this.unknown();
        }
    }
    public String getPossesion(){
        return possesion;
    }
    public void unknown(){
        this.possesion = POSSESION_UNKNOWN;
    }
    
    public String toString(){
        return this.getPossesion();
    }
}
