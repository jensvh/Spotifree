package me.jensvh.spotifree.firefox;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.jensvh.spotifree.Main;
import me.jensvh.spotifree.utils.SqLite;

public class FirefoxImpl {
    
    private static final String base_profile_url = System.getenv("APPDATA") + "\\Mozilla\\Firefox\\Profiles";
    private static final String[] necessaryCookies = {
            "sp_t", 
            "OptanonConsent",
            "OptanonAlertBoxClosed",
            "eupubconsent-v2",
            "sp_dc",
            "sp_key",
            "sp_landing"
    };
    
    public static String getCookies() {
        try {
            String profileFolder = findFirefoxProfile();
            if (profileFolder == null) {
                System.out.println("Could not locate a firefox installation, or an active spotify sessions.");
                return null;
            }
            
            StringBuilder cookies = new StringBuilder();
            
            SqLite.connectToCookieDatabase(profileFolder + "/cookies.sqlite");
            
            for (String cookie : necessaryCookies) {
            	String c = getCookie(cookie);
            	if (Main.debugging)
            		System.out.println("Firefox cookie, name: " + cookie + ", value: " + c);
            	if (c == null)
            		return null;
                cookies.append(c);
            }
            
            SqLite.disconnect();
            
            return cookies.toString();
        } catch (SQLException e) {
            System.out.println("An error ocurred while finding spotify's cookies.");
            e.printStackTrace();
            System.exit(21);
        }
        return null;
    }
    
    private static String getCookie(String name) throws SQLException {
        String query = "SELECT name, value, host FROM moz_cookies WHERE name LIKE \"" + name + "\"";
        
        ResultSet result = SqLite.query(query);
        
        while (result.next()) {
            if (!result.getString("host").contains("spotify"))
                continue;
            return name + "=" + result.getString("value") + "; ";
        }
       
        return null;
    }
    
    private static String findFirefoxProfile() {
        File folder = new File(base_profile_url);
        for (File profileFolder : folder.listFiles()) {
            if (!profileFolder.isDirectory()) continue;
            for (String files : profileFolder.list()) {
                if (files.contains("cookies.sqlite")) {
                    return base_profile_url + "//" + profileFolder.getName();
                }
            }
        }
        return null;
    }
}
