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

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.siemens.oss.omniproperties.OmniProperties;
import com.siemens.oss.omniproperties.exceptions.ParseException;

/**
 * @author Holger Schoener <holger.schoener@siemens.com>
 * 
 */
public final class StringSplitterTest {
	
	@Test
	public void testStringSplitter() throws IOException {
		final OmniProperties properties = OmniProperties.create();
		final String prop =
				"s1 = \"test\";\n" +
				"s2 = 'test1,test2';\n" +
				"s3 = '';\n" +
				"s4 = 'test1;test2';\n" +
				"s5 = 'test1, test2';\n" +
				"s6 = ' test1 ,test2 ';\n" +
				"s7 = ' test1 , test2 ';\n" +
				"o1 = com.siemens.oss.omniproperties.builders.StringSplitter(s1);\n" +
				"o2 = com.siemens.oss.omniproperties.builders.StringSplitter(s2);\n" +
				"o3 = com.siemens.oss.omniproperties.builders.StringSplitter(s3);\n" +
				"o4 = com.siemens.oss.omniproperties.builders.StringSplitter(s4, \";\");\n" +
				"o5 = com.siemens.oss.omniproperties.builders.StringSplitter(s5);\n" +
				"o6 = com.siemens.oss.omniproperties.builders.StringSplitter(s6);\n" +
				"o7 = com.siemens.oss.omniproperties.builders.StringSplitter(s7, false);\n" +
				"";
		properties.readFromString(prop);
		
		String[] o1 = (String[]) properties.get("o1");
		Assert.assertEquals(1, o1.length);
		Assert.assertEquals("test", o1[0]);
		
		String[] o2 = (String[]) properties.get("o2");
		Assert.assertEquals(2, o2.length);
		Assert.assertEquals("test1", o2[0]);
		Assert.assertEquals("test2", o2[1]);
		
		String[] o3 = (String[]) properties.get("o3");
		Assert.assertEquals(1, o3.length);
		
		String[] o4 = (String[]) properties.get("o4");
		Assert.assertEquals(2, o4.length);
		Assert.assertEquals("test1", o4[0]);
		Assert.assertEquals("test2", o4[1]);
		
		String[] o5 = (String[]) properties.get("o5");
		Assert.assertEquals(2, o5.length);
		Assert.assertEquals("test1", o5[0]);
		Assert.assertEquals("test2", o5[1]);
		
		String[] o6 = (String[]) properties.get("o6");
		Assert.assertEquals(2, o6.length);
		Assert.assertEquals("test1", o6[0]);
		Assert.assertEquals("test2", o6[1]);
		
		String[] o7 = (String[]) properties.get("o7");
		Assert.assertEquals(2, o7.length);
		Assert.assertEquals(" test1 ", o7[0]);
		Assert.assertEquals(" test2 ", o7[1]);
	}
	
	@Test
	public void testStringSplitterInShortcuts() throws IOException {
		final OmniProperties properties = OmniProperties.create();
		final String prop =
				"s1 = \"test\";\n" +
				"o1 = StringSplitter(s1);\n" +
				"";
		properties.readFromString(prop);
		
		String[] o1 = (String[]) properties.get("o1");
		Assert.assertEquals(1, o1.length);
		Assert.assertEquals("test", o1[0]);
	}
	
	@Test(expected = ParseException.class)
	public void testStringSplitterNotOnString() throws IOException {
		final OmniProperties properties = OmniProperties.create();
		final String prop = "s1 = 5; o1 = com.siemens.ct.fenrir.coness.StringSplitter(s1);";
		properties.readFromString(prop);
	}
	
}
