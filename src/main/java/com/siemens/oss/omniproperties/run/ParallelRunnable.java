/*
 * Copyright Siemens AG, 2015
 *
 * Licensed under the Apache License, Version 2.0 the "License";
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.siemens.oss.omniproperties.run;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.sf.oval.constraint.Min;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siemens.oss.omniproperties.OmniProperties;

/**
 * Used to run oprops configs for an array typed property in parallel.
 * 
 * @author Markus Michael Geipel
 * 
 */

public final class ParallelRunnable implements Runnable {

	private final static Logger LOG = LoggerFactory.getLogger(ParallelRunnable.class);

	/**
	 * oprops to run
	 */
	@NotNull
	private File runOprops;
	
	/**
	 * items to iterate over
	 */
	@NotNull
	private Object[] items;
	/**
	 * name under which the item will be available in the run
	 */
	@NotNull
	@NotEmpty
	private String itemName;

	/**
	 * degree of paralellity. By default number of processors.
	 */
	@Min(1)
	private int parallelity = Runtime.getRuntime().availableProcessors();

	private final Map<String, Object> map = new HashMap<String, Object>();

	@Override
	public void run() {
		final ExecutorService threadExecutor = Executors.newFixedThreadPool(parallelity);

		try {
			List<Future<?>> futures = new ArrayList<>();
			for (Object item : items) {
				LOG.info("Scheduling run with " + itemName + "='" + item + "'; parallelity=" + parallelity);
				final OmniProperties simulationProperties = OmniProperties.create();
				simulationProperties.put(itemName, item);
				simulationProperties.putAll(map);
				try {
					simulationProperties.readFromFile(runOprops);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}

				final Runnable run = simulationProperties.getObject("run", Runnable.class);
				futures.add(threadExecutor.submit(run));
			}
			
		
			for (int i = 0; i < futures.size(); i++) {
				futures.get(i).get();
				LOG.info(i + " of " + futures.size() + " finished.");
			}

		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		} finally {
			threadExecutor.shutdown();
		}

	}

	public void put(String key, Object value) {
		map.put(key, value);
	}
}
