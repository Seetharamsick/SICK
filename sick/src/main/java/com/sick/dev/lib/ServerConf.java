package com.sick.dev.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServerConf {
	
	public static HashMap<String, ArrayList<String>> getNumberOfServers(){
		HashMap<String, ArrayList<String>> serverDetails = new HashMap<String, ArrayList<String>>();
		
		ArrayList<String> server1 = new ArrayList<String>();
		server1.add("52.91.9.191");
		server1.add("testuser");
		server1.add("etouch123");
		
		serverDetails.put("server1", server1);
		return serverDetails;
	}

}
