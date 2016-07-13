/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StateMachines;

import Utilities.DbUtilities;
import Utilities.EventGEXF;
import Utilities.EventJSON;
import it.uniroma1.dis.wsngroup.gexf4j.core.Node;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author davidellner
 */
public class Event {

    

    public enum EventType {
        FAC, HIT, WRISTSHOT, GOAL,
        SLAPSHOT, SNAPSHOT, PENL,
        TIPINSHOT, TURN, STOP, UNK,
        MISS, START, BLOCK
    }

    private EventType eventType;
    //private final ArrayList<Event> events;
    private final ArrayList<Edge> edges;
    //private ArrayList<HashSet> eventlevels;
    private int count;
    private boolean isvisited;
    private int jsonId;
    private int previousNodeId;
    
    public int getPreviousNodeId(){
        return this.previousNodeId;
    }
    
    public void setPreviousNodeId(int x){
        this.previousNodeId = x;
    }

    public int getJsonId() {
        return this.jsonId;
    }

    private void visit() {
        this.isvisited = true;
    }

    private boolean isVisited() {
        return this.isvisited;
    }

    private void setJsonId(int id) {
        this.jsonId = id;
        //this.visit();
    }

    public Event(EventType e) {
        this.edges = new ArrayList<>();
        this.count = 1;
        this.eventType = e;
        this.isvisited = false;
    }

    public Event add(EventType e) {
        Event ev = new Event(e);
        
        int dex = this.edges.indexOf(new Edge(this, ev));

        if (dex < 0) {
            this.edges.add(new Edge(this, ev));
            return ev;
        } else {
           // System.out.println("incrementing " + e);
            this.edges.get(dex).getTarget().incrementCount();

            return this.edges.get(dex).getTarget();
        }
    }

    public int getCount() {
        return this.count;
    }

    public void incrementCount() {
        this.count++;
    }

    public EventType getEventType() {
        return this.eventType;
    }

    public boolean equals(Event e) {
        return e.getEventType() == this.getEventType();
    }

    public boolean equals(EventType e) {
        return e == this.getEventType();
    }

    @Override
    public int hashCode() {
        return this.eventType.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Event other = (Event) obj;
        return this.eventType == other.eventType;
    }

    public static EventType mapEventType(String ev, String shottype) {
        EventType result = EventType.UNK;
        switch (ev) {
            case "FAC":
                result = EventType.FAC;
                break;
            case "STOP":
                result = EventType.STOP;
                break;
            case "HIT":
                result = EventType.HIT;
                break;
            case "GIVE":
                result = EventType.TURN;
                break;
            case "TAKE":
                result = EventType.TURN;
                break;
            case "SHOT":
                switch (shottype) {
                    case "Tip-In":
                        result = EventType.TIPINSHOT;
                        break;
                    case "Wrist":
                        result = EventType.WRISTSHOT;
                        break;
                    case "Snap":
                        result = EventType.SNAPSHOT;
                        break;
                    case "Slap":
                        result = EventType.SLAPSHOT;
                        break;
                }
                break;
            case "PENL":
                result = EventType.PENL;
                break;
            case "MISS":
                result = EventType.MISS;
                break;
            case "GOAL":
                result = EventType.GOAL;
                break;
            case "BLOCK":
                result = EventType.BLOCK;
                break;

        }
        return result;
    }

    @Override
    public String toString() {
        if (this.edges.size() < 1) {
            return this.eventType + " END!" + "\n";
        } else {
            String result = this.eventType + " ("+this.count + ") --> ";
            for (Edge e : this.edges) {

                result = result + e.getTarget().toString();
            }
            return result;
        }
    }
    
