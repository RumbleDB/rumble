# User-defined types

RumbleDB now supports user-defined array and object types both with the JSound compact syntax and the JSound verbose syntax.

## JSound Schema Compact syntax

RumbleDB user-defined types can be defined with the JSound syntax. A tutorial for the JSound syntax can be found [here](https://github.com/ghislainfourny/jsound-tutorial).

For now, RumbleDB only allows the definition of user-defined types for objects and arrays. User-defined atomic types and union types will follow soon. Also, the @ (primary key) and ? (nullable) characters are not supported at this point. The implementation is still experimental and bugs are still expected, which we will appreciate to be informed of.

## Type declaration

A new type can be declared in the prolog, at the same location where you also define global variables and user-defined functions.

```
declare type local:my-type as {
  "foo" : "string",
  "bar" : "integer"
};

{ "foo" : "this is a string", "bar" : 42 }
```

In the above query, although the type is defined, the query returns an object that was not validated against this type.

## Type declaration

To validate and annotate a sequence of objects, you need to use the validate-type expression, like so:


```
declare type local:my-type as {
  "foo" : "string",
  "bar" : "integer"
};

validate type local:my-type* {
  { "foo" : "this is a string", "bar" : 42 }
}
```

You can use user-defined types wherever other types can appear: as type annotation for FLWOR variables or global variables, as function parameter or return types, in instance-of or treat-as expressions, etc.

```
declare type local:my-type as {
  "foo" : "string",
  "bar" : "integer"
};

declare function local:proj($x as local:my-type+) as string*
{
  $x.foo
};

let $a as local:my-type* := validate type local:my-type* {
  { "foo" : "this is a string", "bar" : 42 }
}
return if($a instance of local:my-type*)
       then local:proj($a)
       else "Not an instance."
```


You can validate larger sequences

```
declare type local:my-type as {
  "foo" : "string",
  "bar" : "integer"
};

validate type local:my-type* {
  { "foo" : "this is a string", "bar" : 42 },
  { "foo" : "this is another string", "bar" : 1 },
  { "foo" : "this is yet another string", "bar" : 2 },
  { "foo" : "this is a string", "bar" : 12 },
  { "foo" : "this is a string", "bar" : 42345 },
  { "foo" : "this is a string", "bar" : 42 }
}
```

You can also validate, in parallel, an entire JSON Lines file, like so:

```
declare type local:my-type as {
  "foo" : "string",
  "bar" : "integer"
};

validate type local:my-type* {
  json-file("hdfs:///directory-file.json")
}
```

## Optional vs. required fields

By defaults, fields are optional:

```
declare type local:my-type as {
  "foo" : "string",
  "bar" : "integer"
};

validate type local:my-type* {
  { "foo" : "this is a string", "bar" : 42 },
  { "bar" : 1 },
  { "foo" : "this is yet another string", "bar" : 2 },
  { "foo" : "this is a string" },
  { "foo" : "this is a string", "bar" : 42345 },
  { "foo" : "this is a string", "bar" : 42 }
}
```

You can, however, make a field required by adding a ! in front of its name:

```
declare type local:my-type as {
  "foo" : "string",
  "!bar" : "integer"
};

validate type local:my-type* {
  { "foo" : "this is a string", "bar" : 42 },
  { "bar" : 1 },
  { "foo" : "this is yet another string", "bar" : 2 },
  { "foo" : "this is a string", "bar" : 1234 },
  { "foo" : "this is a string", "bar" : 42345 },
  { "foo" : "this is a string", "bar" : 42 }
}
```

Or you can provide a default value with the equal sign:

```
declare type local:my-type as {
  "foo" : "string=foobar",
  "!bar" : "integer"
};

validate type local:my-type* {
  { "foo" : "this is a string", "bar" : 42 },
  { "bar" : 1 },
  { "foo" : "this is yet another string", "bar" : 2 },
  { "foo" : "this is a string", "bar" : 1234 },
  { "foo" : "this is a string", "bar" : 42345 },
  { "foo" : "this is a string", "bar" : 42 }
}
```

## Extra fields

Extra fields will be rejected. However, the verbose version of JSound supports allowing extra fields (open objects) and will be supported in a future version of RumbleDB.

## Nested arrays

With the JSound comptact syntax, you can easily define nested array structures:


```
declare type local:my-type as {
  "foo" : "string",
  "!bar" : [ "integer" ]
};

validate type local:my-type* {
  { "foo" : "this is a string", "bar" : [ 42, 1234 ] },
  { "bar" : [ 1 ] },
  { "foo" : "this is yet another string", "bar" : [ 2 ] },
  { "foo" : "this is a string", "bar" : [ ] },
  { "foo" : "this is a string", "bar" : [ 1, 2, 3, 4, 5, 6 ] },
  { "foo" : "this is a string", "bar" : [ 42 ] }
}
```

You can even further nest objects:


```
declare type local:my-type as {
  "foo" : { "bar" : "integer" },
  "!bar" : [ { "first" : "string", "last" : "string" } ]
};

validate type local:my-type* {
  {
    "foo" : { "bar" : 1 },
    "bar" : [
      { "first" : "Albert", "last" : "Einstein" },
      { "first" : "Erwin", "last" : "Schrodinger" }
    ]
  },
  {
    "foo" : { "bar" : 2 },
    "bar" : [
      { "first" : "Alan", "last" : "Turing" },
      { "first" : "John", "last" : "Von Neumann" }
    ]
  },
  {
    "foo" : { "bar" : 3 },
    "bar" : [
    ]
  }
}
```

Or split your definitions into several types that refer to each other:

```
declare type local:person as {
  "first" : "string",
  "last" : "string"
};

declare type local:my-type as {
  "foo" : { "bar" : "integer" },
  "!bar" : [ "local:person" ]
};

validate type local:my-type* {
  {
    "foo" : { "bar" : 1 },
    "bar" : [
      { "first" : "Albert", "last" : "Einstein" },
      { "first" : "Erwin", "last" : "Schrodinger" }
    ]
  },
  {
    "foo" : { "bar" : 2 },
    "bar" : [
      { "first" : "Alan", "last" : "Turing" },
      { "first" : "John", "last" : "Von Neumann" }
    ]
  },
  {
    "foo" : { "bar" : 3 },
    "bar" : [
    ]
  }
}
```

## DataFrames

In fact, RumbleDB will internally convert the sequence of objects to a Spark DataFrame, leading to faster execution times.

In other words, the JSound Compact Schema Syntax is perfect for defining DataFrames schema!

## Verbose syntax

For advanced JSound features, such as open object types or subtypes, the verbose syntax must be used, like so: 

```
declare type local:x as jsound verbose {
  "kind" : "object",
  "baseType" : "object",
  "content" : [
    { "name" : "foo", "type" : "integer" }
  ],
  "closed" : false
};

declare type local:y as jsound verbose {
  "kind" : "object",
  "baseType" : "local:x",
  "content" : [
    { "name" : "bar", "type" : "date" }
  ],
  "closed" : true
};
```

The JSound type system, as its name indicates, is sound: you can only make subtypes more restrictive than the super type. The
complete specification of both syntaxes is available on the [JSound website](https://www.jsound-spec.org/).

In the feature, RumbleDB will support user-defined atomic types and union types via the verbose syntax.

## What's next?

Once you have validated your data as a dataframe with a user-defined type, you are all set to use the RumbleDB ML Machine Learning library and feed it through ML pipelines!
