package com.sick.dev.lib;

import java.util.ArrayList;
import java.util.Set;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SICKQuiz1 {
	
	@Test
	public void startServerStatsstics() throws Exception {
	
		Set<String> server = ServerConf.getNumberOfServers().keySet();
		if (ServerConf.getNumberOfServers().isEmpty()) {
			System.out.println("No servers found to perform the eoperation");
		} else {
			for (String ser : server) {
				ArrayList<String> serverDetails = ServerConf
						.getNumberOfServers().get(ser);
				System.out.println(serverDetails.get(0));
				System.out.println(serverDetails.get(1));
				//Statistics will be collected for 10 hours. This can be configurable.
				StartServerStatistics obj = new StartServerStatistics(serverDetails.get(0), serverDetails.get(1),serverDetails.get(2),10);
				new dataCollectionIntoFiles(serverDetails.get(0), serverDetails.get(1),serverDetails.get(2)).collectData();
				
				Thread tobj = new Thread(obj);
				tobj.start();
			}
		}
	}


}
