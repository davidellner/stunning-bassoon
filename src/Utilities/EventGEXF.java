/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import StateMachines.Event;
import StateMachines.Event.EventType;

import it.uniroma1.dis.wsngroup.gexf4j.core.EdgeType;
import it.uniroma1.dis.wsngroup.gexf4j.core.Gexf;
import it.uniroma1.dis.wsngroup.gexf4j.core.Graph;
import it.uniroma1.dis.wsngroup.gexf4j.core.Mode;
import it.uniroma1.dis.wsngroup.gexf4j.core.Node;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.Attribute;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeClass;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeList;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeType;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.GexfImpl;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.StaxGraphWriter;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.data.AttributeListImpl;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;


/**
 *
 * @author davidellner
 */
public class EventGEXF {
    
    
    private Gexf gexf;
    private Graph graph;
    private AttributeList attrList;
    private Attribute attEventtype;
    private int edgeId;
    private Date createTime;
    
    public EventGEXF(){
       gexf = new GexfImpl();
       Calendar date = Calendar.getInstance();
       //set metadata about graph
         gexf.getMetadata()
			.setLastModified(date.getTime())
			.setCreator("David Ellner")
			.setDescription("A visualization of NHL events");
		gexf.setVisualization(true);
                
        createTime = date.getTime();
        //get graph instance
        graph = gexf.getGraph();
        //set edge type
        graph.setDefaultEdgeType(EdgeType.DIRECTED).setMode(Mode.STATIC);
        //set attribute list? not sure what this does
        attrList = new AttributeListImpl(AttributeClass.NODE);
        graph.getAttributeLists().add(attrList);
        
        attEventtype = attrList.createAttribute("0", AttributeType.STRING, "event");
        edgeId = 0;
        
    }
    
    public Node addNode(Event e){
        Node temp = graph.getNode(Integer.toString(e.getJsonId()));
        if(temp != null){
            
            return temp;
        }
        Node newNode = graph.createNode(Integer.toString(e.getJsonId()));
             newNode
                     .setLabel(e.getEventType().toString())
                     .setSize(e.getCount())
                     .getAttributeValues()
                        .addValue(attEventtype, e.getEventType().toString());
         
        return newNode;
    }
    
    public void addEdge(Node source, Node target){
        source.connectTo(target).setEdgeType(EdgeType.DIRECTED);
        
        
    
    }
    
    public void writeToFile(){
        StaxGraphWriter graphWriter = new StaxGraphWriter();
        String filepath = System.getProperty("user.home") + "/Documents/Projects/Gexf/nhlevents.gexf";
        File f = new File(filepath);
        Writer out;
        try{
           out = new FileWriter(f, false);
           graphWriter.writeToStream(gexf, out, "UTF-8");
           System.out.println(f.getAbsolutePath());
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
}
