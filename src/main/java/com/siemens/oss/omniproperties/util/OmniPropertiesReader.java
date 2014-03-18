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

package com.siemens.oss.omniproperties.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTreeNodeStream;

import com.siemens.oss.omniproperties.OmniProperties;
import com.siemens.oss.omniproperties.exceptions.ParseException;
import com.siemens.oss.omniproperties.exceptions.PropertyNotFoundException;
import com.siemens.oss.omniproperties.parser.OmniPropertiesBuilder;
import com.siemens.oss.omniproperties.parser.OmniPropertiesLexer;
import com.siemens.oss.omniproperties.parser.OmniPropertiesParser;

/**
 * Bundles methods for reading {@link OmniProperties}. Users should not use this
 * class directly. Instead call the respective methods of the {@link OmniProperties}
 * class.
 * 
 * @author Markus Michael Geipel
 * 
 */
public final class OmniPropertiesReader {
	private static final String OPROPS_LOCATION = "OPROPS_LOCATION";

	/**
	 * Read properties from an {@link InputStream}. Read properties are
	 * <em>added</em> to existing properties.
	 * 
	 * @param input
	 * @throws IOException
	 */
	public static void readFromStream(final InputStream input,
			final OmniProperties properties) throws IOException {
		try {
			readProperties(compileAst(input), properties);
		} catch (RecognitionException | PropertyNotFoundException e) {
			throw new ParseException(e);
		}
	}

	/**
	 * Read properties from a {@link File}. Read properties are <em>added</em>
	 * to existing properties.
	 * 
	 * @param url
	 * @throws IOException
	 */
	public static void readFromUrl(final URL url,
			final OmniProperties properties) throws IOException {
		try (final InputStream input = url.openStream()) {
			readFromStream(input, properties);
		} catch (ParseException e) {
			throw new ParseException("Syntax error reading from url '"
					+ url.toString() + "'", e);
		}
	}

	/**
	 * Read properties from a {@link File}. Read properties are <em>added</em>
	 * to existing properties.
	 * 
	 * @param file
	 * @throws IOException
	 */
	public static void readFromFile(final File file,
			final OmniProperties properties) throws IOException {
		try (final FileInputStream input = new FileInputStream(file)) {
			readFromStream(input, properties);
			properties.put(OPROPS_LOCATION, file);
		} catch (ParseException e) {
			throw new ParseException("Error in file '" + file.toString() + "'",
					e);
		}
	}

	/**
	 * Read properties from a string. Read properties are <em>added</em> to
	 * existing properties.
	 * 
	 * @param string
	 * @throws IOException
	 */
	public static void readFromString(final String string,
			final OmniProperties properties) throws IOException {
		try(final InputStream stream = new ByteArrayInputStream(string.getBytes("UTF-8"))) {
			readFromStream(stream,
					properties);
		} catch (ParseException e) {
			throw new ParseException("Error in resource '" + string + "'", e);
		}
	}

	/**
	 * Read properties from a resource. Read properties are <em>added</em> to
	 * existing properties.
	 * 
	 * @param resource
	 * @throws IOException
	 */
	public static void readFromResource(final String resource,
			final OmniProperties properties) throws IOException {
		try (final InputStream input = Thread.currentThread()
				.getContextClassLoader().getResourceAsStream(resource)) {
			if(input==null){
				throw new ParseException("resource '" + resource + "' not found");
			}
			readFromStream(input, properties);
		} catch (ParseException e) {
			throw new ParseException("Error in resource '" + resource + "'", e);
		}
	}

	/**
	 * Read properties from a file. Read properties are <em>added</em> to
	 * existing properties.
	 * 
	 * @param input
	 * @throws IOException
	 */
	public static void readFromFile(final String string,
			final OmniProperties properties) throws IOException {
		readFromFile(new File(string), properties);
	}

	private static CommonTreeNodeStream compileAst(final InputStream input)
			throws IOException, RecognitionException {
		final OmniPropertiesParser parser = new OmniPropertiesParser(
				new CommonTokenStream(new OmniPropertiesLexer(
						new ANTLRInputStream(input))));
		return new CommonTreeNodeStream(parser.omniproperties().getTree());
	}

	private static void readProperties(final CommonTreeNodeStream treeNodes,
			final OmniProperties properties) throws RecognitionException {
		final OmniPropertiesBuilder builder = new OmniPropertiesBuilder(
				treeNodes);
		builder.setProperties(properties);
		builder.parse();
	}

}
