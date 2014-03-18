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

package com.siemens.oss.omniproperties.exceptions;

import java.util.Collection;
import java.util.List;

/**
 * @author Markus Michael Geipel
 *
 */
public class InjectionException extends Exception {
	private static final long serialVersionUID = 1L;

	public InjectionException() {
		super();
	}

	public InjectionException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public InjectionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InjectionException(String arg0) {
		super(arg0);
	}

	public InjectionException(Throwable arg0) {
		super(arg0);
	}
	
	public static class CollectedOmniPropExceptions extends InjectionException {
		private static final long serialVersionUID = -1191843376305223865L;
		private final List<InjectionException> collection;
		public CollectedOmniPropExceptions(List<InjectionException> ope) {
			super(collect(ope));
			collection = ope;
		}

		private static String collect(List<InjectionException> opes) {
			StringBuffer sb = new StringBuffer("InjectionException(s) occured:\n");
			for(InjectionException ope: opes) {
				sb.append( ope.getMessage() ).append("\n");
			}
			return sb.toString();
		}
		
		public Collection<InjectionException> getExceptions() {
			return collection;
		}
	}
}
