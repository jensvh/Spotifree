package me.jensvh.spotifree.firefox;

import java.sql.ResultSet;
import java.sql.SQLException;

import me.jensvh.spotifree.utils.SqLite;

public class FirefoxImpl {
    
    // TODO: search for this url automatically 
    // TODO: Change direct path to be universal!
    private static final String profileUrl = "C:/Users/ikke/AppData/Roaming/Mozilla/Firefox/Profiles/74qv00yq.default-release";
    private static final String[] necessaryCookies = {
            "sp_t", 
            "OptanonConsent",
            "OptanonAlertBoxClosed",
            "eupubconsent-v2",
            "sp_m",
            "_scid",
            "sp_dc",
            "sp_key",
            "sp_landing"
    };
    
    public static String getCookies() throws SQLException {
        StringBuilder cookies = new StringBuilder();
        
        SqLite.connectToCookieDatabase(profileUrl + "/cookies.sqlite");
        
        for (String cookie : necessaryCookies) {
            cookies.append(getCookie(cookie));
        }
        
        SqLite.disconnect();
        
        return cookies.toString();
    }
    
    private static String getCookie(String name) throws SQLException {
        String query = "SELECT name, value, host FROM moz_cookies WHERE name LIKE \"" + name + "\"";
        
        ResultSet result = SqLite.query(query);
        
        while (result.next()) {
            if (!result.getString("host").contains("spotify"))
                continue;
            return name + "=" + result.getString("value") + "; ";
        }
        return "";
    }
}
