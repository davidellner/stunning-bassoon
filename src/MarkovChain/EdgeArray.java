/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MarkovChain;

import java.util.ArrayList;

/**
 *
 * @author davidellner
 */
public class EdgeArray {
    
    
    public ArrayList<UnWeightedEdge> edges;
    
    public EdgeArray(){
        this.edges = new ArrayList();
    }
    
    public void addEdge(UnWeightedEdge e){
        edges.add(e);

    }
    
    public ArrayList<String> getTargetTeamsBySourceTeam(String sourceTeam){
        ArrayList<String> targets = new ArrayList();
        
        for(int i = 0; i < edges.size(); i++){
            if(edges.get(i).getSourceTeam().equals(sourceTeam)){
                targets.add(edges.get(i).getTargetTeam());
            }
        }
        
        return targets;
        
    }
}
