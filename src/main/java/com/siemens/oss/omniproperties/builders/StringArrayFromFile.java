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

package com.siemens.oss.omniproperties.builders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.siemens.oss.omniproperties.ObjectBuilder;

/**
 * @author Markus Michael Geipel
 *
 */
public class StringArrayFromFile implements ObjectBuilder<String[]> {

	private final File[] files;

	public StringArrayFromFile(final File... files) {
		this.files = files;
	}
	
	public StringArrayFromFile(final File file) {
		this.files = new File[]{file};
	}

	@Override
	public String[] build() throws IOException {
		final List<String> list = new ArrayList<>();
		for (File file : files) {
			final BufferedReader br = new BufferedReader(new FileReader(file));
			try {

				String line = br.readLine();

				while (line != null) {
					list.add(line);
					line = br.readLine();
				}

			} finally {
				br.close();
			}
		}
		return list.toArray(new String[list.size()]);
	}
}
