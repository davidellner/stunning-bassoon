/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nhl;

import WebScrapers.PlayByPlayConnector;
import static WebScrapers.PrimaryConnector.parseEventSummary;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author davidellner
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
            int startgame = 20720; //get single game
            String gameId = '0' + Integer.toString(startgame);
            
            parseEventSummary(gameId);
            
            PlayByPlayConnector p = new PlayByPlayConnector(startgame);
        try {
            p.fetchPlayByPlay();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        
    }
    
    
}
