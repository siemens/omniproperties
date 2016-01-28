/*
* Copyright Siemens AG, 2016
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
public final class IntersectionTest {
	
        @Test
        public void testIntersection() throws IOException {
                final OmniProperties properties = OmniProperties.create();
                final String prop =
                                "a1 = {\"1\", \"2\", \"3\"};\n" +
                                "a2 = {\"2\", \"3\", \"4\"};\n" +
                                "o1 = com.siemens.oss.omniproperties.builders.Intersection({a1, a2});\n" +
                                "";
                properties.readFromString(prop);
                
                String[] o1 = (String[]) properties.get("o1");
                Assert.assertEquals(2, o1.length);
                Assert.assertEquals("2", o1[0]);
                Assert.assertEquals("3", o1[1]);
        }
        
        @Test
        public void testIntersectionInt() throws IOException {
                final OmniProperties properties = OmniProperties.create();
                final String prop =
                                "a1 = {1, 2, 3};\n" +
                                "a2 = {2, 3, 4};\n" +
                                "o1 = com.siemens.oss.omniproperties.builders.Intersection({a1, a2});\n" +
//                                "o1 = com.siemens.oss.omniproperties.builders.Intersection()[intArrays={a1, a2}];\n" +
                                "";
                properties.readFromString(prop);
                
                int[] o1 = (int[]) properties.get("o1");
                Assert.assertEquals(2, o1.length);
                Assert.assertEquals(2, o1[0]);
                Assert.assertEquals(3, o1[1]);
        }
        
	@Test
	public void testIntersectionInShortcuts() throws IOException {
		final OmniProperties properties = OmniProperties.create();
		final String prop =
                                "a1 = {1, 2, 3};\n" +
                                "a2 = {2, 3, 4};\n" +
                                "o1 = Intersection({a1, a2});\n" +
				"";
		properties.readFromString(prop);
		
                int[] o1 = (int[]) properties.get("o1");
                Assert.assertEquals(2, o1.length);
                Assert.assertEquals(2, o1[0]);
                Assert.assertEquals(3, o1[1]);
	}
	
	@Test(expected = ParseException.class)
	public void testIntersectionOnIncompatibleArrays() throws IOException {
		final OmniProperties properties = OmniProperties.create();
		final String prop =
                                "a1 = {1, 2, 3};\n" +
                                "a2 = {\"2\", \"3\", \"4\"};\n" +
                                "o1 = com.siemens.oss.omniproperties.builders.Intersection({a1, a2});\n" +
                                "";
		properties.readFromString(prop);
	}
	
}
