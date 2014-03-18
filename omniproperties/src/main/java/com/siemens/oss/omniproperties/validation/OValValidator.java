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

package com.siemens.oss.omniproperties.validation;

import java.util.List;

import com.siemens.oss.omniproperties.OmniProperties;
import com.siemens.oss.omniproperties.Validator;
import com.siemens.oss.omniproperties.exceptions.ValidationException;

import net.sf.oval.ConstraintViolation;

/**
 * {@link Validator} for {@link OmniProperties} which uses
 * <a href="http://oval.sourceforge.net/">OVal</a> as validation framework. It is the default
 * validator for {@link OmniProperties}.
 * 
 * @author Markus Michael Geipel
 * 
 */
public final class OValValidator implements Validator {

	private final net.sf.oval.Validator validator = new net.sf.oval.Validator();

	@Override
	public void validate(Object obj) {
		final List<ConstraintViolation> violations = validator.validate(obj);
		if (!violations.isEmpty()) {
			throw new ValidationException(obj, violations.toString());
		}

	}

}
