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
import java.io.IOException;

import com.siemens.oss.omniproperties.ObjectBuilder;

/**
 * @author Markus Michael Geipel
 *
 */
public class ForcedDirectory implements ObjectBuilder<File> {

	final private File file;

	public ForcedDirectory(final String path) {
		this.file = new File(path);

	}

	public ForcedDirectory(final File dir, final String path) {
		this.file = new File(dir, path);
	}

	@Override
	public File build() throws IOException {
		if (!file.exists()) {
			if(!file.mkdirs()){
				throw new IOException("unable to create '" + file.getAbsolutePath() + "'");
			}
		}
		if (!file.isDirectory()) {
			throw new IllegalArgumentException("'" + file
					+ "' is not a directory");
		}

		return new File(file.getCanonicalPath());

	}
}
