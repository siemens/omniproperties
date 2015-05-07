package com.siemens.oss.omniproperties.run;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.siemens.oss.omniproperties.OmniProperties;

/**
 * Generic main class for oprops files. A variable <code>run</code> of type
 * {@link Runnable} is expected to be part of the {@link OmniProperties} config.
 * The runner just reads the config and invokes <code>run()</code> on
 * <code>run</code>. 
 * 
 * @author Markus Michael Geipel
 * 
 */

public final class GenericMain implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(GenericMain.class);
	
	@Parameter(names = "-args", description = "List of key-value pairs coded as KEY=VALUE KEY=VALUE etc.", variableArity = true)
	public List<String> args = new ArrayList<String>();
	
	@Parameter(names = "-config", description = "Omniproperties file", required = true)
	private File config;
	
	
	@Parameter(names = "-help", help = true)
	private boolean help;

	public static void main(String[] args) throws IOException {
		final GenericMain run = new GenericMain(args);
		try {
			run.run();
		} catch (Exception e) {
			LOG.error("unhandled exception", e);
			System.exit(-1);
		}
	}

	@Override
	public void run() {
		final OmniProperties properties = OmniProperties.create();
		properties.put("BASE_DIR", config.getAbsoluteFile().getParentFile());
		for(String arg:args){
			String[] parts = arg.split("=");
			if(parts.length!=2){
				throw new IllegalArgumentException("arguments must be given as -args [KEY=VALUE]*");
			}
			properties.put(parts[0], parts[1]);
		}

		try {
			properties.readFromFile(config);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		properties.getObject("run", Runnable.class).run();
	}

	public GenericMain(String[] args) {
		final JCommander jcom = new JCommander(this, args);
		if (help) {
			jcom.usage();
			System.exit(0);
		}
	}
}
