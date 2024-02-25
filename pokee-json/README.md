# Pokee JSON Parser 

## Overview

This library is a shitty and minimalistic JSON (de-) serialization tool. 
While it may not compete with the robustness and feature set of well-known JSON handling libraries, 
it's specifically designed for simplicity and integration ease in projects where external libraries are not an option. 
It provides basic functionalities to parse JSON strings into Java objects and serialize Java objects into JSON strings.

## Usage

### Deserializing JSON to Java Objects

To deserialize JSON into a Java object, follow these steps:

1. **Define Your Java Class:** Start by defining the Java class that represents the JSON structure. 
    Use annotations to indicate JSON property names as they might differ from Java field names.
    **Note**: Make sure the class has a no-argument constructor.

    ```java
    public class Person {
        @Property("name")
        private String name; // This field maps to the JSON property "name"
        private int age; // This field maps directly by name to the JSON property "age"
        
        // Constructor, getters, and setters omitted for brevity
    }
    ```

2. **Parse the JSON String:** Use the `ObjectMapper` class to parse the JSON string into an instance of your Java class.

    ```java
    final String json = "{\"name\":\"John Doe\",\"age\":30}";
    final Person person = ObjectMapper.parse(json, Person.class);
    System.out.println(person.getName()); // Output: John Doe
    ```

### Serializing Java Objects to JSON

To serialize a Java object into a JSON string, follow these steps:

1. **Create and Populate Your Java Object:** Instantiate and populate your Java object. You can use the same class structure as for deserialization.

    ```java
    final Person person = new Person("Jane Doe", 25);
    ```

2. **Convert Java Object to JSON String:** Use the `JsonWriter` class to serialize the Java object into a JSON string.

    ```java
    final String json = JsonWriter.objectToJson(person, "  ", 0);
    System.out.println(json);
    ```

## Annotations

- **@Property:** Specifies the JSON property name for a Java field. Use this annotation when the JSON property name does not match the Java field name.
- **@Ignored:** Excludes a field from serialization and deserialization.
- **@Optional:** Marks a field as optional during deserialization. If the JSON does not contain this field, the library will not throw an exception.

## Limitations

Don't use this library in production. It's not suitable for complex JSON structures or large-scale projects.
Buuuuuut for our simple game, this is more than enough.
