# JSONiq

Sparksoniq relies on the JSONiq language.

## JSONiq reference

The complete specification can be found [here](http://www.jsoniq.org/docs/JSONiq/html-single/index.html) on the [JSONiq.org](http://jsoniq.org) website. Note that it is not fully implemented yet (see below).

## JSONiq tutorial

A tutorial can be found [here](https://github.com/ghislainfourny/jsoniq-tutorial). All queries in this tutorial will work with Sparksoniq.

## JSONiq tutorial for Python users

A tutorial aimed at Python users can be found [here](https://github.com/ghislainfourny/jsoniq-tutorial-python). Please keep in mind, though, that examples using not supported features may not work (see below).

## Unsupported/Unimplemented features (alpha release)

Many core features of JSONiq are in place, but please be aware that the function features are not yet implemented. We are working on them for subsequent releases.

### Nested FLWOR expressions

FLWOR expressions now supported nestedness, for example like so:

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

Some expressions are pushed down to Spark out of the box. For example, this will work on a large file leveraging the parallelism of Spark:

```
count(json-file("file.json")[$$.field eq "foo"].bar[].foo[[1]])
```

What is pushed down so far is:

- FLWOR expressions (as soon as a for clause is encountered, binding a variable to a sequence generated with json-file() or parallelize())
- aggregation functions: count, sum, avg, max, min
- JSON navigation expressions: object lookup, array lookup, array unboxing, filtering predicates

More expressions working on sequences will be pushed down in the future, partly depending on the feedback we receive.

When an expression does not support pushdown, it will materialize automaticaly. To avoid issues, the materializion is capped by default at 100 items, but this can be changed on the command line with --result-size. A warning is issued if a materialization happened and the sequence was truncated.

### Unsupported prologs

Prologs are not supported. This includes user-defined functions, global variables, settings and library modules.

### Unsupported try/catch

Try/catch expressions. Exceptions raised remotely may not be displayed in a user-friendly way yet, but we are working on it.

### Unsupported cast/treat-as

Cast/treat-as expressions.

### Unsupported nested expressions in object lookups (rhs)

Nested object lookup keys: nested expressions on the rhs of the dot syntax are not supported yet.

### Unsupported types

The type system is not complete yet: we have objects, arrays, strings, numbers, booleans and null, but not yet: dates, durations, binaries and so on.

### Unsupported functions

Not all JSONiq functions are supported (see function documentation), even though they get added continuously. Please take a look at the function library documentation to know which functions are available.

### Unsupported updates and scripting

JSON updates are not supported. Scripting features (assignment, while loop, ...) are not supported yet either.
