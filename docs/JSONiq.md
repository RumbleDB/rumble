# JSONiq

RumbleDB relies on the JSONiq language.

## JSONiq reference

The complete specification can be found [here](http://www.jsoniq.org/docs/JSONiq/webhelp/index.html) on the [JSONiq.org](http://www.jsoniq.org) website. The implementation is now in a very advanced stage and there remain only few unsupported core JSONiq features.

## JSONiq tutorial

A tutorial can be found [here](https://github.com/ghislainfourny/jsoniq-tutorial). All queries in this tutorial will work with RumbleDB.

## JSONiq tutorial for Python users

A tutorial aimed at Python users can be found [here](https://github.com/ghislainfourny/jsoniq-tutorial-python). Please keep in mind, though, that examples using not supported features may not work (see below).

## Nested FLWOR expressions

FLWOR expressions now support nestedness, for example like so:

```
let $x := for $x in json-lines("file.json")
          where $x.field eq "foo"
          return $x
return count($x)
```

However, keep in mind that parallelization cannot be nested in Spark (there cannot be a job within a job), that is, the following will not work:

```
for $x in json-lines("file1.json")
let $z := for $y in json-lines("file2.json")
          where $y.foo eq $x.fbar
          return $y
return count($z)
```


## Expressions pushed down to Spark

Many expressions are pushed down to Spark out of the box. For example, this will work on a large file leveraging the parallelism of Spark:

```
count(json-lines("file.json")[$$.field eq "foo"].bar[].foo[[1]])
```

What is pushed down so far is:

- FLWOR expressions (as soon as a for clause is encountered, binding a variable to a sequence generated with json-lines() or parallelize())
- aggregation functions such as count
- JSON navigation expressions: object lookup (as well as keys() call), array lookup, array unboxing, filtering predicates
- predicates on positions, include use of context-dependent functions position() and last(), e.g.,
- type checking (instance of, treat as)
- many builtin function calls (head, tail, exist, etc)

```
json-lines("file.json")[position() ge 10 and position() le last() - 2]
```

More expressions working on sequences will be pushed down in the future, prioritized on the feedback we receive.

We also started to push down some expressions to DataFrames and Spark SQL (obtained via structured-json-lines, csv-file and parquet-file calls). In particular, keys() pushes down the schema lookup if used on parquet-file() and structured-json-lines(). Likewise, count() as well as object lookup, array unboxing and array lookup is also pushed down on DataFrames.

When an expression does not support pushdown, it will materialize automaticaly. To avoid issues, the materializion is capped by default at 200 items, but this can be changed on the command line with --materialization-cap. A warning is issued if a materialization happened and the sequence was truncated on screen. An error is thrown if this happens within a query.

## External global variables.

Prologs with user-defined functions and global variables are supported. Global external variables are supported (use "--variable:foo bar" on the command line to assign values to them). If the declared type is not string, then the literal supplied
on the command line is cast. If the declared type is anyURI, the path supplied on the command line is also resolved against
the working directory to an absolute URI. Thus, anyURI should be used to supply paths dynamically through an external variable.

Context item declarations are supported and a global context item value can be passed with the "--context-item" or "-I" parameter on the command line.

## Library modules

Library modules are now supported (experimental, please report bugs), and their namespace URI is used for resolution. If it is relative, it is resolved against the importing module location.

The same schemes are supported as for reading queries and data: file, hdfs, and so on. HTTP is also supported: you can import modules from the Web!

Example of library module (the file name is library-module.jq):

```
module namespace m = "library-module.jq";

declare variable $m:x := 2;

declare function mod:func($v) {
  $m:x + $v
);
```

Example of importing module (assuming it is in the same directory):

```
import module namespace mod = "library-module.jq";

mod:func($mod:x)
```

### Try/catch

Try/catch expressions are supported. Error codes are in the default, RumbleDB namespace and do not need prefixes.

```
try { 1 div 0 } catch FOAR0001 { "Division by zero!" }
```


### Supported types

The JSONiq type system is fully supported. Below is a complete list of JSONiq types and their support status. All builtin types are in the default type namespace, so that no prefix is needed. These types are defined in the XML Schema standard. Note that some
types specific to XML (e.g., NOTATION, NMTOKENS, NMTOKEN, ID, IDREF, ENTITY, etc) are not part of the JSONiq standard and
not supported by RumbleDB.

| Type               | Status          |
|--------------------|-----------------|
| atomic             | JSONiq 1.0 only |
| anyAtomicType      | supported       |
| anyURI             | supported       |
| base64Binary       | supported       |
| boolean            | supported       |
| byte               | supported       |
| date               | supported       |
| dateTime           | supported       |
| dateTimeStamp      | supported       |
| dayTimeDuration    | supported       |
| decimal            | supported       |
| double             | supported       |
| duration           | supported       |
| float              | supported       |
| gDay               | supported       |
| gMonth             | supported       |
| gYear              | supported       |
| gYearMonth         | supported       |
| hexBinary          | supported       |
| int                | supported       |
| integer            | supported       |
| long               | supported       |
| negativeInteger    | supported       |
| nonPositiveInteger | supported       |
| nonNegativeInteger | supported       |
| numeric            | supported       |
| positiveInteger    | supported       |
| short              | supported       |
| string             | supported       |
| time               | supported       |
| unsignedByte       | supported       |
| unsignedInt        | supported       |
| unsignedLong       | supported       |
| unsignedShort      | supported       |
| yearMonthDuration  | supported       |

## Unsupported/Unimplemented features (beta release)

Most core features of JSONiq are now in place, and we are working on getting the last (less used) ones into RumbleDB as well. We prioritize their implementation on user requests.

### Prolog

Some prolog settings (base URI, ordering mode, decimal format, namespace declarations) are not supported yet.

Location hints for the resolution of modules are not supported yet.

### FLWOR features

Window clauses are not supported, because they are not compatible with the Spark execution model.

### Function types

Function type syntax is supported.

Function annotations are not supported (%public, %private...), but this is planned.

### Builtin functions

Most JSONiq and XQuery builtin functions are now supported (see function documentation), except XML-specific functions. A few are
still missing, do not hesitate to reach out if you need them.

Constructors for atomic types are fully supported.

Buitin functions cannot yet be used with named function reference expressions (example: concat#2).

### Error variables

Error variables ($err:code, ...) for inside catch blocks are not supported.

### Updates and scripting

There are future plans to support JSONiq updates and scripting.
