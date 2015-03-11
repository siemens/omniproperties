package com.siemens.oss.omniproperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siemens.oss.omniproperties.util.ReflectionUtil;


/**
 * This class calls a "command" in the currently active shell. The 'shellCommand' must
 * be given to the constructor of this class. Therefore this class can be used
 * for any kind of preprocessing steps outside of Java.    
 * 
 * @author Michel Tokic
 */
public final class ShellExecutor implements Runnable {
	
	private String shellCommand = "";
	
	private static final Logger LOG = LoggerFactory
			.getLogger(ShellExecutor.class);
	
	public ShellExecutor (String shellCommand) {
		this.shellCommand = shellCommand;
	}

	public void run() {
		try {
			// execute shell command
			LOG.info(" => executing command '" + this.shellCommand + "'");
			Process p = Runtime.getRuntime().exec(this.shellCommand);			
            
			// write shell output to console output
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line=reader.readLine();

            while (line != null) {    
            	LOG.info(line);
                line = reader.readLine();
            }
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
