package com.sick.dev.lib;

public class dataCollectionIntoFiles {
	String host = null;
	String userName = null;
	String pass = null;

	public dataCollectionIntoFiles(String hostName, String user, String password) {
		this.host = hostName;
		this.userName = user;
		this.pass = password;
	}

	public void collectData() throws Exception{
		
		SSHClient sshClinet = new SSHClient(this.host, this.userName,
			this.pass);

		sshClinet.exec("/sbin/ifconfig | grep \"inet addr:\" | head -1 >> /tmp/logs/Step1.out");
	    sshClinet.exec("hostname >> /tmp/logs/Step1.out");
	    sshClinet.exec("uptime >> /tmp/logs/Step1.out");
	    sshClinet.exec("date >> /tmp/logs/Step1.out");
	    sshClinet.exec("who | cut -d' ' -f1 | sort | uniq >> /tmp/logs/Step1.out");
	    
	    sshClinet.exec("free | grep Mem | awk '{print $3/$2 * 100.0}' >> /tmp/logs/Step2.out");
		sshClinet.exec("free | grep Mem | awk '{print $4/$2 * 100.0}' >> /tmp/logs/Step2.out");
		
		sshClinet.exec("free | grep Swap | awk '{print $2}' >> /tmp/logs/Step2.out");
		sshClinet.exec("free | grep Swap | awk '{print $3}' >> /tmp/logs/Step2.out");
		
		sshClinet.exec("df -h --total | grep total | awk '{print $4}' >> /tmp/logs/Step2.out");
		sshClinet.exec("df -h --total | grep total | awk '{print $3}' >> /tmp/logs/Step2.out");
		
		sshClinet.exec("df -i --total | grep total | awk '{print $4}' >> /tmp/logs/Step2.out");
		sshClinet.exec("df -i --total | grep total | awk '{print $3}' >> /tmp/logs/Step2.out");
		
		sshClinet.exec("top -bn1 | grep \"Cpu(s)\" | sed \"s/.*, *\\([0-9.]*\\)%* id.*/\1/\" | awk '{print 100 - $1\"%\"}' >> /tmp/logs/Step2.out");
		
	    sshClinet.exec("cat /var/log/boot.log | grep \"FAIL\" >>  /tmp/logs/Step3.out");
	    sshClinet.exec("cat /var/log/messages |grep -e error -e abrt >> /tmp/logs/Step4.out" );
	    sshClinet.exec("gzip -c /tmp/logs/* > /tmp/logs.gz");
	    sshClinet.sftp("/tmp/logs.gz", "c:\\");
	}
	
}
