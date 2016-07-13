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
public class UnWeightedEdge {
    
    private String sourceTeam;
    private String targetTeam;
    
    public UnWeightedEdge(String sourceTeam, String targetTeam){
        this.sourceTeam = sourceTeam;
        this.targetTeam = targetTeam;
    }
    /**
     * @return the sourceTeam
     */
    public String getSourceTeam() {
        return sourceTeam;
    }

    /**
     * @param sourceTeam the sourceTeam to set
     */
    public void setSourceTeam(String sourceTeam) {
        this.sourceTeam = sourceTeam;
    }

    /**
     * @return the targetTeam
     */
    public String getTargetTeam() {
        return targetTeam;
    }

    /**
     * @param targetTeam the targetTeam to set
     */
    public void setTargetTeam(String targetTeam) {
        this.targetTeam = targetTeam;
    }
    
    
    
    
}
