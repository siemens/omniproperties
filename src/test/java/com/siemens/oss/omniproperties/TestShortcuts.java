package com.siemens.oss.omniproperties;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

public class TestShortcuts {
	@Test
	public void testShortcuts() throws IOException{
		Properties classShortcuts = new Properties();
	
			Enumeration<?> enumeration = Thread.currentThread().getContextClassLoader().getResources("omniproperties-class-shortcuts.properties");

			while (enumeration.hasMoreElements()) {
				URL url = (URL) enumeration.nextElement();
				classShortcuts.load(url.openStream());
			}
			
			for(Entry<Object, Object> entry : classShortcuts.entrySet()){
				try{
				this.getClass().getClassLoader().loadClass((String)entry.getValue());
				}catch(ClassNotFoundException e){
					Assert.fail("Broken class shortcut for " + entry.getKey() + " -> " + entry.getValue());
				}
			}
	}
}
