# PSON - Pokee JSON Parser

> [!WARNING]
> The `mapper` package does some _very_ illegal unsafe things to achieve parsing JSON to objects.
> Use with caution. It may or may not work with newer Java versions.

PSON (_Pokee's ~~Shitty~~ Object Notation_) is a minimalistic and probably incomplete JSON (de-) serialization tool.
It provides basic functionalities to parse JSON strings into Java objects and serialize Java objects into JSON strings.

## Reading JSON

```java
import com.github.pokee.pson.Pson;
import com.github.pokee.pson.mapper.annotations.JsonProperty;
import com.github.pokee.pson.mapper.annotations.JsonScope;
import com.github.pokee.pson.value.JsonObject;

// you can use @JsonScope to specify which fields should be serialized/deserialized
@JsonScope(privateFields = false)
class Person {

    // you can use @JsonProperty to specify the name of the field in the JSON
    @JsonProperty("name")
    public String myName;

    // if no @JsonProperty is present, the field name will be used
    public int age;

    // this field will be ignored because it is `private` and the scope is set to `privateFields = false`
    private String secret;

    // this field will be ignored because it is `transient`
    public transient String transientField;

}

final String json = "{\"name\":\"John Doe\",\"age\":30}";
final Pson pson = Pson.createWithDefaults().build();

// Parse JSON to a Java object
final Person parsedPerson = pson.unmarshalObject(json, Person.class);

// Parse JSON to a JSON object
final JsonObject parsedJsonObject = pson.unmarshalJson(json);
```

If you use the `unmarshalObject` method to parse JSON into a Java object, every field from the class must be present in
the JSON string. If a field is missing, the library will throw an exception.

If you want to mark the field as optional, you can annotate it with `@Optional`. This will prevent the library from
throwing an exception if the field is missing in the JSON string and will always return the default value of the field
type.

You can also ignore specific fields by annotating them with `@JsonIgnored`.
This will prevent the library from throwing an exception if the field is missing in the JSON string and will always
return in the default value of the field type. If the field is `transient`, it will be ignored by default.

### Custom Type Adapters

You can also use custom type adapters to parse JSON strings into Java objects. This is useful when you need to parse
complex JSON structures or when you want to customize the parsing process.

```java
import com.github.pokee.pson.Pson;

final Pson pson = Pson.createWithDefaults()
        .registerValueReaderMapper(MyCustomType.class, (element, field) -> {
            return new MyCustomType(element.asObject()
                    .get("value").asPrimitive()
                    .asString());
        })
        .build();
```

If you only want to target specific fields (e.g. only fields that are annotated by a specific annotation), you can use
specify a field predicate:

```java
import com.github.pokee.pson.Pson;
import com.github.pokee.pson.PsonBuilder;

final Pson pson = Pson.createWithDefaults()
        .registerValueReaderMapper(
                MyCustomType.class,
                PsonBuilder.hasAnnotation(MyAnnotation.class),
                (element, field) -> {
                    return new MyCustomType(element.asObject()
                            .get("value").asPrimitive()
                            .asString());
                })
        .build();
```

### Manually parsing JSON

If you want to parse JSON manually, you can use the `JsonParser` class. This class
provides methods to parse JSON strings into `JsonElement` objects, which can be used to navigate and manipulate the JSON
data. If you really want to manually parse something, you can use the `JsonTokenizer` class to tokenize the JSON string.

---

## Writing JSON

```java
import com.github.pokee.pson.Pson;
import com.github.pokee.pson.mapper.annotations.JsonProperty;
import com.github.pokee.pson.mapper.annotations.JsonScope;

// you can use @JsonScope to specify which fields should be serialized/deserialized
@JsonScope(privateFields = false)
class Person {

    // you can use @JsonProperty to specify the name of the field in the JSON
    @JsonProperty("name")
    public String myName;

    // if no @JsonProperty is present, the field name will be used
    public int age;

    public Person(String myName, int age) {
        this.myName = myName;
        this.age = age;
    }

}

final Pson pson = Pson.createWithDefaults().build();
final String outputJson = pson.marshal(new Person("John Doe", 30));
```

You can enable pretty-printing by setting the `prettyPrint` option to `true` when creating the `Pson` instance.

```java
Pson.createWithDefaults()
    .

prettyPrint()
    .

build();
```

---

## JSON Functions

A JSON function is essentially a piece of logic that can be executed as part of the JSON parsing or generation process.
Functions can be used to dynamically modify the JSON data in the parsing process, like reading values from the
environment.
These functions are embedded directly within the JSON data, making the JSON structure more dynamic and adaptable.

**config.pson**
<!-- @formatter:off -->
```json
{ 
  "secret": @env("SECRET"),
  "server": @json-file("server.pson")
}
```
<!-- @formatter:on -->

**server.pson**

<!-- @formatter:off -->
```json
{
  "host": "localhost",
  "port": 8080
}
```
<!-- @formatter:on -->

When parsing the `config.pson` file, the `@env("SECRET")` function retrieves the value of the `SECRET` environment
variable, and the `@json-file("server.pson")` function reads the content of the `server.pson` file and embeds it into
the JSON data. The output of the parsing process will be:

<!-- @formatter:off -->
```json
{
  "secret": "my-secret",
  "server: {
      "host": "localhost",
      "port": 8080
    }
}
```
<!-- @formatter:on -->

### Built-in Functions Overview

**Loading External Data:**

- `@file(path): string | JsonElement` - Reads the content of a specified file as a string.
- `@json-file(path): JsonElement` - Reads the content of a specified file and parses it as JSON.
- `@env(name): string` - Retrieves the value of an environment variable.
- `@json-env(name): JsonElement` - Retrieves the value of an environment variable and parses it as JSON.

**Logical Operations:**

- `@if(truthy, then, else): JsonElement` - Evaluates a condition and returns a value based on the result (true or
  false).
- `@eq(a, b): bool` - Checks equality between two values.
- `@gt(a, b): bool` - Checks if the first value is greater than the second.
- `@gte(a, b): bool` - Checks if the first value is greater than or equal to the second.
- `@lt(a, b): bool` - Checks if the first value is less than the second.
- `@lte(a, b): bool` - Checks if the first value is less than or equal to the second.
- `@default(a, b): a or b` - Returns the first value if it is not null or empty; otherwise, it returns the second value.

**String Manipulation:**

- `@strip(str): string` - Removes leading and trailing whitespace from a string.
- `@trim(str): string` - Removes leading and trailing whitespace from a string.

### Implementing Custom JSON Functions

Custom JSON functions can be implemented to extend the functionality of the JSON parsing library. This is achieved using
the `PsonBuilder`, which allows for the registration of custom function callbacks.

```java
import com.github.pokee.pson.value.JsonObject;
import com.github.pokee.pson.Pson;
import com.github.pokee.pson.value.JsonPrimitive;

final Pson pson = Pson.createWithDefaults()
        .registerFunctionCallback("my-function", (parser, function) -> {
            final JsonObject options = function.getOptions();
            // Custom function logic here
            return JsonPrimitive.fromString("hello world!")
        }).build();
```

The `registerFunctionCallback` method takes the name of the function and a callback function that will be executed when
the function is encountered during the parsing process. The callback function receives the `PsonParser` and the
`Function` object, which contains the function name and options.

Once the custom function is registered, it can be used in the JSON data:

```json
{
  "message": @my-function()
}
```

When the JSON data is parsed, the custom function will be executed, and the result will be embedded into the JSON data.

```json
{
  "message": "hello world!"
}
```

## Limitations

You don't want to use this library in production.
It's not suitable for complex JSON structures or large-scale projects.
Buuuuuut for our simple ASE project, this is more than enough.
