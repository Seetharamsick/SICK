package com.sick.dev.lib;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * This class is responsible/used to run the commands on Linux machine.
 * 
 * @author seethar1
 * 
 */
class StartServerStatistics implements Runnable {
	String host = null;
	String userName = null;
	String pass = null;
	int hours = 0;

	public StartServerStatistics(String hostName, String user, String password,int noOfHours) {
		this.host = hostName;
		this.userName = user;
		this.pass = password;
		this.hours = noOfHours;
		
	}

	public void run() {
	    try {

	        int cutOfWaitTime = 0;
	        int sleepCount = 300;
	        int maxCount = this.hours/sleepCount;
	        
	        try {
	            while (true) {
	                fetchStatisticsAndUpdatetheTable();
	                /* Terminating the loop when it reaches maximum time 1 Hour */
	                TimeUnit.SECONDS.sleep(sleepCount);
	                cutOfWaitTime++;
	                
	                if (cutOfWaitTime >= maxCount) {
	                    throw new RuntimeException("Exiting after monitoring for " + (maxCount * sleepCount) + " seconds");
	                }
	            }
	        } catch (Exception ignored) {

	        }

	    } catch (Exception ignored) {
	    }

	}
	public void fetchStatisticsAndUpdatetheTable(){
		try {

			Statement jdbcConnector = new JDBCConnector(
					"jdbc:mysql://localhost/mysql", "root", "password")
					.getJDBCCOnnector();

			SSHClient sshClinet = new SSHClient(this.host, this.userName,
					this.pass);
			String memoryInUse = sshClinet
					.exec("free | grep Mem | awk '{print $3/$2 * 100.0}'");
			String freeMemory = sshClinet
					.exec("free | grep Mem | awk '{print $4/$2 * 100.0}'");
			System.out.println("memory In Use: " + memoryInUse);
			System.out.println("Free Memory: " + freeMemory);

			String swapMemoryInUse = sshClinet
					.exec("free | grep Swap | awk '{print $2}'");
			String swapFreeMemory = sshClinet
					.exec("free | grep Swap | awk '{print $3}'");
			System.out.println("Swap In Use: " + swapMemoryInUse);
			System.out.println("Free swap memory: " + swapFreeMemory);

			String diskSpaceAvailble = sshClinet
					.exec("df -h --total | grep total | awk '{print $4}'");
			String disSpaceUsed = sshClinet
					.exec("df -h --total | grep total | awk '{print $3}'");
			System.out.println("Disk Space Available: " + diskSpaceAvailble);
			System.out.println("disk Space USed: " + disSpaceUsed);

			String inodesAvailble = sshClinet
					.exec("df -i --total | grep total | awk '{print $4}'");
			String inodesUsed = sshClinet
					.exec("df -i --total | grep total | awk '{print $3}'");
			System.out.println("Inodes Available : " + inodesAvailble);
			System.out.println("Inodes Used: " + inodesUsed);

			String cpuUtilization = sshClinet
					.exec("top -bn1 | grep \"Cpu(s)\" | sed \"s/.*, *\\([0-9.]*\\)%* id.*/\1/\" | awk '{print 100 - $1\"%\"}'");
			System.out.println("CPU Utilization: " + cpuUtilization);

			String myTableName = "CREATE TABLE IF NOT EXISTS SERVER_STATISTICS_TABLE2 ( MEMORYINUSE VARCHAR(20),"
					+ "FREEMEMORY VARCHAR(20),"
					+ "SWAPMEMORYINUSE VARCHAR(20),"
					+ "SWAPFREEMEMORY VARCHAR(20),"
					+ "DISKSPACEAVAILABLE VARCHAR(20),"
					+ "DISKSPACEUSED VARCHAR(20),"
					+ "INODESAVAILABLE VARCHAR(20),"
					+ "INODESUSED VARCHAR(20)," + "CPUUTILIZATION VARCHAR(20))";
			jdbcConnector.executeUpdate(myTableName);
			String sql = "INSERT INTO SERVER_STATISTICS_TABLE2 VALUES ('"
					+ memoryInUse + "','" + freeMemory + "','"
					+ swapMemoryInUse + "','" + swapFreeMemory + "','"
					+ diskSpaceAvailble + "','" + disSpaceUsed + "','"
					+ inodesAvailble + "','" + inodesUsed + "','"
					+ cpuUtilization + "')";
			System.out.println(sql);
			jdbcConnector.executeUpdate(sql);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