    public static JSONArray getJsonArray(Event root) throws JSONException {

        Event current = null;
        int lastid = 0;
        int currentid = lastid;
        int curedgeid = 0;
        Queue<Event> s = new LinkedBlockingQueue<>();
        //set jsonid of node
        root.setJsonId(lastid);
        root.setPreviousNodeId(-1);
        s.add(root);
        EventJSON json = new EventJSON();
        ArrayList<Integer> levels = new ArrayList();
        
        int nextlevel = 1;
        int x = 0;
        int ylower = 0;
        int yupper = 0;
        boolean usehigh = false;
        
        levels.add(nextlevel);
      
        while (!s.isEmpty()) {

            current = s.remove();
           // System.out.println(current.eventType + " " + levels.get(0));
            if (!current.isVisited()) {
                //this code alternates placement of nodes vertically
                int y;
                if(usehigh){
                     y = yupper;
                } else y = ylower;
                
                usehigh = !usehigh;
                
                currentid++;
                current.setJsonId(currentid);
                
                current.visit();
                 
                json.addNode(current, x, y);
                
                //use arraylist to track nodes on current level
                nextlevel = levels.get(0);
                //dec next level & reset
                nextlevel--;
                levels.set(0, nextlevel); //since we are doing bfs we can just track the first one
                
                //track position of x
                if(nextlevel == 0) {
                    x += 20;
                    //in essence, we are treating the levels arraylist as a queue of sorts
                    //we remove the zero from the front (no more levels)
                    levels.remove(0);
                    //add another level?
                    
                    
                }
                
                yupper = nextlevel * 10;
                ylower = nextlevel * -10;
                
                //add children to queue
                levels.add(0);
                for (Edge e : current.edges) {
                    e.getTarget().setPreviousNodeId(currentid);
                    s.add(e.getTarget());
                   // track number of events on next level of 
                    nextlevel++;
                    
                    
                }
                levels.set(levels.size()-1, nextlevel);
                //set last id
                lastid = currentid;
            }
        }
        return json.getEventJson();
    }
    public static void toGexf(Event root) {

        Event current = null;
        int lastid = 0;
        int currentid = lastid;
        int curedgeid = 0;
        Stack<Event> s = new Stack<>();
        //set jsonid of node
        root.setJsonId(lastid);
        //root.setPreviousNodeId(-1);
        s.add(root);
        EventGEXF gexf = new EventGEXF();
        //ArrayList<Integer> levels = new ArrayList();
        
        //int nextlevel = 1;
        //int x = 0;
        //int ylower = 0;
        //int yupper = 0;
        //boolean usehigh = false;
        
       // levels.add(nextlevel);
      Node currentNode = null;
      Node previousNode = null;
      Event temp = null;
        while (!s.isEmpty()) {

            current = s.pop();
          
            if (!current.isVisited()) {
                //currentid++;
                //current.setJsonId(currentid);
                
                current.visit();
                 
                currentNode = gexf.addNode(current);
                
                for (Edge e : current.edges) {
                    currentid++;
                    e.getTarget().setJsonId(currentid);
                    gexf.addEdge(currentNode, gexf.addNode(e.getTarget()));
                    s.push(e.getTarget());      
                }

                //set last id
                lastid = currentid;
                previousNode = currentNode;
            }
        }
        gexf.writeToFile();
    }

    public static void main(String[] args) {

        try {
            String sql = "SELECT eventtype, shottype FROM nhl.playbyplay WHERE gameid = 20001 AND eventtype NOT IN ('PSTR', 'PEND') ORDER BY playbyplayid ASC";
            ResultSet rs = DbUtilities.getResultSet(sql);
            Event ev = null;
            Event base = null;
            String eventstr;
            String shottype;
            
            ev = new Event(EventType.START);
            base = ev;
            
            while (rs.next()) {

                eventstr = rs.getString("eventtype").trim();
                shottype = rs.getString("shottype").trim();
                ev = ev.add(mapEventType(eventstr, shottype));
                
                if (ev.eventType == EventType.STOP || ev.eventType == EventType.GOAL) {
                    ev = base;
                         
                }       
            }
         
           // System.out.println(getJsonArray(base));
            
            toGexf(base);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
