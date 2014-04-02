Copyright Siemens AG, 2014

Licensed under the Apache License, Version 2.0 the "License";
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


# Omniproperties

OmniProperties is a lightweight configuration utility with a flavor of dependency injection. 
Think of "java-properties deluxe": with include statements, string concatenation, typed properties, constructor and setter calls (with validation), 
and last but not least a concise syntax (as opposed to XML's verbosity).

If you are, however in search of a full blown dependency injection framework that offers web application infrastructure, object-relational mappings, persistence etc., 
you might want to consider Spring instead (http://projects.spring.io/spring-framework/). 
If you prefer dependency injection at compile-time via annotations, google-guice (https://code.google.com/p/google-guice/) might be a perfect fit.

This document is organized as follows. First, the OmniProperties file syntax will be discussed.
The second part explains the Java side of OmniProperties. The final part lists tips and application examples.

If you use eclipse or any other IDE to build Omniproperties, please note that some source files are automatically generated by antlr (www.antlr.org). 
Run for instance `mvn package` to trigger the generation and make your IDE happy.

In case you are using Maven, just add

	<dependency>
	    <groupId>com.siemens.oss.omniproperties</groupId>
	    <artifactId>omniproperties</artifactId>
	    <version>3.0.0</version>
	</dependency>

to your dependencies.


## Omniproperties Files

OmniProperties files are text files which support a richer syntax than standard Java Property files. 
In particular they allow the creation of complex objects using constructors and setter methods.

### Property Assignment

#### Primitive Types

1.	Strings:
with double quote: `var = "String with escapes \t tab \n newline etc.";`
or single quote: `var = 'I say: "bla!"';` 

2.	Boolean:
`var = true;` 

3.	Integers:
`var = 10;` 

4.	Longs:
`var = 10L;`

5.	Floats:
`var = 10.0f;`

6.	Doubles:
`var = 10.0;`

#### Arrays:

Note that Arrays are typed. 

Implicitly typed:
 
	variable5 = {1,2, 3};
	variable6 = {"string"};

The type is determined by the first entry. 

Explicitly typed:

	variable7 = int{1,2, 3};
	variable8 = java.lang.String{"string", "another string"};
	variable9 = File{File("test.txt"), ExistingFile("test.txt")};


#### Objects

Objects with constructor:

	variable4 = java.io.File("test.txt");

Objects with setter methods:

	variable4 = com.siemens.Bean()[field1=19, field2="string"];
	
Constructors and setters can by combined.

### Property Defaults

Using the `~` symbol instead of `=`, the integer `10` is assigned to `var` only if `var` does not yet exist: 

	var ~ 10; 

### Concatenation

Strings can be concatenated with the ^ symbol:

	var = "prefix " ^ variable1 ^ " postfix";

Variables of any type can be used in string concatenation as OmniProperties will use their `toString()` method to convert them.
	
In the same vein arrays of any type can be concatenated:

	a1 = {1, 2};
	array =  a1 ^ {3, 4};
	
gives the same result as 

	array = {1, 2, 3, 4};

The resulting array's type is defined by the type of the first array. 
Concatenating arrays of incompatible types will raise an exception.

### Include Statements

Includes can be used to build modular configurations. 
Example: 
	
	include File("../other.oprops");
	
Includes support `File`, `URL`, `InputStream`, `String` (interpreted as a file path) and `Map<String, Object>` as argument.
A `Map` is copied just copied into OmniProperties, replacing properties with the same name as keys in the `Map`. 
All other sources are parsed _in the context_ of the already existing properties. 
This means that the Omniproperties code in an included `File` may reference already existing properties. 

### Comments

OmniProperties supports Java-style line comments:

	// this is a comment
	x = 10; // another comment
	

## The Java Side

### Retrieving Typed Properties

Create and load properties:

	OmniProperties properties = OmniProperties.create().loadFromFile(new File("test.oprops"));

The `OmniProperties` object offers typed getters for the primitive types plus `String`.
	
	int x = properties.getInt("x");
	String s = properties.getString("s");

Objects can be retrieved by the `getObject(String, Class)` method. The method is generic and thus allows for method chaining:
Example: 

	properties.getObject("my-runnable", Runnable.class).run();

Arrays are retrieved in the same vein:

	int[] intArray = properties.getObject("myArray", int[].class);

If the respective property is not found, a `PropertyNotFoundException` is thrown.
Every getter has a sibling method which takes a default value. Furthermore, there are `contains` methods to check for the existence of properties:
	
	int x = properties.getInt("x", 10);
	boolean b = properties.containsInt("x");

### A Closer Look at Object Creation

During object creation OmniProperties performs the following steps:

1. The class name is looked up in the shortcut map (see next section). If no shortcut is found, the name is interpreted as fully qualified class name.

2. The appropriate constructor is loaded and invoked to create a new object.

3. If parameters are given in square brackets they are mapped to setter method invocations. If no setter is found, OmniProperties searches for a field with the respective names and injects the given values. 
As last resort, OmniProperties searches for a `put(String, Object)` method and invokes it (thus `Map` s can by constructed with square bracket notation).

4. If the class implements the `Initializable` interface, the `init()` method is invoked.

5. The object is validated (see section Validation).

6. If the object is an instance of the `ObjectBuilder` interface, the `build()` method is invoked and its result returned as object. Otherwise, the object is returned directly. (see section Builders)

The following sections provide more details...

#### Class Shortcuts
Shortcuts for class names can be defined in `omniproperties-class-shortcuts.properties`. Every file on the class path root with this name will be taken into account. 
Thus, shortcuts can be extended by every new jar on the class path.
Without shortcut: 

	var = java.io.File("test.txt");
	
With shortcut: 

	var = File("test.txt");

#### Validation

Annotations from the OVal Validation Framework (http://oval.sourceforge.net/) are checked after object creation. Only field constraints are enforced, though. 
Example: 

	@NotNull private String test; 
	
The example throws a `ValidationException` if 'test' is still `null` after setters and optional `init()` method have been invoked. 
Alternative validation-frameworks can be plugged in. See the `setValidator(Validator validator)` method in `OmniProperties`. 

#### Builders

Builders can be used to enforce constraints on the created class or to provide alternatives to the constructors. 
Builders must implement the `ObjectBuilder` interface:

	public interface ObjectBuilder<T> {
		T build() throws Exception;
	}
 
In the oprops file, builders and constructors are indistinguishable.
Example: 

	var = ExistingFile("test.txt");

## Tips and Application Examples

The following section presents tips and common application patterns of OmniProperties.

### Syntax Highlighting

There is no OmniProperties plug-in or editor yet. However, as OmniProperties' syntax is close to Java's, any Java editor provides decent syntax highlighting.

### Reading Environment or System Variables

The `include` statement allows the inclusion of the contents of any `Map<String, Object>`. 
`Env` is a builder which reads the environment variables into a map. Thus, the following code does the job:

	include Env();
	
Furthermore, `Env` offers a constructor which allows for filtering the environment variables based on a regular expression:

	include Env("JAVA.*");

In the same vein `SysProps` loads the system properties.

### General Purpose Main-Class

The OmniProperties jar provides a general purpose main class: `com.siemens.oss.omniproperties.Run`.
The idea is that often in projects main classes proliferate and a lot of boilerplate code is written just to run different experimental code with arguments.
`Run` renders such boiler plate mains obsolete. Execute `Run` with an oprops file as sole argument. 
The oprops file is read and the property named `run`, expected to be an instance of `Runnable` is run. Thus, all configuration is contained in the 
oprops file and there is no need for argument parsing and main classes anymore. The code of `Run` is straight forward:

	public final class Run {

		public final static String RUN_KEY = "run";
	
		public static void main(String[] args) throws IOException {
			if (args.length != 1) {
				System.err.println("Usage: java " + Run.class.getName() + " OPROPS_FILE");
				System.exit(-1);
			}
			OmniProperties.create().readFromFile(args[0]).getObject(RUN_KEY, Runnable.class).run();
		}
	}
	
The method chaining is safe as every method is guaranteed to return a non-null object of correct type. All failures are indicated via exceptions.
