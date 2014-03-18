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

grammar OmniProperties;

options {
  language     = Java;
  output       = AST;
  ASTLabelType = CommonTree;
  backtrack    = true;
}

tokens {
  BUILDER;
  //  REFLECTBUILDER;
  ARRAY;
  PARAMETER;
  VAR;
  ROOT;
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
}

@lexer::header {
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
import org.apache.commons.lang.StringEscapeUtils;
import com.siemens.oss.omniproperties.exceptions.*;
}

@lexer::members {
@Override
public void reportError(RecognitionException e) {

	throw new ParseException(getErrorHeader(e) + " "
			+ getErrorMessage(e, getTokenNames()), e);
}
}

@members {
@Override
protected Object recoverFromMismatchedToken(IntStream input, int ttype,
		BitSet follow) throws RecognitionException {
	throw new MismatchedTokenException(ttype, input);
}

@Override
public Object recoverFromMismatchedSet(IntStream input, RecognitionException e,
		BitSet follow) throws RecognitionException {
	throw new ParseException(getErrorHeader(e) + " "
			+ getErrorMessage(e, getTokenNames()), e);
}
}

@rulecatch {
catch(RecognitionException e) {
        throw new ParseException(getErrorHeader(e) + " " + getErrorMessage(e, getTokenNames()), e);
   }
}

omniproperties
  :
  (statement ';')*
    ->
      ^(ROOT statement*)
  ;

statement
  :
  assignement
  | include
  ;
  
include:
  (INCLUDE^ expression)
;

paramameter
  :
  QUALIFIEDNAME EQUALS expression
    -> PARAMETER[$QUALIFIEDNAME, $QUALIFIEDNAME.text] expression
  ;

builder
  :
  QUALIFIEDNAME '(' (expression (',' expression)*)? ','? ')' ('[' (paramameter (',' paramameter)*)? ','? ']')?
    ->
      ^(BUILDER[$QUALIFIEDNAME, $QUALIFIEDNAME.text] expression* paramameter*)
  ;

assignement
  :
  QUALIFIEDNAME EQUALS expression
    ->
      ^(VAR[$QUALIFIEDNAME, $QUALIFIEDNAME.text] EQUALS expression)
  | QUALIFIEDNAME DEFAULTS expression
    ->
      ^(VAR[$QUALIFIEDNAME, $QUALIFIEDNAME.text] DEFAULTS expression)
  ;

expression
  :
  concatExpression
  ;

concatExpression
  :
  atom (CONCAT^ atom)*
  ;

atom
  :
  STRINGLITERAL
  | DOUBLELITERAL
  | FLOATLITERAL
  | INTLITERAL
  | LONGLITERAL
  | QUALIFIEDNAME
  | BOOLEAN
  | array
  | builder
  ;
  
  array
  :
  (type=QUALIFIEDNAME)? '{' (expression (',' expression)*)? ','? '}'
    ->
      ^(ARRAY[$type,$type.text==null?"":$type.text] expression*)
  ;
  
INCLUDE:
  'include'
;

BOOLEAN
  :
  'true'
  | 'false'
  ;

INTLITERAL
  :
  Sign? IntegerNumber
  ;

LONGLITERAL
  :
  Sign? IntegerNumber 'L' 
                          {
                           setText(getText().substring(0, getText().length() - 1));
                          }
  ;

DOUBLELITERAL
  :
  Sign? NonIntegerNumber
  ;

FLOATLITERAL
  :
  Sign? NonIntegerNumber 'f' 
                             {
                              setText(getText().substring(0, getText().length() - 1));
                             }
  ;



EQUALS
  :
  '='
  ;

DEFAULTS
  :
  '~'
  ;

CONCAT
  :
  '^'
  ;

MEMBER
  :
  '->'
  ;

STRINGLITERAL
  :
  (
    '"'
    (
      EscapeSequence
      |
      ~(
        '\\'
        | '"'
       )
    )*
    '"'
    | '\''
    (
      EscapeSequence
      |
      ~(
        '\\'
        | '\''
       )
    )*
    '\''
  )
  
   {
    // strip the quotes from the resulting token and unescape
    setText(StringEscapeUtils.unescapeJava(getText().substring(1,
    		getText().length() - 1)));
   }
  ;

QUALIFIEDNAME
  :
  Identifier
  (
    '.' Identifier
    | '.' Digit*
  )*
  ;

fragment
Sign
  :
  '-'
  | '+'
  ;

fragment
Identifier
  :
  (Letter)
  (
    Letter
    | Digit
  )*
  ;

fragment
IntegerNumber
  :
  '0'
  | '1'..'9' ('0'..'9')*
  | '0' ('0'..'7')+
  | HexPrefix HexDigit+
  ;

fragment
HexPrefix
  :
  '0x'
  | '0X'
  ;

fragment
HexDigit
  :
  (
    '0'..'9'
    | 'a'..'f'
    | 'A'..'F'
  )
  ;

fragment
NonIntegerNumber
  :
  ('0'..'9')+ '.' ('0'..'9')* Exponent?
  | '.' ('0'..'9')+ Exponent?
  | ('0'..'9')+ Exponent
  | ('0'..'9')+
  | HexPrefix (HexDigit)*
  (
    ()
    | ('.' (HexDigit)*)
  )
  (
    'p'
    | 'P'
  )
  (
    '+'
    | '-'
  )?
  ('0'..'9')+
  ;

fragment
Exponent
  :
  (
    'e'
    | 'E'
  )
  (
    '+'
    | '-'
  )?
  ('0'..'9')+
  ;

fragment
EscapeSequence
  :
  '\\'
  (
    'b'
    | 't'
    | 'n'
    | 'f'
    | 'r'
    | '\"'
    | '\''
    | '\\'
  )
  | UnicodeEscape
  | OctalEscape
  ;

fragment
OctalEscape
  :
  '\\' ('0'..'3') ('0'..'7') ('0'..'7')
  | '\\' ('0'..'7') ('0'..'7')
  | '\\' ('0'..'7')
  ;

fragment
UnicodeEscape
  :
  '\\' 'u' HexDigit HexDigit HexDigit HexDigit
  ;

fragment
Letter
  :
  '\u0024'
  | '\u0041'..'\u005a'
  | '\u005f'
  | '\u0061'..'\u007a'
  | '\u00c0'..'\u00d6'
  | '\u00d8'..'\u00f6'
  | '\u00f8'..'\u00ff'
  | '\u0100'..'\u1fff'
  | '\u3040'..'\u318f'
  | '\u3300'..'\u337f'
  | '\u3400'..'\u3d2d'
  | '\u4e00'..'\u9fff'
  | '\uf900'..'\ufaff'
  | '-'
  | '@'
  ;

fragment
Digit
  :
  '0'..'9'
  ;

LINE_COMMENT
  :
  '//'
  ~(
    '\n'
    | '\r'
   )*
  (
    ('\r'? '\n')
    | EOF
  )
  
   {
    $channel = HIDDEN;
   }
  ;

WS
  :
  (
    ' '
    | '\r'
    | '\t'
    | '\u000C'
    | '\n'
  )
  
   {
    $channel = HIDDEN;
   }
  ;
