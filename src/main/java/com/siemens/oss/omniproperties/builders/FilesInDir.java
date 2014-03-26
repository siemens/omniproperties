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

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import com.siemens.oss.omniproperties.ObjectBuilder;

/**
 * @author Markus Michael Geipel
 *
 */
public class FilesInDir implements ObjectBuilder<File[]> {

	final private File dir;
	private String suffix;

	
	public FilesInDir(final File dir) {
		this.dir = dir;
	}
	
	public FilesInDir(final String path) {
		this.dir = new File(path);
	}

	public FilesInDir(final File dir, final String path) {
		this.dir = new File(dir, path);
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	@Override
	public File[] build() throws IOException {
		if (!dir.exists()) {
			throw new IllegalArgumentException("Directory '" + dir
					+ "' does not exist");
		}
		if (!dir.isDirectory()) {
			throw new IllegalArgumentException("'" + dir
					+ "' is not a directory");
		}

		final File[] files = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(suffix);
			}
		});

		return files;

	}
}
