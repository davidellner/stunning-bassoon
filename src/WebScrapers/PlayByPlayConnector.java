/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package WebScrapers;

import Utilities.DbUtilities;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author davidellner
 */
public class PlayByPlayConnector {
    int gameId;
    private final int visitorOnIce = 0;
    private final int homeOnIce = 7;
    private String homeTeam = "";
    private String visitingTeam = "";
    private int homeSituation = 0;
    private int awaySituation = 0;
    private PossesionArrow possesion;
    
    public PlayByPlayConnector(int gameId){
    this.gameId = gameId;
    }
    
    public void fetchPlayByPlay() throws IOException{
        Connection c;
        Document doc;
        Elements visitorPlayerHtml;
        Elements visitors;
        
        int playbyplayid;
        
        //Code to fetch from raw file (practice where there is no internet connection)
        //File file = new File("./playbyplay.html");
        //doc = Jsoup.parse(file, "UTF-8");
        //String URL = BASE_URL + START_YEAR + END_YEAR + "/" + EVENT_SUMMARY_TAG + gameId + ".HTM";   
            
            c = Jsoup.connect("http://www.nhl.com/scores/htmlreports/20152016/PL0"+ Integer.toString(gameId) +".HTM");
            doc = c.timeout(0).maxBodySize(0).get(); //help me jesus
            //System.out.println("Connected to URL ");
         
            String[] s = getTeams(doc);
            
            String v = s[0];
            String h = s[1];
        try {            
            ResultSet rs = DbUtilities.getResultSet("SELECT playbyplaycode p FROM nhl.team WHERE teamcode = '" + v + "';");
            if(rs.next()){
                visitingTeam = rs.getString("p");
                rs.close();
            }
            ResultSet r = DbUtilities.getResultSet("SELECT playbyplaycode p FROM nhl.team WHERE teamcode = '" + h + "';");
            if(r.next()){
               homeTeam = r.getString("p");
               r.close();
            }
        } catch (SQLException ex) {
           
        }
            
            this.possesion = new PossesionArrow(homeTeam, visitingTeam); //create the possesion arrow
            
            Elements evenRowEach = doc.getElementsByClass("evenColor");
            //Elements evenCells = evenRowEach.select("td");
            Elements cells;
            PlayByPlay p;
            boolean goaliePulled;
            String update;
            for(Element e: evenRowEach){
                //TODO: Write a method to parse each "row"
                //System.out.println(e.select("tbody").get(0));
                
                p = parsePlayByPlayRow(e, Integer.toString(gameId));
                
                
                
            }
               /* parsePlayersOnIce_debug(
                evenRowEach.get(314),
                0,//gameId, REPLACE ME BEFORE RUNNING
                "CHI", //home
                "NYR", //away
                -1,
                "test",
                "test"
                );
                break;
            } */  
            
            

    }   
    
    private PlayByPlay parsePlayByPlayRow(Element row, String gameId){
        Elements cells = row.select("td");
        int i = 0;
        
        String eventtype = cells.get(4).text(); //DONT CHANGE!!!
        PlayByPlay p = null;
        //System.out.println(eventtype);
        //we need a different method for each event type
        switch(eventtype){
            case "PSTR": p = parsePeriodStart(cells, gameId, row); break;
            case "FAC": p = parseFaceoff(cells, gameId, row); break;
            case "TAKE": p = parseTakeAway(cells, gameId, row); break;
            case "MISS": p = parseMiss(cells, gameId, row); break;
            case "HIT": p = parseHit(cells, gameId, row); break;
            case "SHOT": p = parseShot(cells, gameId, row); break;
            case "BLOCK": p = parseBlock(cells, gameId, row); break;
            case "PENL": p = parsePenalty(cells, gameId, row); break;
            case "GIVE": p = parseGive(cells, gameId, row); break;
            case "STOP": p = parseStop(cells, gameId, row); break;
            case "GOAL": p = parseGoal(cells, gameId, row); break;
            case "PEND": p = parsePeriodStart(cells, gameId, row); break;
               
        }
        
        
        return p;
    }   
    
