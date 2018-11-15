# JSONiq

Sparksoniq relies on the JSONiq language.

## JSONiq reference

The complete specification can be found [here](http://www.jsoniq.org/docs/JSONiq/html-single/index.html) on the [JSONiq.org](http://jsoniq.org) website. Note that it is not fully implemented yet (see below).

## JSONiq tutorial

A tutorial can be found [here](https://github.com/ghislainfourny/jsoniq-tutorial). Most queries in this tutorial will work with Sparksoniq, except that:

- the complex FLWOR query does not work (yet), because of unsupported nesting.

## Unsupported/Unimplemented features (alpha release)

Many core features of JSONiq are in place, but please be aware that the function features are not yet implemented. We are working on them for subsequent releases.

- Prologs are not supported. This includes user-defined functions, global variables, settings and library modules.

- Try/catch expressions. Exceptions raised remotely may not be displayed in a user-friendly way yet, but we are working on it.
- Cast/treat-as expressions.
- Nested object lookup keys: nested expressions on the rhs of the dot syntax are not supported yet.
- Count clauses in FLWOR expressions. This is hard to parallelize (for the moment).

- The type system is not complete yet: we have objects, arrays, strings, numbers, booleans and null, but not yet: dates, durations, binaries and so on.
- FLWOR expressions may sometimes raise errors, especially if nested. We are also working on generalized FLWOR supported, also local.
- Not all JSONiq functions are supported (see function documentation).

