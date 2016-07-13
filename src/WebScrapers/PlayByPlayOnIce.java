/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package WebScrapers;

import Utilities.DbUtilities;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author davidellner
 */
public class PlayByPlayOnIce {
    int playbyplayid;
    String firstname;
    String lastname;
    int jerseynumber;
    
    public PlayByPlayOnIce(int playbyplayid,
                           String firstname,
                           String lastname,
                           int jerseynumber
                           ){
         playbyplayid = this.playbyplayid;
     firstname = this.firstname;
     lastname = this.lastname;
     jerseynumber = this.jerseynumber;
        
    }
    
    public void writeToDb(){
        String sql = "INSERT INTO `nhl`.`playbyplayonice`\n" +
"(" +
"`playbyplayid`,\n" +
"`firstname`,\n" +
"`lastname`,\n" +
"`jerseynumber`,\n" +
") VALUES (" + this.playbyplayid + ",'" + this.firstname + "','" + this.lastname + "'," + this.jerseynumber + ")";
        DbUtilities.executeQuery(sql);
 
    }
}
