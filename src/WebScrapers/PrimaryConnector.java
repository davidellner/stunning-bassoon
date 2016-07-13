/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package WebScrapers;

import static WebScrapers.HomepageConnector.ROW_OFFSET;
import static WebScrapers.HomepageConnector.TEAM_TOTAL_STR;
import java.sql.ResultSet;
import java.sql.SQLException;
import static WebScrapers.HomepageConnector.printPlayerRow;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class PrimaryConnector {
    
    public final static String START_YEAR = "2015";
    public final static String END_YEAR = "2016";
    public final static String BASE_URL = "http://www.nhl.com/scores/htmlreports/";
    public final static String EVENT_SUMMARY_TAG = "ES";
    public static String url = "";
    private String gameId;
    
    //Ideally we'll use the db to find the most recent gameid
    //020582 - first test value
    
    public PrimaryConnector( String gameId ){   
        this.gameId = gameId;
    }
    
    public static void parseEventSummary(String gameId) {
        
        try {
            fetchPlayerDataFromEventSummary(gameId);
        } catch (IOException ex) {
            System.out.println("Could not connect to " + url + " - IO Exception");
            System.out.println(ex);
        }
    }
    
    public static void main(String[] args) {
        //first game id of 2015/2016 is 020001
        int startgame = 21231; //20889;
        int lastgame = 21230;
        
        for(; startgame <= lastgame; startgame++){
            String gameId = '0' + Integer.toString(startgame);
            parseEventSummary(gameId);
        }
    }
    public static GameInfo getGameInfo(Document eventSummary, int gameId){
        Date date = null; // 3
        GameInfo g = null;
        String attendance; //4 
        String gameNumber; //6
        Elements gameInfoTbl = eventSummary.select("table [id=GameInfo]").select("td");
        
        
        date = Utilities.DateUtils.strToDate(gameInfoTbl.get(3).text());
        attendance = gameInfoTbl.get(4).text().split(" ")[1].replaceAll("[^0-9.]", "");
        gameNumber = gameInfoTbl.get(6).text().split(" ")[1];
        
        //System.out.println(attendance + " " + gameNumber);
        
        //int gameId, Date date, int attendance, int gameNumber
        g = new GameInfo(gameId, date, Integer.parseInt(attendance), Integer.parseInt(gameNumber));
        
        return g;
    }
    public static void fetchPlayerDataFromEventSummary(String gameId) throws IOException{
        Connection c;
        Document doc;
        Elements visitorPlayerHtml;
        Elements visitors;
        String visitingTeam;
        //Code to fetch from raw file (practice where there is no internet connection)
        //File file = new File("./NHL/testevent2.html");
        //doc = Jsoup.parse(file, "UTF-8");
        String URL = BASE_URL + START_YEAR + END_YEAR + "/" + EVENT_SUMMARY_TAG + gameId + ".HTM";   
            
            c = Jsoup.connect(URL);
            doc = c.timeout(0).get();
            System.out.println("Connected to URL : " + URL);
        
            
            
            visitors = doc.getElementsByTag("table");

            Element fullTable = visitors.get(18);
            String homeTeam = fullTable.getElementsByClass("homesectionheading").first().text();
            visitingTeam = fullTable.getElementsByClass("visitorsectionheading").first().text();
            
            //okay, so this is the whole table
            Elements allRows = fullTable.getElementsByTag("tr");
            
            String text;
            int targetIndex = 0;
            for(int i = 1; i < allRows.size(); i++){
                text = allRows.get(i).text();
                if(text.contains(TEAM_TOTAL_STR)){
                    targetIndex = i;
                    break;
                }
            }
            
            
           
            //We now have all rows. We need to extract player and team info.
           // visitor_str = headers.first().text();
            ArrayList<PlayerData> visitingteam = new ArrayList<PlayerData>();
            
            for(int i = 2; i < targetIndex; i++) {
                //ALL VISITOR PLAYERS
                String[] playerdata = printPlayerRow(allRows.get(i)).split(",");
                try{
                   PlayerData player = new PlayerData(
                    NumberUtils.toInt(playerdata[0]),
                    playerdata[1].trim().replace("'", "''"),
                    playerdata[2].trim().replace("'", "''"),
                    playerdata[3].trim().replace("'", "''"),
                    NumberUtils.toInt(playerdata[4]),
                    NumberUtils.toInt(playerdata[5].replace(" ","")),
                    NumberUtils.toInt(playerdata[6].replace(" ","")),
                    NumberUtils.toInt(playerdata[7].replace(" ","")),
                    NumberUtils.toInt(playerdata[8].replace(" ","")),
                    NumberUtils.toInt(playerdata[9].replace(" ","")),
                    playerdata[10].trim(),
                    NumberUtils.toInt(playerdata[11].replace(" ","")),
                    playerdata[12].trim(),
                    playerdata[13].trim(),
                    playerdata[14].trim(),
                    playerdata[15].trim(),
                    NumberUtils.toInt(playerdata[16].replace(" ","")),
                    NumberUtils.toInt(playerdata[17].replace(" ","")),
                    NumberUtils.toInt(playerdata[18].replace(" ","")),
                    NumberUtils.toInt(playerdata[19].replace(" ","")),
                    NumberUtils.toInt(playerdata[20].replace(" ","")),
                    NumberUtils.toInt(playerdata[21].replace(" ","")),
                    NumberUtils.toInt(playerdata[22].replace(" ","")),
                    NumberUtils.toInt(playerdata[23].replace(" ","")),
                    NumberUtils.toInt(playerdata[24].replace(" ","")),
                    NumberUtils.toInt(playerdata[25].replace(" ","")),
                    visitingTeam,
                    Integer.parseInt(gameId)
                );
                visitingteam.add(player);
                player.writeToDB();
            }catch(Exception e) {
                   System.out.println("Encountered an error. GameId: " + gameId); }}
            
            
            System.out.println(homeTeam + " vs. "+ visitingTeam);
            ArrayList<PlayerData> hometeam = new ArrayList<PlayerData>();
            for(int i = targetIndex+ROW_OFFSET; i < allRows.size(); i++){
                //ALL HOME PLAYERS
                String[] playerdata = printPlayerRow(allRows.get(i)).split(",");
                try{
                    PlayerData player = new PlayerData(
                    NumberUtils.toInt(playerdata[0]),
                    playerdata[1].trim().replace("'", "''"),
                    playerdata[2].trim().replace("'", "''"),
                    playerdata[3].trim().replace("'", "''"),
                    NumberUtils.toInt(playerdata[4]),
                    NumberUtils.toInt(playerdata[5].replace(" ","")),
                    NumberUtils.toInt(playerdata[6].replace(" ","")),
                    NumberUtils.toInt(playerdata[7].replace(" ","")),
                    NumberUtils.toInt(playerdata[8].replace(" ","")),
                    NumberUtils.toInt(playerdata[9].replace(" ","")),
                    playerdata[10].trim(),
                    NumberUtils.toInt(playerdata[11].replace(" ","")),
                    playerdata[12].trim(),
                    playerdata[13].trim(),
                    playerdata[14].trim(),
                    playerdata[15].trim(),
                    NumberUtils.toInt(playerdata[16].replace(" ","")),
                    NumberUtils.toInt(playerdata[17].replace(" ","")),
                    NumberUtils.toInt(playerdata[18].replace(" ","")),
                    NumberUtils.toInt(playerdata[19].replace(" ","")),
                    NumberUtils.toInt(playerdata[20].replace(" ","")),
                    NumberUtils.toInt(playerdata[21].replace(" ","")),
                    NumberUtils.toInt(playerdata[22].replace(" ","")),
                    NumberUtils.toInt(playerdata[23].replace(" ","")),
                    NumberUtils.toInt(playerdata[24].replace(" ","")),
                    NumberUtils.toInt(playerdata[25].replace(" ","")),
                    homeTeam,
                    Integer.parseInt(gameId)        
                );
                    player.writeToDB();
                    hometeam.add(player);
                } catch (Exception e){
                    System.out.println("Done!");
                    break;
                }
                
            }
            
         //Store some game event data here
            GameSummary game = new GameSummary(hometeam, visitingteam);
            game.writeToDb();
    }
}
