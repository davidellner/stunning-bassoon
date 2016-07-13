/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author davidellner
 * Utilities to create, update, refresh line combinations
 */
public class LineUtilities {
    
    public static HashSet<Integer> getCurrentShifts(){
        HashSet<Integer> shifts = new HashSet<>();
        
        try{
            ResultSet rs = DbUtilities.getResultSet("SELECT shiftid FROM nhl.lineforshift");
            while(rs.next()) shifts.add(rs.getInt("shiftid"));
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return shifts;
    }
    
    public static void setLines(int initialShiftId){
        
        HashMap<HashSet<Integer>, Integer> lines = getLines();
        HashSet<Integer> currentShifts = getCurrentShifts();
       // HashSet<HashSet<Integer>> newLines = new HashSet<>();
        //build lines
        ResultSet rs;
        
        HashSet<Integer> shifts = new HashSet<>();
        //again, don't need two passes here, but concerned about db performance with two concurrent resultsets
        try{
            rs = DbUtilities.getResultSet("SELECT shiftid FROM nhl.shift WHERE shiftid >= " + initialShiftId); //+ " AND shiftid <= " + lastShiftId);
            while(rs.next()){
                shifts.add(rs.getInt("shiftid"));
            }
            String sql = "SELECT playerid FROM nhl.playeronice WHERE shiftid = ";
            HashSet<Integer> currentLine = new HashSet<>();
            for(int shiftid: shifts){
                rs = DbUtilities.getResultSet(sql + shiftid);
                
                while(rs.next()){
                    currentLine.add(rs.getInt("playerid"));
                }
                if(currentShifts.contains(shiftid)){
                 //Duplicate. Do nothing
                }
                else if(lines.containsKey(currentLine)){ //have we already written this line, but not the current shift associated?
                    //System.out.println("line already exists.");
                    //write line - shift
                    int lineid = lines.get(currentLine);
                    //System.out.println("Writing shift for duplicate lineid " + lineid);
                    
                    String lineshiftsql = "INSERT INTO nhl.lineforshift (lineid, shiftid) VALUES (" + lineid + ", " + shiftid + ");";
                    DbUtilities.executeQuery(lineshiftsql);
                }
                else{
                    //rather than maintaining two arrays, lets just add new lines to the db here
                    int newLineId = storeLine(currentLine, shiftid);
                    lines.put(currentLine, newLineId);
                    
                    
                }
                currentLine.clear();
            }
        }catch(SQLException se){
            se.printStackTrace();
        }
        
    }
    
     
    public static void main(String[] args){
        setLines(0);
    }
    
    public static HashMap<HashSet<Integer>, Integer> getLines(){
        
        String linesql = "SELECT lineid FROM nhl.line";
        String allLines = "SELECT playerid FROM nhl.playeronline WHERE lineid = ";
        HashSet<Integer> allLineIds = new HashSet<>();
        //lines holds the actual line combos
        //HashSet<HashSet<Integer>> lines = new HashSet<>();
        HashMap<HashSet<Integer>, Integer> lines = new HashMap();
        //dont need two passes here. refactor into single loop
        try{
            ResultSet rs = DbUtilities.getResultSet(linesql);
            //get all unique line ids
            while(rs.next()){
                allLineIds.add(rs.getInt("lineid"));
            }
            //initialize current line
            HashSet<Integer> currentLine = new HashSet<>(); 
            String sql;
            for(int id: allLineIds){

                sql = allLines + id;
                rs = DbUtilities.getResultSet(sql);
                
                while(rs.next()){
                    //build currentline
                    currentLine.add(rs.getInt("playerid")); 
                   // System.out.println(currentLine.toString());
                }
                
                lines.put(new HashSet<>(currentLine), id);
                currentLine.clear();
            }
        }catch(SQLException se){
            se.printStackTrace();
        }
       // System.out.println(lines.toString());
        return lines;   
    }
    
    public static int storeLine(HashSet<Integer> line, int shiftid){
        String linesql = "INSERT INTO nhl.line VALUES (null)";
        String lastid = "SELECT last_insert_id() as lastid";
        
        int lineid = 0;
        String lineshiftsql = null;
        ResultSet rs;
        String playeronlinesql = null;
        String playeronlineinsert = " VALUES ";
        try{
            DbUtilities.executeQuery(linesql);
            rs = DbUtilities.getResultSet(lastid);
            if(rs.next()) lineid = rs.getInt("lastid");
            else System.err.println("Could not write line.");
            
            //write line - shift
            lineshiftsql = "INSERT INTO nhl.lineforshift (lineid, shiftid) VALUES (" + lineid + ", " + shiftid + ");";
            DbUtilities.executeQuery(lineshiftsql);
            
            //write line combination
            
            for(int playerid : line) {
                playeronlineinsert += ("( " + lineid + ", " + playerid + " ), ");
            
            }
            playeronlinesql = "INSERT INTO nhl.playeronline (lineid, playerid)" + playeronlineinsert.substring(0, playeronlineinsert.length()-2);
           // System.out.println(playeronlinesql);
            DbUtilities.executeQuery(playeronlinesql);
            System.out.println("Wrote line #" + lineid + ". Shift #" + shiftid);
        }catch(Exception se){
            se.printStackTrace();
        }
        
      return lineid;
    }
}
