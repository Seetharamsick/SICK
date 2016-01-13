package com.sick.dev.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.JOptionPane;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

/**
 * This class is responsible/used to run the commands on Linux machine.
 * 
 * @author seethar1
 * 
 */
public class SSHClient {
	/** The host. */
	private String host;

	/** The user name. */
	private String userName;

	/** The password. */
	private String password;

	/**
	 * Instantiates a new port forwarding.
	 * 
	 * @param host
	 *            the host
	 * @param userName
	 *            the user name
	 * @param password
	 *            the password
	 */

	public SSHClient(String host, String userName, String password) {
		this.host = host;
		this.userName = userName;
		this.password = password;
	}

	/**
	 * Gets the host.
	 * 
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Gets the user name.
	 * 
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Exec.
	 * 
	 * @param command
	 *            the command
	 * @return the string
	 */
	public String exec(String command) throws Exception {
		JSch jsch = null;
		Session session = null;
		Channel channel = null;
		StringBuilder builder = new StringBuilder();
		int exitStatus = 0;
		try {
			jsch = new JSch();
			System.out.println(getHost() + "-" + getUserName() + "-"
					+ getPassword());
			System.out.println(command);
			session = jsch.getSession(getUserName(), getHost(), 22);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setPassword(getPassword().getBytes());
			session.setConfig(config);
			UserInfo ui = new MyUserInfo(getPassword());
			session.setUserInfo(ui);
			session.connect();
			channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(System.err);
			InputStream in = channel.getInputStream();
			channel.connect();
			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					String response = new String(tmp, 0, i);
					System.out.println(response);
					builder = builder.append(response + "\n");

				}
				if (channel.isClosed()) {
					System.out.println("exit-status: "
							+ channel.getExitStatus());
					exitStatus = channel.getExitStatus();
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (channel != null)
				channel.disconnect();
			if (session != null)
				session.disconnect();
		}
		if (exitStatus != 0) {

			throw new Exception("Command failed! Error message:\n"
					+ builder.toString().trim());
		}
		return builder.toString().trim();
	}

	public void sftp(String source, String destination) throws Exception {

		JSch jsch = new JSch();
	    Session session = jsch.getSession(this.userName, this.host, 22); //port is usually 22
	    session.setPassword(this.password);
	    session.setConfig("StrictHostKeyChecking", "no");
	    session.connect();
	    Channel channel = session.openChannel("sftp");
	    channel.connect();
	    ChannelSftp cFTP = (ChannelSftp) channel;
	    try {

	        cFTP.put(source , destination );
	    } catch (SftpException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }

	    cFTP.disconnect();
	    session.disconnect();
	}

	/**
	 * The Class MyUserInfo.
	 */
	public static class MyUserInfo implements UserInfo, UIKeyboardInteractive {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.jcraft.jsch.UserInfo#getPassword()
		 */
		public String getPassword() {
			return passwd;
		}

		/**
		 * Instantiates a new my user info.
		 * 
		 * @param password
		 *            the password
		 */
		public MyUserInfo(String password) {
			this.passwd = password;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.jcraft.jsch.UserInfo#promptYesNo(java.lang.String)
		 */
		public boolean promptYesNo(String str) {
			return true;
		}

		/** The passwd. */
		String passwd;

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.jcraft.jsch.UserInfo#getPassphrase()
		 */
		public String getPassphrase() {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.jcraft.jsch.UserInfo#promptPassphrase(java.lang.String)
		 */
		public boolean promptPassphrase(String message) {
			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.jcraft.jsch.UserInfo#promptPassword(java.lang.String)
		 */
		public boolean promptPassword(String message) {
			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.jcraft.jsch.UserInfo#showMessage(java.lang.String)
		 */
		public void showMessage(String message) {
			JOptionPane.showMessageDialog(null, message);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.jcraft.jsch.UIKeyboardInteractive#promptKeyboardInteractive(java
		 * .lang.String, java.lang.String, java.lang.String, java.lang.String[],
		 * boolean[])
		 */
		public String[] promptKeyboardInteractive(String destination,
				String name, String instruction, String[] prompt, boolean[] echo) {
			return null; // cancel
		}
	}

}
