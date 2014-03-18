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

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.Map;

import org.antlr.runtime.RecognitionException;
import org.junit.Assert;
import org.junit.Test;

import com.siemens.oss.omniproperties.OmniProperties;
import com.siemens.oss.omniproperties.exceptions.ParseException;
import com.siemens.oss.omniproperties.exceptions.WrongClassException;

/**
 * @author Markus Geipel
 * 
 */
public final class OmniPropertiesTest {

	@Test
	public void testBasicTypes() throws UnsupportedEncodingException, IOException {
		final OmniProperties properties = OmniProperties.create();
		final String prop = "s = \"test\"; s2 = 'test2\\''; i = 10; d = 1.4; b1 = true; b2=false; f=0.1f; l=1234L;";
		properties.readFromString(prop);

		Assert.assertTrue(properties.containsString("s"));
		Assert.assertTrue(properties.containsString("s2"));
		Assert.assertTrue(properties.containsInt("i"));
		Assert.assertTrue(properties.containsDouble("d"));

		Assert.assertFalse(properties.containsString("t"));
		Assert.assertFalse(properties.containsInt("d"));
		Assert.assertFalse(properties.containsDouble("s"));

		Assert.assertEquals("test", properties.getString("s"));
		Assert.assertEquals("test2'", properties.getString("s2"));
		Assert.assertEquals(10, properties.getInt("i"));
		Assert.assertEquals(10L, properties.getLong("i"));
		Assert.assertEquals(1.4, properties.getDouble("d"), 0.0001);

		Assert.assertTrue(properties.containsBoolean("b1"));
		Assert.assertTrue(properties.containsBoolean("b2"));
		Assert.assertFalse(properties.containsDouble("b1"));

		Assert.assertTrue(properties.getBoolean("b1"));
		Assert.assertFalse(properties.getBoolean("b2"));

		Assert.assertTrue(properties.containsFloat("f"));
		Assert.assertTrue(properties.containsDouble("f"));
		Assert.assertFalse(properties.containsFloat("d"));

		Assert.assertTrue(properties.containsLong("l"));
		Assert.assertTrue(properties.containsLong("i"));
		Assert.assertFalse(properties.containsInt("l"));

		Assert.assertEquals(0.1f, properties.getFloat("f"), 0.0001);
		Assert.assertEquals(0.1, properties.getDouble("f"), 0.0001);
		Assert.assertEquals(1234L, properties.getLong("l"));
	}

	@Test
	public void testWeirdVarNames() throws UnsupportedEncodingException, IOException {
		final OmniProperties properties = OmniProperties.create();

		properties.readFromString("@-.5.Hallo.@ = 1;");
		Assert.assertEquals(1, properties.getInt("@-.5.Hallo.@"));
	}

	@Test
	public void testPut() throws UnsupportedEncodingException, IOException {
		final OmniProperties properties = OmniProperties.create();

		properties.readFromString("ht = java.util.HashMap()[string='hallo', int=1];");
		Assert.assertEquals("hallo", properties.getObject("ht", Map.class).get("string"));
		Assert.assertEquals(1, properties.getObject("ht", Map.class).get("int"));
	}

	@Test
	public void testDefault() throws UnsupportedEncodingException, IOException {
		final OmniProperties properties = OmniProperties.create();

		properties.readFromString("i = 1; i ~ 2;");
		Assert.assertEquals(1, properties.getInt("i"));

		properties.clear();
		properties.readFromString("i ~ 2; i ~ 1;");
		Assert.assertEquals(2, properties.getInt("i"));

		properties.clear();
		properties.readFromString("i ~ 2; i = 1;");
		Assert.assertEquals(1, properties.getInt("i"));

	}

	@Test
	public void testBuilder() throws UnsupportedEncodingException, IOException {
		final OmniProperties properties = OmniProperties.create();
		final String prop = "o1 = java.io.File(\"test.txt\"); o2 = com.siemens.oss.omniproperties.builders.NonExistingFile(\"test.txt\");";
		properties.readFromString(prop);
		final File expected1 = new File("test.txt");
		final File expected2 = new File("test.txt").getAbsoluteFile();

		Assert.assertTrue(properties.containsObject("o1", File.class));
		Assert.assertFalse(properties.containsObject("o1", Assert.class));
		Assert.assertFalse(properties.containsObject("o3", File.class));
		Assert.assertFalse(properties.containsObject("o3", Assert.class));

		Assert.assertEquals(expected1, properties.getObject("o1", File.class));
		Assert.assertEquals(expected2, properties.getObject("o2", File.class));
	}

	@Test
	public void testReflectionBuilder() throws UnsupportedEncodingException, IOException {
		final OmniProperties properties = OmniProperties.create();

		final String prop = "B = com.siemens.oss.omniproperties.Bean(\"X\")[a=\"A\", b = \"B\", c=\"C\"];";
		properties.readFromString(prop);

		Assert.assertTrue(properties.containsObject("B", Bean.class));
		final Bean bean = properties.getObject("B", Bean.class);

		Assert.assertEquals("A", bean.getA());
		Assert.assertEquals("B", bean.getB());
		Assert.assertEquals("C", bean.getC());
		Assert.assertEquals("X", bean.getX());
	}

	@Test
	public void testConstructorMatching() throws UnsupportedEncodingException, IOException {
		final OmniProperties properties = OmniProperties.create();
		final String prop = "s = Socket(\"localhost\", 88);";
		properties.readFromString(prop);
		properties.getObject("s", InetSocketAddress.class);
	}

