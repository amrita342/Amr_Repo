package com.test;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import com.crestech.opkey.plugin.KeywordLibrary;
import com.crestech.opkey.plugin.ResultCodes;
import com.crestech.opkey.plugin.communication.contracts.functionresult.FunctionResult;
import com.crestech.opkey.plugin.communication.contracts.functionresult.Result;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class RunCommandOnRemote implements KeywordLibrary {

	public static void main(String[] args) throws JSchException, SftpException,
			IOException {
		
		
		//executeCommandOnRemote("admin","Password1", "172.17.2.66", 22, "ls");
	}

	public  FunctionResult executeCommandOnRemote(String username,
			String password, String hostName, int port, String command) {
		String output = "";
		try {
		output = executeLinuxCommand(username, password, hostName, port,command);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.FAIL(ResultCodes.ERROR_UNHANDLED_EXCEPTION)
					.setMessage(e.getMessage()).setOutput(false).make();
		}

		return Result.PASS().setOutput(output).make();
	}

	public  String executeLinuxCommand(String username, String password, String hostName,
		int port, String command) throws Exception {
		String str = null;
		
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");

	
		//String command1 = "Command";

		// create a object JSCH class
		JSch jsch = new JSch();
		// establish a connection to linux machine by providing username and
		// hostname
		Session session = jsch.getSession(username, hostName, port);
		// set passward
		session.setPassword(password);
		session.setConfig(config);
		session.connect();
		// use execute channel to execute linuc cammond
		// Channel channel = session.openChannel("sftp");
		Channel channel = session.openChannel("exec");
		System.out.println("Connected");
		((ChannelExec) channel).setCommand(command);


		// read data from the channel
		InputStream in = channel.getInputStream();
		channel.connect();
		byte[] tmp = new byte[1024];
		while (true) {
			while (in.available() > 0) {
				int i = in.read(tmp, 0, 1024);
				if (i < 0)
					break;
				str = str + new String(tmp, 0, i);

			}
			if (channel.isClosed()) {
				System.out.println("exit-status: " + channel.getExitStatus());
				break;
			}

		}
		channel.disconnect();
		session.disconnect();

		System.out.println(str);
		//System.out.println("DONE");
		return str;

	}

}
	
	
	
