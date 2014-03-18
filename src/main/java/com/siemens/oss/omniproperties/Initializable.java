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

/**
 * After creating an object, Omniproperties calls the setters, and finally the
 * <code>init()</code> method if the {@link Initializable} interface is
 * implemented
 * 
 * @author Markus Michael Geipel
 * 
 */
public interface Initializable {
	void init();
}
