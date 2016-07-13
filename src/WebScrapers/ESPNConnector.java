/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebScrapers;

import Utilities.DateUtils;
import Utilities.DbUtilities;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author davidellner
 */
public class ESPNConnector {
    
    public static void main(String[] args){
        String sp = "call nhl.NHL_usp_UpdatePlayLocations()";
        int espngameid = 65739;
        //int gameid;
        for(; espngameid < 65740; espngameid++){
            
            writePlayLocationsByEspnId(espngameid);
            System.out.println("Processed ESPN Game #" + espngameid);
            System.gc();
        }
        // Call SP to join play locations
            //DbUtilities.executeQuery(sp);
    }
    
    private static void writePlayLocationsByEspnId(int espnGameId){
        String URL = "http://scores.espn.go.com/nhl/gamecast/data/masterFeed?lang=en&isAll=true&gameId=40081" + espnGameId;
        //String URL = "http://scores.espn.go.com/nhl/gamecast/data/masterFeed?lang=en&isAll=true&gameId=4008" + espnGameId;

        Document doc;
         
        try {
            doc = Jsoup.connect(URL).timeout(0).get();
            Elements game = doc.getElementsByTag("Game");
            String[] gameinfo = game.text().split("~");
            String date = DateUtils.strToDate(gameinfo[7]).toString();
            String home = gameinfo[22] + " " + gameinfo[23];
            String away = gameinfo[27] + " " + gameinfo[28];
            //System.out.println(date + " - " + home.toUpperCase() + " - " + away.toUpperCase());
            int gameid = getNhlGameId(date, home.toUpperCase());
            
            mapEspnGameIdGameId(espnGameId, gameid);
            
            Elements plays = doc.getElementsByTag("Play");
            int playcounter = 1;
            int x;
            int y;
            String time;
            String[] data;
            int period;
           
            String basesql = "INSERT INTO nhl.playlocation (espngameid, x, y, time, period, playnumber) VALUES (";
            String sql = "";
            for(Element p : plays){
                data = p.text().split("~");
                
                x = Integer.parseInt(data[0]);
                y = Integer.parseInt(data[1]);
                time = data[3];
                period = Integer.parseInt(data[4]);
                sql = basesql + Integer.toString(espnGameId) 
                              + ", " 
                              + Integer.toString(x)
                              + ", "
                              + Integer.toString(y)
                              + ", '"
                              + time
                              + "', "
                              + period 
                              + ", "
                              + playcounter
                              + ");";
                DbUtilities.executeQuery(sql);
                playcounter++;
            }
            
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private static void mapEspnGameIdGameId(int espngameid, int gameid){
        String sql = "INSERT INTO nhl.mapespngameidgameid VALUES (" + gameid + ", " + espngameid + ");";
        DbUtilities.executeQuery(sql);
    }
    public static int getNhlGameId(String date, String hometeam){
        int gameid = 0;
        String sql = "select " + 
                    "i.gameid " +
                    "from nhl.gameinfo i " +
                    "join nhl.gamesummary h on h.gameid = i.gameid and h.homeoraway = 'HOME' " +
                "where h.team = '" + hometeam + "' and i.gamedate = '" + date + "';";
        try{
            ResultSet rs = DbUtilities.getResultSet(sql);
            if(rs.next()) gameid = rs.getInt("gameid");
        } catch (SQLException ex) {
            
        }
        return gameid;
    }
}
