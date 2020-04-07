# JSONiq

Rumble relies on the JSONiq language.

## JSONiq reference

The complete specification can be found [here](http://www.jsoniq.org/docs/JSONiq/html-single/index.html) on the [JSONiq.org](http://jsoniq.org) website. Note that it is not fully implemented yet (see below).

## JSONiq tutorial

A tutorial can be found [here](https://github.com/ghislainfourny/jsoniq-tutorial). All queries in this tutorial will work with Rumble.

## JSONiq tutorial for Python users

A tutorial aimed at Python users can be found [here](https://github.com/ghislainfourny/jsoniq-tutorial-python). Please keep in mind, though, that examples using not supported features may not work (see below).

## Unsupported/Unimplemented features (alpha release)

Many core features of JSONiq are in place, but please be aware that some features (less and less, though) are not yet implemented. We are working on them for subsequent releases.

### Nested FLWOR expressions

FLWOR expressions now support nestedness, for example like so:

```
let $x := for $x in json-file("file.json")
          where $x.field eq "foo"
          return $x
return count($x)
```

However, keep in mind that parallelization cannot be nested in Spark (there cannot be a job within a job), that is, the following will not work:

```
for $x in json-file("file1.json")
let $z := for $y in json-file("file2.json")
          where $y.foo eq $x.fbar
          return $y
return count($z)
```


### Expressions pushed down to Spark

Many expressions are pushed down to Spark out of the box. For example, this will work on a large file leveraging the parallelism of Spark:

```
count(json-file("file.json")[$$.field eq "foo"].bar[].foo[[1]])
```

What is pushed down so far is:

- FLWOR expressions (as soon as a for clause is encountered, binding a variable to a sequence generated with json-file() or parallelize())
- aggregation functions such as count
- JSON navigation expressions: object lookup (as well as keys() call), array lookup, array unboxing, filtering predicates
- predicates on positions, include use of context-dependent functions position() and last(), e.g.,
- type checking (instance of, treat as)
- many builtin function calls (head, tail, exist, etc)

```
json-file("file.json")[position() ge 10 and position() le last() - 2]
```

More expressions working on sequences will be pushed down in the future, prioritized on the feedback we receive.

We also started to push down some expressions to DataFrames and Spark SQL (obtained via structured-json-file, csv-file and parquet-file calls). In particular, keys() pushes down the schema lookup if used on parquet-file() and structured-json-file(). Likewise, count() as well as object lookup, array unboxing and array lookup is also pushed down on DataFrames.

When an expression does not support pushdown, it will materialize automaticaly. To avoid issues, the materializion is capped by default at 100 items, but this can be changed on the command line with --result-size. A warning is issued if a materialization happened and the sequence was truncated.

### Unsupported global variables, settings and modules

Prologs with user-defined functions are now supported, but not yet global global variables, settings and library modules.

Global external variables with string values are supported (use "--variable:foo bar" on the command line to assign values to them).

Dynamic functions (aka, function items that can be passed as values and dynamically called) are supported.

All function calls are type-checked.

### Unsupported try/catch

Try/catch expressions. Exceptions raised remotely may not be displayed in a user-friendly way yet, but we are working on it.

### Unsupported nested expressions in object lookups (rhs)

Nested object lookup keys: nested expressions on the rhs of the dot syntax are not supported yet.

### Unsupported types

The type system is not quite complete yet, although a lot of progress was made. Below is a complete list of JSONiq types and their support status.

|  Type | Status |
|-------|--------|
| atomic | supported |
| anyURI | supported |
| base64Binary | supported |
| boolean | supported |
| byte | not supported |
| date | supported |
| dateTime | supported |
| dateTimeStamp | not supported |
| dayTimeDuration | supported |
| decimal | supported |
| double | supported |
| duration | supported |
| float | not supported |
| gDay | not supported |
| gMonth | not supported |
| gYear | not supported |
| gYearMonth | not supported |
| hexBinary | supported |
| int | not supported |
| integer | supported |
| long | not supported |
| negativeInteger | not supported |
| nonPositiveInteger | not supported |
| nonNegativeInteger | not supported |
| positiveInteger | not supported |
| short | not supported |
| string | supported |
| time | supported |
| unsignedByte | not supported |
| unsignedInt | not supported |
| unsignedLong | not supported |
| unsignedShort | not supported |
| yearMonthDuration | supported |


### Unsupported functions

Not all JSONiq functions in the library are supported (see function documentation), even though they get added continuously. Please take a look at the function library documentation to know which functions are available.

### Unsupported updates and scripting

JSON updates are not supported. Scripting features (assignment, while loop, ...) are not supported yet either.
