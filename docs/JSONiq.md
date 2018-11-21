# JSONiq

Sparksoniq relies on the JSONiq language.

## JSONiq reference

The complete specification can be found [here](http://www.jsoniq.org/docs/JSONiq/html-single/index.html) on the [JSONiq.org](http://jsoniq.org) website. Note that it is not fully implemented yet (see below).

## JSONiq tutorial

A tutorial can be found [here](https://github.com/ghislainfourny/jsoniq-tutorial). Most queries in this tutorial will work with Sparksoniq, except that the complex FLWOR query does not work (yet), because of unsupported nesting.

## JSONiq tutorial for Python users

A tutorial aimed at Python users can be found [here](https://github.com/ghislainfourny/jsoniq-tutorial-python). Please keep in mind, though, that examples using not supported features may not work (see below).

## Unsupported/Unimplemented features (alpha release)

Many core features of JSONiq are in place, but please be aware that the function features are not yet implemented. We are working on them for subsequent releases.

### Unsupported nested FLWOR expressions

FLWOR expressions may sometimes raise errors, especially if nested. We are also working on generalized FLWOR supported, also local. Instead of using lets, you need to inline the expressions.

Do NOT do

```
let $x := for $x in json-file("file.json")
          where $x.field eq "foo"
          return $x
return count($x)
```

Do instead:

```
count(for $x in json-file("file.json")
      where $x.field eq "foo"
      return $x)
```

### Expressions NOT pushed down to Spark yet

Some expressions are not yet pushed down to Spark (but this will come soon!).

Do NOT do

```
json-file("file.json")[$$.field eq "foo"].bar[]
```

Do instead use an explicit FLWOR expression:

```
for $x in json-file("file.json")
where $x.field eq "foo"
return $x.bar[]
```

### Unsupported prologs

Prologs are not supported. This includes user-defined functions, global variables, settings and library modules.

### Unsupported try/catch

Try/catch expressions. Exceptions raised remotely may not be displayed in a user-friendly way yet, but we are working on it.

### Unsupported cast/treat-as

Cast/treat-as expressions.

### Unsupported nested expressions in object lookups (rhs)

Nested object lookup keys: nested expressions on the rhs of the dot syntax are not supported yet.

### Unsupported count clauses

 Count clauses in FLWOR expressions. This feature will arrive very soon, though.

### Unsupported types

The type system is not complete yet: we have objects, arrays, strings, numbers, booleans and null, but not yet: dates, durations, binaries and so on.

### Unsupported functions

Not all JSONiq functions are supported (see function documentation), even though they get added continuously. Please take a look at the function library documentation to know which functions are available.

### Unsupported updates and scripting

JSON updates are not supported. Scripting features (assignment, while loop, ...) are not supported yet either.