    private PlayByPlay parseGoal(Elements cells, String gameId, Element completeElement){
        String desc = cells.get(5).text();
        
        String actingTeam = desc.substring(0, 3).trim();
        
        possesion.set(actingTeam);
         
        //int dashIndex = desc.indexOf("-");
        //int vsIndex = desc.indexOf("vs");
        //int commaIndex = desc.indexOf(",");
        int firstParen = desc.indexOf("(");
        int actingSituation;
        
        if(actingTeam.equals(homeTeam)){
            actingSituation = homeSituation; //lazy refactor
            homeSituation++;
            awaySituation--;
            
        }else{
            actingSituation = awaySituation; //lazy refactor
            homeSituation--;
            awaySituation++;     
        }
        
        if(firstParen < 0) firstParen = desc.indexOf(",");
        
        int actingNum = NumberUtils.toInt(desc.substring(5, 7).trim());
        String actingName = desc.substring(7, firstParen).trim();
        
        String[] arr = desc.split(",");
        boolean adjustlow = arr.length < 4;
        boolean adjusthigh = arr.length > 4;
        String shottype;
        String zone;
        String dist;
        int sp;
        int distance;
        
        if(adjusthigh){ //penalty shot
            shottype = arr[1].trim();
            zone = arr[2].trim();
            dist = arr[4];
            sp = dist.indexOf("ft");
            distance = NumberUtils.toInt(dist.substring(0,sp-1).trim());
        }
        else if(adjustlow){
        
            shottype = "";
            zone = arr[1].trim();
            dist = arr[2];
            sp = dist.indexOf("ft");
            distance = NumberUtils.toInt(dist.substring(0,sp-1).trim());
            
        } else {
        shottype = arr[1].trim();
        zone = arr[2].trim();
        dist = arr[3];
        sp = dist.indexOf("ft");
        distance = NumberUtils.toInt(dist.substring(0,sp-1).trim());
        }    
        String againstTeam = "";
        int againstNumber = -1;
        String againstName = "";
        //time here
        String time = cells.get(3).text();
        int space = time.indexOf(" ");
        String timeElapsed = time.substring(0, space).trim();
        
        PlayByPlay pl = new PlayByPlay(
            NumberUtils.toInt(gameId), //gameid
            NumberUtils.toInt(cells.get(0).text()), //play#
            NumberUtils.toInt(cells.get(1).text()), //period
            cells.get(2).text(),
            timeElapsed, //Time Elapsed
            "GOALSHOT",
            cells.get(5).text(), //Description
            actingNum,   //actingplayernumber
            actingName,    //actingplayername
             actingTeam,   //actingteam
             againstNumber,   //againstplayernumber
            againstName,    //againstplayername
            againstTeam,    //againstteam
            zone,    //zone
            distance,    //distance
            shottype,    //shottype
            actingSituation,
            possesion.getPossesion()
        );
        pl.writeToDb();
        
        try {
            ResultSet rs = DbUtilities.getResultSet("SELECT last_insert_id() AS id");
            while(rs.next()){
                pl.playbyplayid = rs.getInt("id");
            }
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
       
        parsePlayersOnIce(
            completeElement,
            NumberUtils.toInt(gameId),//gameId, REPLACE ME BEFORE RUNNING
            homeTeam, //home
            visitingTeam, //away
            pl.playbyplayid,
            pl.actingplayername,
            pl.actingteam,
            pl.playnumber
        );
        
        parseAssists(
        desc, gameId, NumberUtils.toInt(cells.get(0).text()), timeElapsed, shottype, actingTeam, cells.get(2).text(), NumberUtils.toInt(cells.get(1).text()), distance, -1, actingSituation
        );
        
        PlayByPlay p = new PlayByPlay(
            NumberUtils.toInt(gameId), //gameid
            NumberUtils.toInt(cells.get(0).text()), //play#
            NumberUtils.toInt(cells.get(1).text()), //period
            cells.get(2).text(),
            timeElapsed, //Time Elapsed
            "GOAL",
            cells.get(5).text(), //Description
            actingNum,   //actingplayernumber
            actingName,    //actingplayername
             actingTeam,   //actingteam
             againstNumber,   //againstplayernumber
            againstName,    //againstplayername
            againstTeam,    //againstteam
            zone,    //zone
            distance,    //distance
            shottype,    //shottype
            actingSituation,
            possesion.getPossesion()
        );
        p.writeToDb();
        try {
            ResultSet rs = DbUtilities.getResultSet("SELECT last_insert_id() AS id");
            while(rs.next()){
                p.playbyplayid = rs.getInt("id");
            }
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        parsePlayersOnIce(
            completeElement,
            NumberUtils.toInt(gameId),//gameId, REPLACE ME BEFORE RUNNING
            homeTeam, //home
            visitingTeam, //away
            p.playbyplayid,
            p.actingplayername,
            p.actingteam,
            p.playnumber
        );
        //add a shot too!!!
        
        
        possesion.unknown();
        return p;
    }
    private PlayByPlay parseStop(Elements cells, String gameId, Element e){
        String desc = cells.get(5).text();
        
        String actingTeam = "";
        //int dashIndex = desc.indexOf("-");
        //int vsIndex = desc.indexOf("vs");
        //int commaIndex = desc.indexOf(",");
        //int blockedIndex = desc.indexOf("BLOCKED");
        //int endOfName = desc.indexOf("\u00a0");
        //int firstComma = desc.indexOf(",");
        int actingNum = -1;
        //int colon = desc.indexOf(":");
        String actingName = "";
        
        //String[] arr = desc.split(" ");
        String shottype = "";
        String zone = "";
        //String dist = arr[3];
        //int sp = dist.indexOf("ft");
        //int distance = NumberUtils.toInt(dist.substring(0,sp-1).trim());
                
        String againstTeam = "";
        //System.out.println(desc.substring(colon+6, colon+8).trim());
        int againstNumber = -1;
        String againstName = "";
        //time here
        String time = cells.get(3).text();
        int space = time.indexOf(" ");
        String timeElapsed = time.substring(0, space).trim();
        
        int actingSituation = 0;
        
        
        PlayByPlay p = new PlayByPlay(
            NumberUtils.toInt(gameId), //gameid
            NumberUtils.toInt(cells.get(0).text()), //play#
            NumberUtils.toInt(cells.get(1).text()), //period
            cells.get(2).text(), //Strength
            timeElapsed, //Time Elapsed
            "STOP",
            desc, //Description
            actingNum,   //actingplayernumber
            actingName,    //actingplayername
             actingTeam,   //actingteam
             againstNumber,   //againstplayernumber
            againstName,    //againstplayername
            againstTeam,    //againstteam
            zone,    //zone
            -1,    //distance
            shottype,    //shottype
            actingSituation,
            possesion.getPossesion()
        );
        
        p.writeToDb();
        //boolean goaliePulled;
        try {
            ResultSet rs = DbUtilities.getResultSet("SELECT last_insert_id() AS id");
            while(rs.next()){
                p.playbyplayid = rs.getInt("id");
            }
            parsePlayersOnIce(
                e,
                NumberUtils.toInt(gameId),//gameId, REPLACE ME BEFORE RUNNING
                homeTeam, //home
                visitingTeam, //away
                p.playbyplayid,
                p.actingplayername,
                p.actingteam,
                p.playnumber
                );
                    
                    
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        return p;
    }
    private PlayByPlay parseGive(Elements cells, String gameId, Element e){
        String desc = cells.get(5).text();
        
        String actingTeam = desc.substring(0, 3).trim();
        
        possesion.set(actingTeam);
        possesion.flip();
        
        int dashIndex = desc.indexOf("-");
        //int vsIndex = desc.indexOf("vs");
        //int commaIndex = desc.indexOf(",");
        //int blockedIndex = desc.indexOf("BLOCKED");
        //int endOfName = desc.indexOf("\u00a0");
        int firstComma = desc.indexOf(",");
        int actingNum = NumberUtils.toInt(desc.substring(dashIndex+3,dashIndex+5).trim());
        //int colon = desc.indexOf(":");
        String actingName = desc.substring(dashIndex+5, firstComma).trim();
        
//        String[] arr = desc.split(" ");
        String shottype = "";
        String zone = desc.substring(firstComma+1).trim();
        //String dist = arr[3];
        //int sp = dist.indexOf("ft");
        //int distance = NumberUtils.toInt(dist.substring(0,sp-1).trim());
                
        String againstTeam = "";
        //System.out.println(desc.substring(colon+6, colon+8).trim());
        int againstNumber = -1;
        String againstName = "";
        //time here
        String time = cells.get(3).text();
        int space = time.indexOf(" ");
        String timeElapsed = time.substring(0, space).trim();
        
        int actingSituation;
        if(actingTeam.equals(homeTeam)){
            
            actingSituation = homeSituation;
        }else{
            
            actingSituation = awaySituation;
        }
        
        PlayByPlay p = new PlayByPlay(
            NumberUtils.toInt(gameId), //gameid
            NumberUtils.toInt(cells.get(0).text()), //play#
            NumberUtils.toInt(cells.get(1).text()), //period
            cells.get(2).text(), //Strength
            timeElapsed, //Time Elapsed
            "GIVE",
            cells.get(5).text(), //Description
            actingNum,   //actingplayernumber
            actingName,    //actingplayername
             actingTeam,   //actingteam
             againstNumber,   //againstplayernumber
            againstName,    //againstplayername
            againstTeam,    //againstteam
            zone,    //zone
            -1,    //distance
            shottype,    //shottype
            actingSituation,
            possesion.getPossesion()
        );
        
        p.writeToDb();
        boolean goaliePulled;
        try {
            ResultSet rs = DbUtilities.getResultSet("SELECT last_insert_id() AS id");
            while(rs.next()){
                p.playbyplayid = rs.getInt("id");
            }
            parsePlayersOnIce(
                e,
                NumberUtils.toInt(gameId),//gameId, REPLACE ME BEFORE RUNNING
                homeTeam, //home
                visitingTeam, //away
                p.playbyplayid,
                p.actingplayername,
                p.actingteam,
                p.playnumber
                );
                    
                    
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        return p;
    }
    private PlayByPlay parsePenalty(Elements cells, String gameId, Element e){
        String desc = cells.get(5).text();
        boolean benchminor = desc.contains("Served By:");
        boolean delayofgame = !benchminor && desc.contains("Delaying Game");
        //need to treat these cases 
        
        int actingNum = 0;   //actingplayernumber
        String actingName;    //actingplayername
        String actingTeam;  //actingteam
        int againstNumber = 0;   //againstplayernumber
        String againstName = "";    //againstplayername
        String againstTeam = "";    //againstteam
        String zone = "";
        String shottype;
        String time;
        String timeElapsed;
        
        if(benchminor){
        actingTeam = desc.substring(0, 3).trim();
        actingName = actingTeam;
        shottype = "BENCH MINOR";
        }
        else if(delayofgame){
        actingTeam = desc.substring(0, 3).trim();
        int hash = desc.indexOf("#");
        actingNum = NumberUtils.toInt(desc.substring(hash + 1, hash + 3).trim());
        int delay = desc.indexOf("Delaying");
        actingName = desc.substring(hash + 3, delay).trim();
        shottype = "Delay of Game";
        }
        else{
        actingTeam = desc.substring(0, 3).trim();
        //int dashIndex = desc.indexOf("-");
        //int vsIndex = desc.indexOf("vs");
        //int commaIndex = desc.indexOf(",");
        //int blockedIndex = desc.indexOf("BLOCKED");
        int endOfName = desc.indexOf("\u00a0");
        if(endOfName < 0) endOfName = 8; //Too many men?
        int firstComma = desc.indexOf(",");
        int endOfPenalty;
        if(firstComma < endOfName){
            endOfPenalty = desc.indexOf("Drawn");
            if(endOfPenalty < 0){
                endOfPenalty = desc.length() - 1;
            }
        }else{
            endOfPenalty = firstComma;
            
        }
        actingNum = NumberUtils.toInt(desc.substring(5,7).trim());
        int colon = desc.indexOf(":");
        int offset = 0;
        if(actingNum < 10) offset = 1;
        actingName = desc.substring(6 + offset, endOfName).trim();
        
      //  String[] arr = desc.split(" ");
        shottype = desc.substring(endOfName+1, endOfPenalty).trim();
        zone = "";
        //String dist = arr[3];
        //int sp = dist.indexOf("ft");
        //int distance = NumberUtils.toInt(dist.substring(0,sp-1).trim());
                
        againstTeam = desc.substring(colon+1, colon+5).trim();
        //System.out.println(desc.substring(colon+6, colon+8).trim());
        againstNumber = NumberUtils.toInt(desc.substring(colon+7, colon+9).trim());
        againstName = desc.substring(colon+9).trim();
        //time here
        
        }
        
        time = cells.get(3).text();
        int space = time.indexOf(" ");
        timeElapsed = time.substring(0, space).trim();
        
        int actingSituation;
        if(actingTeam.equals(homeTeam)){
            
            actingSituation = homeSituation;
        }else{
            
            actingSituation = awaySituation;
        }
        
        PlayByPlay p = new PlayByPlay(
            NumberUtils.toInt(gameId), //gameid
            NumberUtils.toInt(cells.get(0).text()), //play#
            NumberUtils.toInt(cells.get(1).text()), //period
            cells.get(2).text(), //Strength
            timeElapsed, //Time Elapsed
            "PENL",
            cells.get(5).text(), //Description
            actingNum,   //actingplayernumber
            actingName,    //actingplayername
             actingTeam,   //actingteam
             againstNumber,   //againstplayernumber
            againstName,    //againstplayername
            againstTeam,    //againstteam
            zone,    //zone
            -1,    //distance
            shottype,    //shottype
            actingSituation,
            possesion.getPossesion()
        );
        possesion.unknown();
        p.writeToDb();
        //boolean goaliePulled;
        try {
            ResultSet rs = DbUtilities.getResultSet("SELECT last_insert_id() AS id");
            while(rs.next()){
                p.playbyplayid = rs.getInt("id");
            }
            parsePlayersOnIce(
                e,
                NumberUtils.toInt(gameId),//gameId, REPLACE ME BEFORE RUNNING
                homeTeam, //home
                visitingTeam, //away
                p.playbyplayid,
                p.actingplayername,
                p.actingteam,
                p.playnumber
                );
                    
                    
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        return p;
    }
    private PlayByPlay parseBlock(Elements cells, String gameId, Element e){
        String desc = cells.get(5).text();
        
        String actingTeam = desc.substring(0, 3).trim();
        //int dashIndex = desc.indexOf("-");
        //int vsIndex = desc.indexOf("vs");
        int commaIndex = desc.indexOf(",");
        int blockedIndex = desc.indexOf("BLOCKED");
        
        int actingNum = NumberUtils.toInt(desc.substring(5,7).trim());
        String actingName = desc.substring(7, blockedIndex-1).trim();
        
        String[] arr = desc.split(",");
        
        String shottype;
        String zone;
        
        if(arr.length < 3){
        shottype = "";
        zone = arr[1].trim();
        } else {
        shottype = arr[1].trim();
        zone = arr[2].trim();
        }
        //String dist = arr[3];
        //int sp = dist.indexOf("ft");
        //int distance = NumberUtils.toInt(dist.substring(0,sp-1).trim());
                
        String againstTeam = desc.substring(blockedIndex+11, blockedIndex+14).trim();
        possesion.set(againstTeam);
        int againstNumber = NumberUtils.toInt(desc.substring(blockedIndex+16, blockedIndex+18).trim());
        String againstName = desc.substring(blockedIndex+18, commaIndex).trim();
        //time here
        String time = cells.get(3).text();
        int space = time.indexOf(" ");
        String timeElapsed = time.substring(0, space).trim();
        
        int actingSituation;
        if(actingTeam.equals(homeTeam)){
            
            actingSituation = homeSituation;
        }else{
            
            actingSituation = awaySituation;
        }
        
        PlayByPlay p = new PlayByPlay(
            NumberUtils.toInt(gameId), //gameid
            NumberUtils.toInt(cells.get(0).text()), //play#
            NumberUtils.toInt(cells.get(1).text()), //period
            cells.get(2).text(),
            timeElapsed, //Time Elapsed
            "BLOCK",
            cells.get(5).text(), //Description
            actingNum,   //actingplayernumber
            actingName,    //actingplayername
             actingTeam,   //actingteam
             againstNumber,   //againstplayernumber
            againstName,    //againstplayername
            againstTeam,    //againstteam
            zone,    //zone
            -1,    //distance
            shottype,    //shottype
            actingSituation,
            possesion.getPossesion()
        );
        possesion.unknown();
        p.writeToDb();
        //boolean goaliePulled;
        try {
            ResultSet rs = DbUtilities.getResultSet("SELECT last_insert_id() AS id");
            while(rs.next()){
                p.playbyplayid = rs.getInt("id");
            }
            parsePlayersOnIce(
                e,
                NumberUtils.toInt(gameId),//gameId, REPLACE ME BEFORE RUNNING
                homeTeam, //home
                visitingTeam, //away
                p.playbyplayid,
                p.actingplayername,
                p.actingteam,
                p.playnumber
                );
                    
                    
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        
        return p;
    }
    private PlayByPlay parseShot(Elements cells, String gameId, Element e){
        String desc = cells.get(5).text();
        
        String actingTeam = desc.substring(0, 3).trim();
        possesion.set(actingTeam);
        int dashIndex = desc.indexOf("-");
        //int vsIndex = desc.indexOf("vs");
        int commaIndex = desc.indexOf(",");
        
        
        int actingNum = NumberUtils.toInt(desc.substring(dashIndex+3, dashIndex+5).trim());
        String actingName = desc.substring(dashIndex+5, commaIndex).trim();
        
        String[] arr = desc.split(",");
        int adj = arr.length - 4; //adjust for penalty shots
        String shottype = arr[1].trim(); //for penalty shots, we want to store "penalty shot"
        String zone = arr[adj + 2].trim();
        String dist = arr[adj + 3];
        int sp = dist.indexOf("ft");
        int distance = NumberUtils.toInt(dist.substring(0,sp-1).trim());
                
        String againstTeam = "";
        int againstNumber = -1;
        String againstName = "";
        //time here
        String time = cells.get(3).text();
        int space = time.indexOf(" ");
        String timeElapsed = time.substring(0, space).trim();
        
        int actingSituation;
        if(actingTeam.equals(homeTeam)){
            
            actingSituation = homeSituation;
        }else{
            
            actingSituation = awaySituation;
        }
        
        PlayByPlay p = new PlayByPlay(
            NumberUtils.toInt(gameId), //gameid
            NumberUtils.toInt(cells.get(0).text()), //play#
            NumberUtils.toInt(cells.get(1).text()), //period
            cells.get(2).text(),
            timeElapsed, //Time Elapsed
            "SHOT",
            cells.get(5).text(), //Description
            actingNum,   //actingplayernumber
            actingName,    //actingplayername
             actingTeam,   //actingteam
             againstNumber,   //againstplayernumber
            againstName,    //againstplayername
            againstTeam,    //againstteam
            zone,    //zone
            distance,    //distance
            shottype,    //shottype
            actingSituation,
            possesion.getPossesion()
        );
        possesion.unknown();
        p.writeToDb();
       // boolean goaliePulled;
        try {
            ResultSet rs = DbUtilities.getResultSet("SELECT last_insert_id() AS id");
            while(rs.next()){
                p.playbyplayid = rs.getInt("id");
            }
            parsePlayersOnIce(
                e,
                NumberUtils.toInt(gameId),//gameId, REPLACE ME BEFORE RUNNING
                homeTeam, //home
                visitingTeam, //away
                p.playbyplayid,
                p.actingplayername,
                p.actingteam,
                p.playnumber
                );
                    
                    
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        return p;
    }
    private PlayByPlay parseHit(Elements cells, String gameId, Element e){
        String desc = cells.get(5).text();
        
        String actingTeam = desc.substring(0, 3);
        int dashIndex = desc.indexOf("-");
        //int vsIndex = desc.indexOf("vs");
        int commaIndex = desc.indexOf(",");
        String zone = desc.substring(commaIndex+1).trim();
        
        int actingNum = NumberUtils.toInt(desc.substring(5, 7).trim()); //This isn't always 2 digits...
        
        int hitIndex = desc.indexOf("HIT");
        int startOfName = desc.indexOf(" ", 5);
        String actingName = desc.substring(startOfName, hitIndex-1).trim();
        
        String againstTeam = desc.substring(hitIndex+4,hitIndex+7);
        
        
        int againstNumber = NumberUtils.toInt(desc.substring(hitIndex+9,hitIndex+11).trim());
        String againstName = desc.substring(hitIndex+11, commaIndex).trim();
        String time = cells.get(3).text();
        int space = time.indexOf(" ");
        String timeElapsed = time.substring(0, space).trim();
        
        int actingSituation;
        if(actingTeam.equals(homeTeam)){
           
            actingSituation = homeSituation;
        }else{
            
            actingSituation = awaySituation;
        }
        
        PlayByPlay p = new PlayByPlay(
            NumberUtils.toInt(gameId), //gameid
            NumberUtils.toInt(cells.get(0).text()), //play#
            NumberUtils.toInt(cells.get(1).text()), //period
            cells.get(2).text(),
            timeElapsed, //Time Elapsed
            "HIT",
            cells.get(5).text(), //Description
            actingNum,   //actingplayernumber
            actingName,    //actingplayername
             actingTeam,   //actingteam
             againstNumber,   //againstplayernumber
            againstName,    //againstplayername
            againstTeam,    //againstteam
            zone,    //zone
            -1,    //distance
            null,    //shottype
            actingSituation,
            possesion.getPossesion()
        );
        
        p.writeToDb();
        boolean goaliePulled;
        try {
            ResultSet rs = DbUtilities.getResultSet("SELECT last_insert_id() AS id");
            while(rs.next()){
                p.playbyplayid = rs.getInt("id");
            }
            parsePlayersOnIce(
                e,
                NumberUtils.toInt(gameId),//gameId, REPLACE ME BEFORE RUNNING
                homeTeam, //home
                visitingTeam, //away
                p.playbyplayid,
                p.actingplayername,
                p.actingteam,
                p.playnumber
                );
                    
                    
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        return p;
    }
    private PlayByPlay parseMiss(Elements cells, String gameId, Element e){
        String desc = cells.get(5).text();
        
        String actingTeam = desc.substring(0, 3);
       // int dashIndex = desc.indexOf("-");
        //int vsIndex = desc.indexOf("vs");
        int commaIndex = desc.indexOf(",");
        
        possesion.set(actingTeam);
        
        int actingNum = NumberUtils.toInt(desc.substring(5, 7).trim());
        int startOfName = desc.indexOf(" ", 5);
        String actingName = desc.substring(startOfName, commaIndex).trim();
        
        String[] arr = desc.split(",");
        int adj = arr.length - 5;
        String shottype = arr[1].trim();
        String zone = arr[adj + 3].trim();
        String dist = arr[adj + 4];
        int sp = dist.indexOf("ft");
        int distance = NumberUtils.toInt(dist.substring(0,sp-1).trim());
                
        String againstTeam = "";
        int againstNumber = -1;
        String againstName = "";
        //time here
        String time = cells.get(3).text();
        int space = time.indexOf(" ");
        String timeElapsed = time.substring(0, space).trim();
        
        int actingSituation;
        if(actingTeam.equals(homeTeam)){
            
            actingSituation = homeSituation;
        }else{
            
            actingSituation = awaySituation;
        }
        
        PlayByPlay p = new PlayByPlay(
            NumberUtils.toInt(gameId), //gameid
            NumberUtils.toInt(cells.get(0).text()), //play#
            NumberUtils.toInt(cells.get(1).text()), //period
            cells.get(2).text(),
            timeElapsed, //Time Elapsed
            "MISS",
            cells.get(5).text(), //Description
            actingNum,   //actingplayernumber
            actingName,    //actingplayername
             actingTeam,   //actingteam
             againstNumber,   //againstplayernumber
            againstName,    //againstplayername
            againstTeam,    //againstteam
            zone,    //zone
            distance,    //distance
            shottype,    //shottype
            actingSituation,
            possesion.getPossesion()
        );
        possesion.unknown();
        p.writeToDb();
        //boolean goaliePulled;
        try {
            ResultSet rs = DbUtilities.getResultSet("SELECT last_insert_id() AS id");
            while(rs.next()){
                p.playbyplayid = rs.getInt("id");
            }
            parsePlayersOnIce(
                e,
                NumberUtils.toInt(gameId),//gameId, REPLACE ME BEFORE RUNNING
                homeTeam, //home
                visitingTeam, //away
                p.playbyplayid,
                p.actingplayername,
                p.actingteam,
                p.playnumber
                );
                    
                    
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        return p;
    }
    private PlayByPlay parseTakeAway(Elements cells, String gameId, Element e){
        String desc = cells.get(5).text();
        
        String actingTeam = desc.substring(0, 3);
        
        possesion.set(actingTeam);
        
        int dashIndex = desc.indexOf("-");
        //int vsIndex = desc.indexOf("vs");
        int commaIndex = desc.indexOf(",");
        String zone = desc.substring(commaIndex+1).trim();
        
        int actingNum = NumberUtils.toInt(desc.substring(dashIndex + 3, dashIndex + 5).trim());
        String actingName = desc.substring(dashIndex + 5, commaIndex).trim();
        String againstTeam = "";
        int againstNumber = -1;
        String againstName = "";
        String time = cells.get(3).text();
        int space = time.indexOf(" ");
        String timeElapsed = time.substring(0, space).trim();
        
        int actingSituation;
        if(actingTeam.equals(homeTeam)){
            
            actingSituation = homeSituation;
        }else{
            
            actingSituation = awaySituation;
        }
        
        PlayByPlay p = new PlayByPlay(
            NumberUtils.toInt(gameId), //gameid
            NumberUtils.toInt(cells.get(0).text()), //play#
            NumberUtils.toInt(cells.get(1).text()), //period
            cells.get(2).text(),
            timeElapsed, //Time Elapsed
            "TAKE",
            cells.get(5).text(), //Description
            actingNum,   //actingplayernumber
            actingName,    //actingplayername
             actingTeam,   //actingteam
             againstNumber,   //againstplayernumber
            againstName,    //againstplayername
            againstTeam,    //againstteam
            zone,    //zone
            -1,    //distance
            null,    //shottype
            actingSituation,
            possesion.getPossesion()
        );
        
        p.writeToDb();
        boolean goaliePulled;
        try {
            ResultSet rs = DbUtilities.getResultSet("SELECT last_insert_id() AS id");
            while(rs.next()){
                p.playbyplayid = rs.getInt("id");
            }
            parsePlayersOnIce(
                e,
                NumberUtils.toInt(gameId),//gameId, REPLACE ME BEFORE RUNNING
                homeTeam, //home
                visitingTeam, //away
                p.playbyplayid,
                p.actingplayername,
                p.actingteam,
                p.playnumber
                );
                    
                    
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        return p;
    }
    private PlayByPlay parseFaceoff(Elements cells, String gameId, Element e){
        String desc = cells.get(5).text();
        
        String actingTeam = desc.substring(0, 3);
        
        possesion.set(actingTeam);
        
        int dashIndex = desc.indexOf("-");
        int vsIndex = desc.indexOf("vs");
        String zone = desc.substring(7, dashIndex-1).trim();
       // System.out.println(desc.substring(dashIndex + 7, dashIndex + 9).trim());
        
        int actingNum = 100;
        String actingName = "";
        String againstTeam = "";
        int againstNumber = 100;
        String againstName = "";
        String time = cells.get(3).text();
        int space = time.indexOf(" ");
        String timeElapsed = time.substring(0, space).trim();
        
        String teama = desc.substring(vsIndex + 2, vsIndex + 6).trim();
        String teamb = desc.substring(dashIndex + 2, dashIndex + 5).trim();
        
        if(teama.equals(actingTeam)){
                   againstName = desc.substring(dashIndex + 9, vsIndex).trim();
                   againstNumber = NumberUtils.toInt(desc.substring(dashIndex + 7, dashIndex + 9).trim());
                   
                   actingName = desc.substring(vsIndex + 10).trim();
                   actingNum = NumberUtils.toInt(desc.substring(vsIndex + 8, vsIndex + 10).trim());
                   //find target player id
                   againstTeam = teamb;
                   
               }
               else if(teamb.equals(actingTeam)){
                   actingName = desc.substring(dashIndex + 9, vsIndex).trim();
                   actingNum = NumberUtils.toInt(desc.substring(dashIndex + 7, dashIndex + 9).trim());
                   
                   againstName = desc.substring(vsIndex + 10).trim();
                   againstNumber = NumberUtils.toInt(desc.substring(vsIndex + 8, vsIndex + 10).trim());
                   
                   againstTeam = teama;
               }
               
        
        
        int actingSituation;
        if(actingTeam.equals(homeTeam)){
            
            actingSituation = homeSituation;
        }else{
           
            actingSituation = awaySituation;
        }
        
        PlayByPlay p = new PlayByPlay(
            NumberUtils.toInt(gameId), //gameid
            NumberUtils.toInt(cells.get(0).text()), //play#
            NumberUtils.toInt(cells.get(1).text()), //period
            cells.get(2).text(),
            timeElapsed, //Time Elapsed
            "FAC",
            cells.get(5).text(), //Description
            actingNum,   //actingplayernumber
            actingName,    //actingplayername
             actingTeam,   //actingteam
             againstNumber,   //againstplayernumber
            againstName,    //againstplayername
            againstTeam,    //againstteam
            zone,    //zone
            -1,    //distance
            "",    //shottype
            actingSituation,
            possesion.getPossesion()
        );
        
        p.writeToDb();
        boolean goaliePulled;
        try {
            ResultSet rs = DbUtilities.getResultSet("SELECT last_insert_id() AS id");
            while(rs.next()){
                p.playbyplayid = rs.getInt("id");
            }
            parsePlayersOnIce(
                e,
                NumberUtils.toInt(gameId),//gameId, REPLACE ME BEFORE RUNNING
                homeTeam, //home
                visitingTeam, //away
                p.playbyplayid,
                p.actingplayername,
                p.actingteam,
                p.playnumber
                );
                    
                    
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        return p;
    }
    private PlayByPlay parsePeriodStart(Elements cells, String gameId, Element e){
        String desc = cells.get(5).text();
        
        possesion.unknown();
        
        PlayByPlay p = new PlayByPlay(
            NumberUtils.toInt(gameId), //gameid
            NumberUtils.toInt(cells.get(0).text()), //play#
            NumberUtils.toInt(cells.get(1).text()), //period
            cells.get(2).text(),
            cells.get(3).text(), //Time Elapsed
            "PSTR",
            desc, //Description
             //Strength
            -1,   //actingplayernumber
            "",    //actingplayername
             "",   //actingteam
             -1,   //againstplayernumber
            "",    //againstplayername
            "",    //againstteam
            "",    //zone
            -1,    //distance
            "",    //shottype
            0,
            possesion.getPossesion()
        );
        
        //this is the actual writing to db. Outsourcing this logic to the individual submethods
        p.writeToDb();
        boolean goaliePulled;
        try {
            ResultSet rs = DbUtilities.getResultSet("SELECT last_insert_id() AS id");
            while(rs.next()){
                p.playbyplayid = rs.getInt("id");
            }
            parsePlayersOnIce(
                e,
                NumberUtils.toInt(gameId),//gameId, REPLACE ME BEFORE RUNNING
                homeTeam, //home
                visitingTeam, //away
                p.playbyplayid,
                p.actingplayername,
                p.actingteam,
                p.playnumber
                );
                    
                    
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        
        return p;
    }   
    
    private void parseAssists(String desc, String gameId, int playNumber, String timeElapsed, String shottype, String actingTeam, String strength, int period, int distance, int parentgoalid, int actingsituation){
        int assistsIndex = desc.indexOf("Assist");
        String assistStr = desc.substring(assistsIndex+9);
        
        String[] assists = assistStr.split(";");
        String actingName;
        
        int actingNum;
        for(int i = 0; i < assists.length; i ++){
        
        try{
        int hash = assists[i].indexOf("#");
        int open = assists[i].indexOf("(");
        
        if(hash < 0 || open < 0) break;
        actingNum = NumberUtils.toInt(assists[i].substring(hash+1, hash+3).trim());
            actingName = assists[i].substring(hash+3, open).trim();
        PlayByPlay p;
            p = new PlayByPlay(
                    NumberUtils.toInt(gameId), //gameid
                    playNumber, //play#
                    period, //period
                    strength,
                    timeElapsed, //Time Elapsed
                    "ASSIST",
                    desc, //Description
                    actingNum,   //actingplayernumber
                    actingName,    //actingplayername
                    actingTeam,   //actingteam
                    -2,   //againstplayernumber 
                    Integer.toString(parentgoalid),    //againstplayername - this is parent goal id
                    "",    //againstteam
                    "",    //zone
                    distance,    //distance
                    shottype,    //shottype
                    actingsituation,
                    possesion.getPossesion()
            );
            
            p.writeToDb();
            
            
       
        }catch(Exception e){
        System.out.println("Issue parsing Assist. Game #" +gameId + " " + assists[i]);
        }}
    }
    private String[] getTeams(Document doc){
        
    Elements e = doc.select("img");
    
    String a = e.get(0).attr("alt");
    String h = e.get(3).attr("alt");
    
    String sql = "SELECT teamcode FROM nhl.team WHERE team.teamname = '"+a+"'";
    
    try{
    
        ResultSet rs =DbUtilities.getResultSet(sql);
        
        if(rs.next()){
            a = rs.getString("teamcode");
            rs.close();
        }
        else{
        System.out.println("Issue getting team "+ a);
        }
        sql = "SELECT teamcode FROM nhl.team WHERE team.teamname = '"+h+"'";
        ResultSet rs2  = DbUtilities.getResultSet(sql);
        
        if(rs2.next()){
            h = rs2.getString("teamcode");
        }
        else{
        System.out.println("Issue getting team "+ h);
        }
        
        
    }catch(Exception ex){
    ex.printStackTrace();
    }
    
    
    
    
    
    String[] teams = {a, h};
    
    return teams;
    } 
    private void parsePlayersOnIce(Element row, int gameId, String homeTeam, String awayTeam, int playByPlayId, String actingPlayerLastName, String actingTeam, int playnumber){
       boolean goaliePulled; 
        try{
        Elements visitors = row.select("tbody").get(visitorOnIce).select("td > table").select("td");
        int homeOffset = homeOnIce - (6 - (visitors.size()/2));
        Elements home = row.select("tbody").get(homeOffset).select("td > table").select("td");//I feel this is incredibly fragile code. I'd like to find a more elegant way of getting this data
        
        String str;
        int number;
        int endOfName;
        Element curr;
        String position;
        String firstName;
        String lastName;
        String fullName;
        String[] names;
        boolean onActingTeam;
        boolean isActing;
        ArrayList<String> positionsAway = new ArrayList<>(5);
        ArrayList<String> positionsHome = new ArrayList<>(5);
        //boolean debug = playnumber == 301;
       // String title = "title=" + "\"";
        //if(!(visitors.get(visitors.size()-1).text().contains("G")) || !(home.get(home.size()-1).text().contains("G"))) goaliePulled = true;
        int dashIndex;
        //Now we should have 2 rows for each player. We'll loop through in groups of 2
        
      // if(debug){
            //System.out.println(visitors);
        //}
        for(int i = 0 ; i < visitors.size() -1; i = i+2){
            //long time = System.currentTimeMillis();
            curr = visitors.get(i);
            number = NumberUtils.toInt(curr.text());
            str = curr.toString();
            
            position = visitors.get(i+1).text();
            
            dashIndex = str.indexOf("-");
            endOfName = str.substring(dashIndex).indexOf(">")-1 + dashIndex;
            fullName = str.substring(dashIndex+1, endOfName).trim();
            //System.out.println(number + ": "+ fullName + " - " + position);
            
            names = fullName.split(" ");
            firstName = names[0].trim();
            lastName = names[1].trim();
            
            onActingTeam = awayTeam.equals(actingTeam);
            isActing = lastName.equals(actingPlayerLastName) && onActingTeam;
            positionsAway.add(position);
           // if(debug) System.out.println(!goaliePulled);
            //System.out.println(position);
            PlayerOnIce.writeToDbStatic(position, firstName, lastName, number, gameId, playByPlayId, awayTeam, isActing, onActingTeam);
           // System.out.println(endOfName);
            //if(i == 0) System.out.println("Parsed visitor...");
           // if(i == 0) System.out.println("AWAY: " + (System.currentTimeMillis() - time));
        }
        //if(true) return;
        //need two loops because powerplay
        for(int i =0; i<home.size()-1; i = i+2){
            //long time = System.currentTimeMillis();
            curr = home.get(i);
            number = NumberUtils.toInt(curr.text());
            str = curr.toString();
            
            position = home.get(i+1).text();
            positionsHome.add(position);
            dashIndex = str.indexOf("-");
            endOfName = str.substring(dashIndex).indexOf(">")-1 + dashIndex;
            fullName = str.substring(dashIndex+1, endOfName).trim();
            //System.out.println(number + ": "+ fullName + " - " + position);
            
            names = fullName.split(" ");
            firstName = names[0].trim();
            lastName = names[1].trim();
           // System.out.println(position);
            onActingTeam = homeTeam.equals(actingTeam);
            isActing = lastName.equals(actingPlayerLastName) && onActingTeam;
            //goaliePulled = position.trim().equals("G");
            PlayerOnIce.writeToDbStatic(position, firstName, lastName, number, gameId, playByPlayId, homeTeam, isActing, onActingTeam);
           // if(i == 0) System.out.println("HOME: " + (System.currentTimeMillis() - time));
            
        }
        //update main record to indicate goalie pulled
        
        goaliePulled = !positionsHome.contains("G") || !positionsAway.contains("G");
        
        if(goaliePulled){
                       // update = "UPDATE nhl.playbyplay SET goaliepulled = 1 WHERE playbyplayid =" + p.playbyplayid;
                        //DbUtilities.executeQuery(update);
            String update = "UPDATE nhl.playbyplay SET goaliepulled = 1 WHERE playbyplayid =" + playByPlayId;
            DbUtilities.executeQuery(update);
        }
      } catch(IndexOutOfBoundsException e){
        //System.out.println("IO Execption parsing players at " + playnumber);
        //e.printStackTrace();

        }catch(Exception ex){

       System.out.println("Error at: " + playByPlayId);
       ex.printStackTrace();
        }  
        
    }
}   

 
    
   
