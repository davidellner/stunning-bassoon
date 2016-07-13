/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StateMachines;

import java.util.ArrayList;

/**
 *
 * @author davidellner
 */
public class Edge {
    
    private Event source;
    private Event target;
    private int id;
    
    public Edge(Event source, Event target){
        
        this.target = target;
        this.source = source;
        this.id = id;
        
    }
    
    public Event getTarget(){
        
        return this.target;
    
    }
    
    @Override
    public int hashCode(){
        return this.target.hashCode();
    }
    
    public boolean equals(Edge e){
        return e.getTarget().equals(this.getTarget());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Edge other = (Edge) obj;
        return this.target.equals(other.getTarget());
    }
    
}
