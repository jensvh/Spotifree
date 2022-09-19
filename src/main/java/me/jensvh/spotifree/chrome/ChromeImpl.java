package me.jensvh.spotifree.chrome;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.jna.platform.win32.Crypt32Util;

import me.jensvh.spotifree.Main;
import me.jensvh.spotifree.utils.SqLite;

public class ChromeImpl {

	// Chrome can have profiles, search for them
	private static final String base_profile_url = System.getProperty("user.home") + "/AppData/Local/Google/Chrome/User Data/Default/network/Cookies";
    private static final String local_state_url = System.getProperty("user.home") + "/AppData/Local/Google/Chrome/User Data/Local State";
	
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
    		File folder = new File(base_profile_url);
    		if (!folder.exists()) {
    			System.out.println("No chrome installation found!");
    			return null;
    		}
    		
    		SqLite.connectToCookieDatabase(base_profile_url);
    		byte[] master = getMasterKey();
    		
    		StringBuilder cookies = new StringBuilder();
    		
    		for (String cookie : necessaryCookies) {
    			String c = getAndDecryptCookie(cookie, master);
    			if (Main.debugging)
            		System.out.println("Chrome cookie, name: " + cookie + ", value: " + c);
    			if (c == null)
    				return null;
    			cookies.append(c);
            }
            
            SqLite.disconnect();
    		
    		return cookies.toString();
    	} catch (Exception ex) {
    		if (Main.debugging)
    			ex.printStackTrace();
    		System.out.println("Unable to load spotify's cookies.");
    		System.exit(25);
    	}
    	return null;
    }
    
    private static String getAndDecryptCookie(String name, byte[] master_key) throws SQLException, JsonIOException, JsonSyntaxException, FileNotFoundException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
    	String query = "SELECT name, encrypted_value, host_key FROM cookies WHERE name LIKE \"" + name + "\"";
        
        ResultSet result = SqLite.query(query);
        
        while (result.next()) {
            if (!result.getString("host_key").contains("spotify"))
                continue;
            byte[] cookie = result.getBytes("encrypted_value");
            return name + "=" + new String(decryptCookie(cookie, master_key)) + "; ";
        }
        return null;
    }
    
    private static byte[] getMasterKey() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
    	JsonObject jsonObjectLocalState = (JsonObject) JsonParser.parseReader(new FileReader(local_state_url));
    	String encryptedMasterKeyWithPrefixB64 = jsonObjectLocalState.getAsJsonObject("os_crypt").get("encrypted_key").getAsString();
    	// Remove praefix (DPAPI)
    	byte[] encryptedMasterKeyWithPrefix = Base64.getDecoder().decode(encryptedMasterKeyWithPrefixB64);
    	byte[] encryptedMasterKey =  Arrays.copyOfRange(encryptedMasterKeyWithPrefix, 5, encryptedMasterKeyWithPrefix.length);
    	// Decrypt
    	return Crypt32Util.cryptUnprotectData(encryptedMasterKey);
    }
    
    private static byte[] decryptCookie(byte[] cookie, byte[] masterKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
    	byte[] nonce = Arrays.copyOfRange(cookie, 3, 3 + 12);
    	byte[] ciphertextTag = Arrays.copyOfRange(cookie, 3 + 12, cookie.length);
    	// Decrypt
    	Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
    	GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, nonce);
    	SecretKeySpec keySpec = new SecretKeySpec(masterKey, "AES");
    	cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);
    	return cipher.doFinal(ciphertextTag);
    }
}
