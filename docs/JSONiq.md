#JSONiq

Sparksoniq relies on the JSONiq language.

## JSONiq reference

The complete specification can be found [here](http://www.jsoniq.org/docs/JSONiq/html-single/index.html) on the [JSONiq.org](jsoniq.org) website. Note that it is not fully implemented yet (see below).

## JSONiq tutorial

A tutorial can be found [here](https://github.com/ghislainfourny/jsoniq-tutorial). Most queries in this tutorial will work with Sparksoniq, except those that:
- use functions like concat, string-join or substring.
- use general comparison operators: you should replace = with eq and < with lt, etc.
- the complex FLWOR query does not work (yet), because of unsupported nesting.

## Unsupported/Unimplemented features (alpha release)

- Prologs are not supported. This includes user-defined functions, global variables, settings.
- Library modules are not supported. (no import available in the prolog).
- Try/catch expressions. Exceptions raised remotely may not be displayed in a user-friendly way yet, but we are working on it.
- Cast/treat expressions
- The type system is not complete yet: we have objects, arrays, strings, numbers, booleans and null, but not yet: dates, durations, binaries and so on.
- Advanced object lookup: nested expressions on the rhs of the dot syntax are not supported yet.
- Positional variables in FLWOR expressions. This is hard to parallelize (for the moment).
- FLWOR expressions may sometimes raise errors, especially if nested. We are also working on generalized FLWOR supported, also local.
