package com.telesign.example.phoneid;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;

import com.telesign.PhoneIdClient;
import com.telesign.RestClient.TelesignResponse;

public class Addon {
	
	@SuppressWarnings("unchecked")
	public static void main (String[] args) {
		String customerId = "FFFFFFFF-EEEE-DDDD-1234-AB1234567890";
	    String apiKey = "EXAMPLE----TE8sTgg45yusumoN6BYsBVkh+yRJ5czgsnCehZaOYldPJdmFh6NeX8kunZ2zU1YWaUw/0wV6xfw==";
	    
		String phoneNumber = "phone_number";
		
		PhoneIdClient client = new PhoneIdClient(
		        customerId, 
		        apiKey);
		    try {
		      HashMap<String, Object> map = new HashMap<String, Object>();
		      map.put("addons", new HashMap<String, Object>());
		      ((HashMap<String, Object>)map.get("addons")).put("contact", new HashMap<String, Object>());
		      ((HashMap<String, Object>)map.get("addons")).put("live", new HashMap<String, Object>());
		      TelesignResponse response = client.phoneid(phoneNumber, map); // 14329345827
		      System.out.println(response.json);
		    } catch (IOException | GeneralSecurityException e) {
		      // TODO Auto-generated catch block
		      e.printStackTrace();
		    }
	}
	
	    
}
