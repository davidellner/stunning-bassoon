/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import StateMachines.Event;
import StateMachines.Event.EventType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author davidellner
 */
public class EventJSON {
    
    
    private JSONArray nodes;
    private JSONArray edges;
    private int curedgeid;
    
    public EventJSON(){
        this.nodes = new JSONArray();
        this.edges = new JSONArray();
        this.curedgeid = 0;
    }
    
    public void addNode(Event e, int x, int y) throws JSONException{
        
        //TODO: Need algorithm to set x, y
        
        JSONObject temp = new JSONObject();
        temp.put("id", e.getJsonId());
        temp.put("label", e.getEventType());
        temp.put("size", e.getCount());
        temp.put("x", x);
        temp.put("y", y);
        temp.put("color", chooseColor(e.getEventType()));
        this.nodes.put(temp);
        if(e.getPreviousNodeId() >= 0){
            this.addEdge(this.curedgeid++, e.getPreviousNodeId(), e.getJsonId());
        }
    }
    
    public void addEdge(int id, int source, int target) throws JSONException{
        JSONObject temp = new JSONObject();
        temp.put("source", source);
        temp.put("target", target);
        temp.put("id", id);
        this.edges.put(temp);
    
    }
    public JSONArray getEventJson() throws JSONException{
        JSONObject tmpNodes = new JSONObject();
        tmpNodes.put("nodes", nodes);
        JSONObject tmpEdges = new JSONObject();
        tmpNodes.put("edges", edges);
        JSONArray tmp = new JSONArray();
        
        tmp.put(tmpNodes);
        tmp.put(tmpEdges);
        
        return tmp;
    }
    private static String chooseColor(EventType e){
        String color = "rgb(0,0,0)";
        switch(e){
            case FAC:
                
                break;
            case STOP:
                color = "rgb(255,0,0)";
                break;
            case HIT:
                color = "rgb(100,100,0)";
                break;
            case TURN:
                color = "rgb(175, 20, 50)";
                break;
           
            case WRISTSHOT:
                color = "rgb(20, 150, 60)";
                break;
            case TIPINSHOT:
                color = "rgb(20, 150, 60)";
                break;
            case SLAPSHOT:
                color = "rgb(20, 150, 60)";
                break;
            case SNAPSHOT:
                color = "rgb(20, 150, 60)";
                break;
            case PENL:
                color = "rgb(185, 20, 95)";
                break;
            case MISS:
                color = "rgb(20, 120, 80)";
                break;
            case GOAL:
                color = "rgb(0, 255, 0)";
                break;
            case BLOCK:
                color = "rgb(20, 60, 120)";
                break;

        }
        return color;
    }
}
