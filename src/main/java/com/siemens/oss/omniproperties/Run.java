/*
* Copyright Siemens AG, 2014
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

package com.siemens.oss.omniproperties;

import java.io.IOException;

/**
 * Generic runner for oprop files. Expects an object "run" implementing the {@link Runnable} interface.
 * 
 * @author Markus Michael Geipel
 *
 */
public final class Run {

	public final static String RUN_KEY = "run";

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println("Usage: java " + Run.class.getName() + " OPROPS_FILE");
			System.exit(-1);
		}

		OmniProperties.create().readFromFile(args[0]).getObject(RUN_KEY, Runnable.class).run();
	}
}
