package com.siemens.oss.omniproperties;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LoggingOverserver extends AOmniPropertyObserver {
	 
	
	private final Logger logger;
	
	public static OmniProperties wrap(final OmniProperties properties, final Logger logger){
		return  ObservableOmniProperties.wrap(properties, new LoggingOverserver(logger));
	}
	
	public static OmniProperties wrap(final OmniProperties properties){
		return  ObservableOmniProperties.wrap(properties, new LoggingOverserver());
	}
	
	public LoggingOverserver(final Logger logger) {
		this.logger = logger;
	}
	
	public LoggingOverserver() {
		this.logger = LoggerFactory.getLogger(LoggingOverserver.class);
	}
	
	public void clear() {
		logger.info("clear()");
	}
	

	public void put(String key, Object value) {
		logger.info("put({},{})", key, value);

	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		logger.info("putAll({})", m);

	}

	public void readFromStream(InputStream input) {
		logger.debug("read(stream)");

	}

	public void readFromFile(File file) {
		logger.info("read('{}')", file);

	}

	public void readFromResource(String string)  {
		logger.info("readFromResource('{}')", string);

	}

	public void readFromFile(String string) {
		logger.info("readFromFile('{}')", string);

	}
	
	public void readFromUrl(URL url) {
		logger.info("readFromUrl('{}')", url);
	}
	
	public void readFromString(String string) {
		logger.info("readFromString('{}')", string);
	}

	public void remove(Object key) {
		logger.info("remove('{}')", key);
	}

	@Override
	public void setOmniproperties(OmniProperties properties) {
	
	}
}
