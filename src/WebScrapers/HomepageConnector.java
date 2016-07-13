/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package WebScrapers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**

/**
 *
 * @author davidellner
 */
public class HomepageConnector {
    public static String TEAM_TOTAL_STR = "TEAM TOTALS";
    public static int ROW_OFFSET = 5;
    public static void fetchPlayerDataFromEventSummary(String URL) throws IOException{
        Connection c;
        Document doc;
        Elements visitorPlayerHtml;
        Elements visitors;
        String visitingTeam;
        //Code to fetch from raw file (practice where there is no internet connection)
        //File file = new File("./NHL/testevent2.html");
        //doc = Jsoup.parse(file, "UTF-8");
            
            
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
                PlayerData player = new PlayerData(
                    NumberUtils.toInt(playerdata[0]),
                    playerdata[1].trim(),
                    playerdata[2].trim(),
                    playerdata[3].trim(),
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
                        0
                );
                visitingteam.add(player);
                System.out.println(player);
            }
            System.out.println(homeTeam + " vs. "+ visitingTeam);
            ArrayList<PlayerData> hometeam = new ArrayList<PlayerData>();
            for(int i = targetIndex+ROW_OFFSET; i < allRows.size(); i++){
                //ALL HOME PLAYERS
                String[] playerdata = printPlayerRow(allRows.get(i)).split(",");
                try{
                    PlayerData player = new PlayerData(
                    NumberUtils.toInt(playerdata[0]),
                    playerdata[1].trim(),
                    playerdata[2].trim(),
                    playerdata[3].trim(),
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
                            0
                );
                    System.out.println(player);
                } catch (Exception e){
                    System.out.println("Done!");
                    break;
                }
                
            }
            
    }
    public static void main(String[] args) throws IOException{
        Connection c;
        String URL;
        Document doc;
        Elements visitorPlayerHtml;
        Elements visitors;
        String visitingTeam;
        //Code to fetch from raw file (practice where there is no internet connection)
        File file = new File("./NHL/testevent2.html");
        doc = Jsoup.parse(file, "UTF-8");
            
            /*URL = "http://www.nhl.com/scores/htmlreports/20152016/ES020261.HTM";
            c = Jsoup.connect(URL);
            doc = c.timeout(0).get();
            System.out.println("Connected to URL : " + URL);*/
        
            
            
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
                PlayerData player = new PlayerData(
                    NumberUtils.toInt(playerdata[0]),
                    playerdata[1].trim(),
                    playerdata[2].trim(),
                    playerdata[3].trim(),
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
                        0
                );
                visitingteam.add(player);
                System.out.println(player);
            }
            System.out.println(homeTeam + " vs. "+ visitingTeam);
            ArrayList<PlayerData> hometeam = new ArrayList<PlayerData>();
            for(int i = targetIndex+ROW_OFFSET; i < allRows.size(); i++){
                //ALL HOME PLAYERS
                String[] playerdata = printPlayerRow(allRows.get(i)).split(",");
                try{
                    PlayerData player = new PlayerData(
                    NumberUtils.toInt(playerdata[0]),
                    playerdata[1].trim(),
                    playerdata[2].trim(),
                    playerdata[3].trim(),
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
                            0
                );
                    System.out.println(player);
                } catch (Exception e){
                    System.out.println("Done!");
                    break;
                }
                
            }
            
    }
    public static String printPlayerRow(Element playerElement){
        String formattedStr = "";
        Elements rows = playerElement.select("td");
        for(Element e: rows){ 
            formattedStr += ',' + e.text();
        }
        
        return formattedStr.substring(1);
    }
    
}
