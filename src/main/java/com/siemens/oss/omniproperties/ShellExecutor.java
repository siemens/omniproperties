package com.siemens.oss.omniproperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * This class calls a "command" in the currently active shell. The 'shellCommand' must
 * be given to the constructor of this class. Therefore this class can be used
 * for any kind of preprocessing steps outside of Java.    
 * 
 * The output of the shell is written to STDOUT. 
 * @author Michel Tokic
 */
public final class ShellExecutor implements Runnable {
	
	private String shellCommand = "";
	
	public ShellExecutor (String shellCommand) {
		this.shellCommand = shellCommand;
	}

	public void run() {
		try {
			// execute shell command
			System.out.println (" => executing command '" + this.shellCommand + "'");
			Process p = Runtime.getRuntime().exec(this.shellCommand);			
            
			// write shell output to console output
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line=reader.readLine();

            while (line != null) {    
                System.out.println(line);
                line = reader.readLine();
            }
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
