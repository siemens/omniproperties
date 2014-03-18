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

import net.sf.oval.constraint.NotNull;

/**
 * @author Markus Michael Geipel
 *
 */
public final class Bean{
	
	@NotNull private String a;
	private String beee;
	private String c;
	private String x;
	
	public Bean(String x) {
		this.x = x;
	}
	
	public Bean() {
		
	}
	
	public String getA() {
		return a;
	}
	public void setA(String a) {
		this.a = a;
	}
	public String getB() {
		return beee;
	}
	public void setB(String b) {
		this.beee = b;
	}
	
	public String getC() {
		return c;
	}
	
	public String getX() {
		return x;
	}
}