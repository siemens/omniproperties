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
tree grammar OmniPropertiesBuilder;

options {
  language     = Java;
  tokenVocab   = OmniProperties;
  ASTLabelType = CommonTree;
}

@header {
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

package com.siemens.oss.omniproperties.parser;

import com.siemens.oss.omniproperties.exceptions.*;
import com.siemens.oss.omniproperties.util.*;
import com.siemens.oss.omniproperties.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Collections;


import java.io.File;
import java.io.InputStream;
import java.net.URL;
}

@members {
private OmniProperties properties = OmniProperties.create();

public final void setProperties(final OmniProperties properties) {
	this.properties = properties;
}

public final OmniProperties getProperties() {
	return properties;
}

@Override
protected Object recoverFromMismatchedToken(IntStream input, int ttype,
		BitSet follow) throws RecognitionException {
	throw new MismatchedTokenException(ttype, input);
}

@Override
public Object recoverFromMismatchedSet(IntStream input, RecognitionException e,
		BitSet follow) throws RecognitionException {
	throw e;
}
}

@rulecatch {
catch(RecognitionException ex) {
        throw ex;
   }
}

parse
  :
  ^(ROOT statement*)
  ;

statement
  :
  assignement
  | include
  ;

include
  :
  ^(INCLUDE e=expression)
  
   {
    try {
    	if ($e.value instanceof File) {
    		properties.readFromFile((File) $e.value);
    	} else if ($e.value instanceof InputStream) {
    		properties.readFromStream((InputStream) $e.value);
    	} else if ($e.value instanceof URL) {
    		properties.readFromUrl((URL) $e.value);
    	} else if ($e.value instanceof String) {
    		properties.readFromFile((String) $e.value);
    	} else if ($e.value instanceof Map) {
    		for (Entry entry : (Set<Entry>) ((Map) $e.value).entrySet()) {
    			properties.put(entry.getKey().toString(), entry.getValue());
    		}
    	} else {
    		throw new ParseException($INCLUDE, "Cannot include resource of type '"
    				+ $e.value.getClass().getName() + "'");
    	}
    } catch (Exception ex) {
    	throw new ParseException($INCLUDE, ex);
    }
   }
  ;

assignement
  :
  ^(v=VAR EQUALS e=expression)
  
   {
    if ($v.text.equals("self")) {
    	throw new ParseException($v, " 'self' is a reserved keyword.");
    }
    properties.put($v.text, $e.value);
   }
  |
  ^(v=VAR DEFAULTS e=expression)
  
   {
    if ($v.text.equals("self")) {
    	throw new ParseException($v, " 'self' is a reserved keyword.");
    }
    if (!properties.containsKey($v.text)) {
    	properties.put($v.text, $e.value);
    }
   }
  ;

concatExpression returns [Object value]
  :
  ^(CONCAT e1=expression e2=expression)
     {try{
	   	if($e1.value.getClass().isArray() && $e2.value.getClass().isArray()){
	   		
	   			$value = ReflectionUtil.mergeArrays($e1.value, $e2.value);
	   	
	   	}else{
	    	$value = $e1.value.toString() + $e2.value.toString();
	    }
	    	} catch (Exception ex) {
	    		throw new ParseException($CONCAT, ex);
	    	}
    }
  ;

expression returns [Object value]
  :
  STRINGLITERAL 
                {
                 $value = $STRINGLITERAL.text;
                }
  | DOUBLELITERAL 
                  {
                   try {
                   	$value = Double.parseDouble($DOUBLELITERAL.text);
                   } catch (NumberFormatException e) {
                   	throw new ParseException($DOUBLELITERAL, "'" + $DOUBLELITERAL.text
                   			+ "' is not a valid double number.");
                   }
                  }
  | INTLITERAL 
               {
                try {
                	$value = Integer.parseInt($INTLITERAL.text);
                } catch (NumberFormatException e) {
                	throw new ParseException($INTLITERAL, "'" + $INTLITERAL.text
                			+ "' is not a valid integer number.");
                }
               }
  | LONGLITERAL 
                {
                 try {
                 	$value = Long.parseLong($LONGLITERAL.text);
                 } catch (NumberFormatException e) {
                 	throw new ParseException($LONGLITERAL, "'" + $LONGLITERAL.text
                 			+ "' is not a valid long number.");
                 }
                }
  | FLOATLITERAL 
                 {
                  try {
                  	$value = Float.parseFloat($FLOATLITERAL.text);
                  } catch (NumberFormatException e) {
                  	throw new ParseException($FLOATLITERAL, "'" + $FLOATLITERAL.text
                  			+ "' is not a valid float number.");
                  }
                 }
  | QUALIFIEDNAME 
                  {
                   try {
                   	$value = properties.getObject($QUALIFIEDNAME.text, Object.class);
                   } catch (PropertyNotFoundException e) {
                   	throw new ParseException($QUALIFIEDNAME, "Variable '" + $QUALIFIEDNAME.text
                   			+ "' is undefined.");
                   }
                  }
  | BOOLEAN 
            {
             $value = Boolean.parseBoolean($BOOLEAN.text);
            }
  | array 
          {
           $value = $array.value;
          }
  | builder 
            {
             $value = $builder.value;
            }
  | concatExpression 
                     {
                      $value = $concatExpression.value;
                     }
  ;

array returns [Object value]
@init {
final List<Object> args = new ArrayList<Object>();
}
  :
  ^(
    type=ARRAY
    (
      e=expression 
                   {
                    args.add($e.value);
                   }
    )*
   )
  
   {
    try {
    	if ($type.text.isEmpty()) {
    		$value = ReflectionUtil.createArray(args);
    	} else {
    		$value = ReflectionUtil.createArray(args,
    				ReflectionUtil.classForName($type.text));
    	}
    } catch (WrongClassException ex) {
    	throw new ParseException($type, "Array incorrectly typed: "+ ex.getMessage());
    }
   }
  ;

builder returns [Object value]
@init {
final List<Object> args = new ArrayList<Object>();
final Map<String, Object> setterArgs = new HashMap<String, Object>();
}
  :
  ^(
    BUILDER
    (
      e=expression 
                   {
                    args.add($e.value);
                   }
    )*
    (
      (
        p=PARAMETER e=expression
      )
      
       {
        setterArgs.put($p.text, $e.value);
       }
    )*
   )
  
   {
    try {
    	$value = ReflectionUtil.newInstanceOf($BUILDER.text, args.toArray());
    	ReflectionUtil.inject(setterArgs, $value);
    	properties.getValidator().validate($value);
    	ReflectionUtil.init($value);
    	$value = ReflectionUtil.buildIfBuilder($value);
    } catch (Exception exc) {
    	throw new ParseException($BUILDER, exc);
    }
   }
  ;
