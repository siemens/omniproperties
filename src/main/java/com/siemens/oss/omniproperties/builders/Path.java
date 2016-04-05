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

import java.io.IOException;
import java.nio.file.Paths;

import com.siemens.oss.omniproperties.ObjectBuilder;

/**
 * @author Markus Michael Geipel
 *
 */
public class Path implements ObjectBuilder<java.nio.file.Path> {

	final private java.nio.file.Path path;

	public Path(final String path) {
		this.path = Paths.get(path);
	}
	
	public Path(final String[] parts) {
		this.path = Paths.get("", parts);
	}
	

	@Override
	public java.nio.file.Path build() throws IOException {
		return path;
	}
}