	@Test
	public void testVariableReferences() throws UnsupportedEncodingException, IOException {
		final OmniProperties properties = OmniProperties.create();
		final String prop = "s1 = \"test\"; s2 = s1;";
		properties.readFromString(prop);

		Assert.assertEquals("test", properties.getString("s1"));
		Assert.assertEquals("test", properties.getString("s2"));
	}

	@Test
	public void testStringConcatination() throws UnsupportedEncodingException, IOException {
		final OmniProperties properties = OmniProperties.create();

		properties.readFromString("i = 1; s1 = \"test\" ^ 1;");
		Assert.assertEquals("test1", properties.getString("s1"));

		properties.readFromString("s2 = \"test\" ^ i ^ \"test\";");
		Assert.assertEquals("test1test", properties.getString("s2"));

		properties.readFromString("s3 = \"test\" ^ i ^ \"test\" ^ 2;");
		Assert.assertEquals("test1test2", properties.getString("s3"));
	}
	
	@Test
	public void testArrayConcatination() throws UnsupportedEncodingException, IOException {
		final OmniProperties properties = OmniProperties.create();

		properties.readFromString("a = {1,2} ^ {3,4};");
		Assert.assertArrayEquals(new int[]{1,2,3,4}, properties.getObject("a", int[].class));

		properties.readFromString("a = {'a'} ^ {'b'};");
		Assert.assertArrayEquals(new String[]{"a","b"}, properties.getObject("a", String[].class));

		
	}

	@Test
	public void testMissingVariable() throws UnsupportedEncodingException, IOException {
		try {
			final OmniProperties properties = OmniProperties.create();
			properties.readFromString("s = X;");
		} catch (Exception e) {
			Assert.assertTrue(e.getCause().getMessage().contains("Error in line"));
			return;
		}
		Assert.fail();

	}

	@Test
	public void testClassShortcuts() throws UnsupportedEncodingException, IOException {
		final OmniProperties properties = OmniProperties.create();
		properties.readFromString("o = NonExistingFile(\"test.txt\");");
		final File expected = new File("test.txt").getAbsoluteFile();
		Assert.assertEquals(expected, properties.getObject("o", File.class));
	}

	@Test
	public void testArrays() throws UnsupportedEncodingException, IOException {
		final OmniProperties properties = OmniProperties.create();
		properties.readFromString("ia = {1,2, 3,}; sa ={\"string\"};");

		Assert.assertArrayEquals(new int[] { 1, 2, 3 }, properties.getObject("ia", int[].class));
		Assert.assertArrayEquals(new String[] { "string" }, properties.getObject("sa", String[].class));
	}

	@Test
	public void testTypedArrays() throws UnsupportedEncodingException, IOException {
		final OmniProperties properties = OmniProperties.create();
		properties.readFromString("ia1 = java.lang.Integer{1,2}; ia2 = int{1,2}; sa = String{};");

		Assert.assertArrayEquals(new int[] { 1, 2 }, properties.getObject("ia1", int[].class));
		Assert.assertArrayEquals(new int[] { 1, 2 }, properties.getObject("ia2", int[].class));
		Assert.assertArrayEquals(new String[] {}, properties.getObject("sa", String[].class));
	}

	@Test(expected = WrongClassException.class)
	public void testIncorrectClass1() throws UnsupportedEncodingException, IOException {
		final OmniProperties properties = OmniProperties.create();
		properties.readFromString("a = \"string\";");
		properties.getInt("a");
	}

	@Test(expected = WrongClassException.class)
	public void testIncorrectClass2() throws UnsupportedEncodingException, IOException {
		final OmniProperties properties = OmniProperties.create();
		properties.readFromString("a = File(\"string\");");
		properties.getString("a");
	}

	@Test(expected = ParseException.class)
	public void testIllegalNumber() throws UnsupportedEncodingException, IOException {
		final OmniProperties properties = OmniProperties.create();
		properties.readFromString("a = 1234567890123456789;");
		properties.getInt("a");
	}

	@Test(expected = ParseException.class)
	public void testReservedKeywordSelf() throws UnsupportedEncodingException, IOException {
		final OmniProperties properties = OmniProperties.create();
		properties.readFromString("self = 1;");

	}

	@Test(expected = ParseException.class)
	public void testIllegalInclude() throws UnsupportedEncodingException, IOException {
		final OmniProperties properties = OmniProperties.create();
		properties.readFromString("#include 1;");
	}

	@Test(expected = ParseException.class)
	public void testIncorrectArray() throws UnsupportedEncodingException, RecognitionException, IOException {
		final OmniProperties properties = OmniProperties.create();
		properties.readFromString("a = {1, \"hula\"};");
	}

	@Test(expected = ParseException.class)
	public void testVerification() throws UnsupportedEncodingException, RecognitionException, IOException {
		final OmniProperties properties = OmniProperties.create();
		final String prop = "B = com.siemens.oss.omniproperties.Bean()[b = \"B\", c=\"C\"];";
		properties.readFromString(prop);
	}

	@Test
	public void testInclude() throws UnsupportedEncodingException, RecognitionException, IOException {
		final OmniProperties properties = OmniProperties.create();
		properties.readFromString("include File('src/test/resources/withComments.omniprop');");

		Assert.assertEquals("@51", properties.getString("prefix"));
	}

	@Test
	public void testComments() throws UnsupportedEncodingException, RecognitionException, IOException {
		final OmniProperties properties = OmniProperties.create();
		properties.readFromResource("withComments.omniprop");

		Assert.assertArrayEquals(new String[] { "@51a", "@51b", "@51c", "@51d" }, properties.getObject("inputs", String[].class));
	}

}
