# Function library

We list here the most important functions supported by RumbleDB, and introduce them by means of examples. Highly detailed specifications can be found in the [underlying W3C standard](https://www.w3.org/TR/xpath-functions-30/#func-floor), unless the function is marked as specific to JSON or RumbleDB, in which case it can be found [here](http://www.jsoniq.org/docs/JSONiq/html-single/index.html#idm34604304). JSONiq and RumbleDB intentionally do not support builtin functions on XML nodes, NOTATION or QNames. RumbleDB supports almost all other W3C-standardized functions, please contact us if you are still missing one.

For the sake of ease of use, all W3C standard builtin functions and JSONiq builtin functions are in the
RumbleDB namespace, which is the default function namespace and does not require any prefix in front of function names.

It is recommended that user-defined functions are put in the local namespace, i.e., their name should have the local: prefix (which is predefined). Otherwise, there is the risk that your code becomes incompatible with subsequent releases if new (unprefixed) builtin functions are introduced.

## Errors and diagnostics
### Diagnostic tracing

### trace
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-trace)

Fully implemented

```
trace(1 to 3)
```

returns (1, 2, 3) and logs it in the log-path if specified

## Functions and operators on numerics
### Functions on numeric values

### abs
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-abs)

Fully implemented

```
abs(-2)
```

returns 2.0

### ceiling
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-ceiling)

Fully implemented
```
ceiling(2.3)
```

returns 3.0

### floor
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-floor)

Fully implemented
```
floor(2.3)
```

returns 2.0

### round
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-round)

Fully implemented
```
round(2.3)
```

returns 2.0

```
round(2.2345, 2)
```

returns 2.23

### round-half-to-even
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-round-half-to-even)

Fully implemented

```
round-half-to-even(2.2345, 2), round-half-to-even(2.2345)
```

### Parsing numbers

### number
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-number)

Fully implemented

```
number("15")
```
returns 15 as a double

```
number("foo")
```
returns NaN as a double

```
number(15)
```
returns 15 as a double


### Formatting integers

### format-integer
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-format-integer)

Not implemented

##Formatting numbers

### format-number
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-format-number)

Not implemented

##Trigonometric and exponential functions

###pi
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-pi)

Fully implemented
```
pi()
```
returns 3.141592653589793

###exp
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-exp)

Fully implemented

```
exp(10)
```


###exp10
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-exp10)

Fully implemented

```
exp10(10)
```


### log
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-log)

Fully implemented

```
log(100)
```


### log10
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-log10)

Fully implemented

```
log10(100)
```

### pow
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-pow)

Fully implemented

```
pow(10, 2)
```

### sqrt
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-sqrt)

Fully implemented

```
sqrt(4)
```

returns 2



### sin
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-sin)

Fully implemented

```
sin(pi())
```

### cos
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-cos)

Fully implemented

```
cos(pi())
```


### cosh
JSONiq-specific. Fully implemented

```
cosh(pi())
```


### sinh
JSONiq-specific. Fully implemented

```
sinh(pi())
```


### tan
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-tan)

Fully implemented

```
tan(pi())
```

### asin
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-asin)

Fully implemented

```
asin(1)
```

### acos
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-acos)

Fully implemented
```
acos(1)
```


### atan
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-atan)

Fully implemented
```
atan(1)
```

### atan2
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-atan2)

Fully implemented
```
atan2(1)
```

### Random numbers
### random-number-generator
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-random-number-generator)

Not implemented


## Functions on strings

### Functions to assemble and disassemble strings

### string-to-codepoint
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-string-to-codepoint)

Fully implemented

```
string-to-codepoints("Thérèse")
```
returns (84, 104, 233, 114, 232, 115, 101)

```
string-to-codepoints("")
```
returns ()

### codepoints-to-string
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-codepoints-to-string)

Fully implemented
```
codepoints-to-string((2309, 2358, 2378, 2325))
```
returns "अशॊक"

```
codepoints-to-string(())
```
returns ""


### Comparison of strings

### compare
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-compare)

Fully implemented
```
compare("aa", "bb")
```
returns -1

### codepoint-equal

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-codepoint-equal)

Fully implemented

```
codepoint-equal("abcd", "abcd")
```
returns true

```
codepoint-equal("", ())
```
returns ()


### collation-key

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-collation-key)

Not implemented

### contains-token

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-contains-token)

Not implemented


### Functions on string values


### concat

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-concat)

Fully implemented

```
concat("foo", "bar", "foobar")
```

returns "foobarfoobar"


### string-join

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-string-join)

Fully implemented

```
string-join(("foo", "bar", "foobar"))
```

returns "foobarfoobar"

```
string-join(("foo", "bar", "foobar"), "-")
```

returns "foo-bar-foobar"

### substring
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-substring)

Fully implemented

```
substring("foobar", 4)
```

returns "bar"

```
substring("foobar", 4, 2)
```

returns "ba"

### string-length
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-string-length)

Fully implemented

Returns the length of the supplied string, or 0 if the empty sequence is supplied.

```
string-length("foo")
```

returns 3.

```
string-length(())
```

returns 0.

###normalize-space
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-normalize-space)

Fully implemented

Normalization of spaces in a string.

```
normalize-space(" The    wealthy curled darlings                                         of    our    nation. "),
```

returns "The wealthy curled darlings of our nation."

### normalize-unicode

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-normalize-unicode)

Fully implemented

Returns the value of the input after applying Unicode normalization.

```
normalize-unicode("hello world", "NFC")
```

returns the unicode-normalized version of the input string. Normalization forms NFC, NFD, NFKC, and NFKD are supported. "FULLY-NORMALIZED" though supported, should be used with caution as only the composition exclusion characters supported FULLY-NORMALIZED are which are uncommented in the [following file](https://www.unicode.org/Public/UCD/latest/ucd/CompositionExclusions.txt).


### upper-case

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-upper-case)

Fully implemented

```
upper-case("abCd0")
```
returns "ABCD0"

### lower-case

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-lower-case)

Fully implemented

```
lower-case("ABc!D")
```
returns "abc!d"


### translate

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-translate)

Fully implemented

```
translate("bar","abc","ABC")
```
returns "BAr"

```
translate("--aaa--","abc-","ABC")
```
returns "AAA"


### Functions based on substring matching

### contains

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-contains)

Fully implemented

```
contains("foobar", "ob")
```

returns true.

### starts-with

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-starts-with)

Fully implemented

```
starts-with("foobar", "foo")
```

returns true

### ends-with

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-ends-with)

Fully implemented

```
ends-with("foobar", "bar")
```

returns true.


### substring-before

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-substring-before)

Fully implemented

```
substring-before("foobar", "bar")
```

returns "foo"

```
substring-before("foobar", "o")
```

returns "f"

### substring-after

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-substring-after)

Fully implemented

```
substring-after("foobar", "foo")
```

returns "bar"

```
substring-after("foobar", "r")
```

returns ""

### String functions that use regular expressions

### matches

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-matches)

Arity 2 implemented, arity 3 is not.

Regular expression matching. The semantics of regular expressions are those of Java's Pattern class.

```
matches("foobar", "o+")
```

returns true.

```
matches("foobar", "^fo+.*")
```

returns true.


### replace

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-replace)

Arity 3 implemented, arity 4 is not.

Regular expression matching and replacing. The semantics of regular expressions are those of Java's Pattern class.

```
replace("abracadabra", "bra", "*")
```

returns "a\*cada\*"

```
replace("abracadabra", "a(.)", "a$1$1")
```

returns "abbraccaddabbra"

### tokenize

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-tokenize)

Arity 2 implemented, arity 3 is not.


```
tokenize("aa bb cc dd")
```

returns ("aa", "bb", "cc", "dd")

```
tokenize("aa;bb;cc;dd", ";")
```

returns ("aa", "bb", "cc", "dd")

### analyze-string

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-analyze-string)

Not implemented

## Functions that manipulate URIs

### resolve-uri

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-resolve-uri)

Fully implemented

```
string(resolve-uri("examples","http://www.examples.com/"))
```
returns http://www.examples.com/examples

### encode-for-uri

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-encode-for-uri)

Fully implemented

```
encode-for-uri("100% organic")
```
returns 100%25%20organic

### iri-to-uri

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-iri-to-uri)

Not implemented

### escape-html-uri

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-escape-html-uri)

Not implemented

## Functions and operators on Boolean values

### Boolean constant functions

### true

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-true)

Fully implemented

```
fn:true()
```
returns true

### false

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-false)

Fully implemented

```
fn:false()
```
returns false

### boolean

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-boolean)

Fully implemented

```
boolean(9)
```
returns true

```
boolean("")
```
returns false

### not

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-not)

Fully implemented

```
not(9)
```
returns false

```
boolean("")
```
returns true

## Functions and operators on durations

### Component extraction functions on durations

### years-from-duration

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-years-from-duration)

Fully implemented

```
years-from-duration(duration("P2021Y6M"))
```

returns 2021.

### months-from-duration

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-months-from-duration)

Fully implemented

```
months-from-duration(duration("P2021Y6M"))
```

returns 6.

### days-from-duration

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-days-from-duration)

Fully implemented

```
days-from-duration(duration("P2021Y6M17D"))
```

returns 17.

### hours-from-duration

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-hours-from-duration)

Fully implemented

```
hours-from-duration(duration("P2021Y6M17DT12H35M30S"))
```

returns 12.


### minutes-from-duration

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-minutes-from-duration)

Fully implemented

```
minutes-from-duration(duration("P2021Y6M17DT12H35M30S"))
```

returns 35.

### seconds-from-duration

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-seconds-from-duration)

Fully implemented

```
minutes-from-duration(duration("P2021Y6M17DT12H35M30S"))
```

returns 30.


## Functions and operators on dates and times

### Constructing a DateTime

### dateTime

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-dateTime)

Fully implemented

```
dateTime("2004-04-12T13:20:00+14:00")
```

returns 2004-04-12T13:20:00+14:00

### Component extraction functions on dates and times

### year-from-dateTime

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-year-from-dateTime)

Fully implemented

```
year-from-dateTime(dateTime("2021-04-12T13:20:32.123+02:00"))
```
returns 2021.

### month-from-dateTime

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-month-from-dateTime)

Fully implemented

```
month-from-dateTime(dateTime("2021-04-12T13:20:32.123+02:00"))
```
returns 04.

### day-from-dateTime

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-day-from-dateTime)

Fully implemented

```
day-from-dateTime(dateTime("2021-04-12T13:20:32.123+02:00"))
```
returns 12.

### hours-from-dateTime

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-hours-from-dateTime)

Fully implemented

```
hours-from-dateTime(dateTime("2021-04-12T13:20:32.123+02:00"))
```
returns 13.

### minutes-from-dateTime

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-minutes-from-dateTime)

Fully implemented

```
minutes-from-dateTime(dateTime("2021-04-12T13:20:32.123+02:00"))
```
returns 20.

### seconds-from-dateTime

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-seconds-from-dateTime)

Fully implemented

```
seconds-from-dateTime(dateTime("2021-04-12T13:20:32.123+02:00"))
```
returns 32.

### timezone-from-dateTime

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-timezone-from-dateTime)

Fully implemented

```
timezone-from-dateTime(dateTime("2021-04-12T13:20:32.123+02:00"))
```
returns PT2H.


### year-from-date

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-year-from-date)

Fully implemented

```
year-from-date(date("2021-06-04"))
```
returns 2021.

### month-from-date

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-month-from-date)

Fully implemented

```
month-from-date(date("2021-06-04"))
```
returns 6.

### day-from-date

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-day-from-date)

Fully implemented

```
day-from-date(date("2021-06-04"))
```
returns 4.

### timezone-from-date

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-timezone-from-date)

Fully implemented

```
timezone-from-date(date("2021-06-04-14:00"))
```
returns -PT14H.

### hours-from-time

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-hours-from-time)

Fully implemented

```
hours-from-time(time("13:20:32.123+02:00"))
```
returns 13.

### minutes-from-time

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-minutes-from-time)

Fully implemented

```
minutes-from-time(time("13:20:32.123+02:00"))
```
returns 20.

### seconds-from-time

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-seconds-from-time)

Fully implemented

```
seconds-from-time(time("13:20:32.123+02:00"))
```
returns 32.123.

### timezone-from-time

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-timezone-from-time)

Fully implemented

```
timezone-from-time(time("13:20:32.123+02:00"))
```
returns PT2H.


### Timezone adjustment functions on dates and time values

### adjust-dateTime-to-timezone

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-adjust-dateTime-to-timezone)

Fully implemented

```
adjust-dateTime-to-timezone(dateTime("2004-04-12T13:20:15+14:00"), dayTimeDuration("PT4H5M"))
```
returns 2004-04-12T03:25:15+04:05.

### adjust-date-to-timezone

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-adjust-date-to-timezone)

Fully implemented

```
adjust-date-to-timezone(date("2014-03-12"), dayTimeDuration("PT4H"))
```
returns 2014-03-12+04:00.

### adjust-time-to-timezone

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-adjust-time-to-timezone)

Fully implemented

```
adjust-time-to-timezone(time("13:20:00-05:00"), dayTimeDuration("-PT14H"))
```
returns 04:20:00-14:00.


### Formatting dates and times functions

The functions in this section accept a simplified version of the picture string, in which a variable marker accepts only:

* One of the following component specifiers: Y, M, d, D, F, H, m, s, P
* A first presentation modifier, for which the value can be:
  * Nn, for all supported component specifiers, besides P
  * N, if the component specifier is P
  * a format token that indicates a numbering sequence of the the following form: '0001'
* A second presentation modifier, for which the value can be t or c, which are also the default values
* A width modifier, both minimum and maximum values

### format-dateTime

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-format-dateTime)

Fully implemented

```
format-dateTime(dateTime("2004-04-12T13:20:00"), "[m]-[H]-[D]-[M]-[Y]")
```

returns 20-13-12-4-2004

### format-date

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-format-date)

Fully implemented

```
format-date(date("2004-04-12"), "[D]-[M]-[Y]")
```

returns 12-4-2004

### format-time

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-format-time)

Fully implemented

```
format-time(time("13:20:00"), "[H]-[m]-[s]")
```

returns 13-20-0


## Functions related to QNames

Not implemented


## Functions and operators on sequences

### General functions and operators on sequences

### empty

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-empty)

Fully implemented

Returns a boolean whether the input sequence is empty or not.

```
empty(1 to 10)
```

returns false.

### exists

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-exists)

Fully implemented

Returns a boolean whether the input sequence has at least one item or not.

```
exists(1 to 10)
```

returns true.


```
exists(())
```

returns false.

This is pushed down to Spark and works on big sequences.

```
exists(json-lines("file.json"))
```

### head

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-head)

Fully implemented


Returns the first item of a sequence, or the empty sequence if it is empty.

```
head(1 to 10)
```

returns 1.


```
head(())
```

returns ().

This is pushed down to Spark and works on big sequences.

```
head(json-lines("file.json"))
```

### tail

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-tail)

Fully implemented


Returns all but the last item of a sequence, or the empty sequence if it is empty.

```
tail(1 to 5)
```

returns (2, 3, 4, 5).

```
tail(())
```

returns ().

This is pushed down to Spark and works on big sequences.

```
tail(json-lines("file.json"))
```


### insert-before

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-insert-before)

Fully implemented

```
insert-before((3, 4, 5), 0, (1, 2))
```
returns (1, 2, 3, 4, 5).

### remove

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-remove)

Fully implemented

```
remove((1, 2, 10), 3)
```

returns (1, 2).

### reverse

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-reverse)

Fully implemented

```
remove((1, 2, 3))
```

returns (3, 2, 1).

### subsequence

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-subsequence)

Fully implemented

```
subsequence((1, 2, 3), 2, 5)
```

returns (2, 3).

### unordered

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-unordered)

Fully implemented

```
unordered((1, 2, 3))
```

returns (1, 2, 3).


### Functions that compare values in sequences

### distinct-values

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-distinct-values)

Fully implemented

Eliminates duplicates from a sequence of atomic items.

```
distinct-values((1, 1, 4, 3, 1, 1, "foo", 4, "foo", true, 3, 1, true, 5, 3, 1, 1))
```

returns (1, 4, 3, "foo", true, 5).


This is pushed down to Spark and works on big sequences.

```
distinct-values(json-lines("file.json").foo)
```

```
distinct-values(text-file("file.txt"))
```

### index-of

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-index-of)

Fully implemented

```
index-of((10, 20, 30, 40), 30)
```
returns 3.

```
index-of((10, 20, 30, 40), 35)
```
returns "".


### deep-equal

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-deep-equal)

Fully implemented

```
deep-equal((10, 20, "a"), (10, 20, "a"))
```
returns true.

```
deep-equal(("b", "0"), ("b", 0))
```
returns false.

### Functions that test the cardinality of sequences

### zero-or-one

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-zero-or-one)

Fully implemented

```
zero-or-one(("a"))
```
returns "a".

```
zero-or-one(("a", "b"))
```
returns an error.

### one-or-more

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-one-or-more)

Fully implemented

```
one-or-more(("a"))
```
returns "a".

```
one-or-more(())
```
returns an error.

### exactly-one

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-exactly-one)

Fully implemented

```
exactly-one(("a"))
```
returns "a".

```
exactly-one(("a", "b"))
```
returns an error.


### Aggregate functions

### count

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-count)

Fully implemented

```
let $x := (1, 2, 3, 4)
return count($x)
```

returns 4.

Count calls are pushed down to Spark, so this works on billions of items as well:

```
count(json-lines("file.json"))
```

```
count(
  for $i in json-lines("file.json")
  where $i.foo eq "bar"
  return $i
)
```

### avg

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-avg)

Fully implemented

```
let $x := (1, 2, 3, 4)
return avg($x)
```

returns 2.5.

Avg calls are pushed down to Spark, so this works on billions of items as well:

```
avg(json-lines("file.json").foo)
```




### max

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-max)

Fully implemented

```
let $x := (1, 2, 3, 4)
return max($x)
```

returns 4.

```
for $i in 1 to 3
return max($i)
```

returns (1, 2, 3).


Max calls are pushed down to Spark, so this works on billions of items as well:

```
max(json-lines("file.json").foo)
```

### min

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-min)

Fully implemented

```
let $x := (1, 2, 3, 4)
return min($x)
```

returns 1.

```
for $i in 1 to 3
return min($i)
```

returns (1, 2, 3).

Min calls are pushed down to Spark, so this works on billions of items as well:

```
min(json-lines("file.json").foo)
```


### sum

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-sum)

Fully implemented

```
let $x := (1, 2, 3, 4)
return sum($x)
```

returns 10.

Sum calls are pushed down to Spark, so this works on billions of items as well:

```
sum(json-lines("file.json").foo)
```


### Functions giving access to external information

### doc

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-doc)

Fully implemented

```
doc("path/to/file.xml")
```

Returns the corresponding document node

### collection

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-collection)

Not implemented

### Parsing and serializing

### serialize

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-serialize)

Fully implemented

Serializes the supplied input sequence, returning the serialized representation of the sequence as a string

```
serialize({hello: "world"})
```
returns { "hello" : "world" }

## Context Functions

### position
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-position)

Fully implemented

```
(1 to 10)[position() eq 5]
```

returns 5

### last
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-last)

Fully implemented
```
(1 to 10)[position() eq last()]
```
returns 10


```
(1 to 10)[last()]
```
returns 10

### current-dateTime

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-current-dateTime)

Fully implemented

```
current-dateTime()
```

returns 2020-02-26T11:22:48.423+01:00

### current-date
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-current-date)

Fully implemented
```
current-date()
```

returns 2020-02-26Europe/Zurich

### current-time
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-current-time)

Fully implemented
```
current-time()
```

returns 11:24:10.064+01:00

### implicit-timezone
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-implicit-timezone)

Fully implemented
```
implicit-timezone()
```

returns PT1H.

### default-collation
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-default-collation)

Fully implemented
```
default-collation()
```

returns http://www.w3.org/2005/xpath-functions/collation/codepoint.


## High order functions

### Functions on functions

### function-lookup
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-function-lookup)

Not implemented

### function-name
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-function-name)

Not implemented

### function-arity
[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-function-arity)

Not implemented

### Basic higher-order functions

### for-each

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-function-for-each)

Not implemented

### filter

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-function-filter)

Not implemented

### fold-left

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-function-fold-left)

Not implemented

### fold-right

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-function-fold-right)

Not implemented

### for-each-pair

[W3C specification](https://www.w3.org/TR/xpath-functions-31/#func-function-for-each-pair)

Not implemented



## JSONiq functions

### keys

[JSONiq specification](https://www.jsoniq.org/docs/JSONiq/webhelp/index.html#ch08s01s01.html)

Fully implemented

```
keys({"foo" : "bar", "bar" : "foobar"})
```

returns ("foo", "bar").  Also works on an input sequence, eliminating duplicates

```
keys(({"foo" : "bar", "bar" : "foobar"}, {"foo": "bar2"}))
```

Keys calls are pushed down to Spark, so this works on billions of items as well:

```
keys(json-lines("file.json"))
```

### members
[JSONiq specification](https://www.jsoniq.org/docs/JSONiq/webhelp/index.html#ch08s01s02.html)

Fully implemented

```
members([1 to 100])
```

This function returns the members as an array, but not recursively, i.e., nested arrays are not unboxed.

Returns the first 100 integers as a sequence.  Also works on an input sequence, in a distributive way.

```
members(([1 to 100], [ 300 to 1000 ]))
```


### null

[JSONiq specification](https://www.jsoniq.org/docs/JSONiq/webhelp/index.html#ch08s01s03.html)

Fully implemented

```
null()
```

Returns a JSON null (also available as the literal null).


### parse-json
[JSONiq specification](https://www.jsoniq.org/docs/JSONiq/webhelp/index.html#ch08s01s04.html)

Fully implemented

### size
[JSONiq specification](https://www.jsoniq.org/docs/JSONiq/webhelp/index.html#ch08s01s05.html)

Fully implemented
```
size([1 to 100])
```

returns 100. Also works if the empty sequence is supplied, in which case it returns the empty sequence.

```
size(())
```


### accumulate
[JSONiq specification](https://www.jsoniq.org/docs/JSONiq/webhelp/index.html#ch08s01s06.html)

Fully implemented
```
accumulate(({ "b" : 2 }, { "c" : 3 }, { "b" : [1, "abc"] }, {"c" : {"d" : 0.17}}))
```
returns
```
{ "b" : [ 2, [ 1, "abc" ] ], "c" : [ 3, { "d" : 0.17 } ] }
```

### descendant-arrays
[JSONiq specification](https://www.jsoniq.org/docs/JSONiq/webhelp/index.html#ch08s01s06.html)

Fully implemented
```
descendant-arrays(([0, "x", { "a" : [1, {"b" : 2}, [2.5]], "o" : {"c" : 3} }]))
```
returns 
```
[ 0, "x", { "a" : [ 1, { "b" : 2 }, [ 2.5 ] ], "o" : {"c" : 3} } ]
[ 1, { "b" : 2 }, [ 2.5 ] ]
[ 2.5 ]
```

### descendant-objects
[JSONiq specification](https://www.jsoniq.org/docs/JSONiq/webhelp/index.html#ch08s01s07.html)

Fully implemented
```
descendant-objects(([0, "x", { "a" : [1, {"b" : 2}, [2.5]], "o" : {"c" : 3} }]))
```
returns 
```
{ "a" : [ 1, { "b" : 2 }, [ 2.5 ] ], "o" : { "c" : 3 } }
{ "b" : 2 }
{ "c" : 3 }
```
### descendant-pairs
[JSONiq specification](https://www.jsoniq.org/docs/JSONiq/webhelp/index.html#ch08s01s08.html)

Fully implemented
```
descendant-pairs(({ "a" : [1, {"b" : 2}], "d" : {"c" : 3} }))
```
returns 
```
{ "a" : [ 1, { "b" : 2 } ] }
{ "b" : 2 }
{ "d" : { "c" : 3 } }
{ "c" : 3 }
```

### flatten

[JSONiq specification](https://www.jsoniq.org/docs/JSONiq/webhelp/index.html#ch08s01s09.html)

Fully implemented
```
flatten(([1, 2], [[3, 4], [5, 6]], [7, [8, 9]]))
```

Unboxes arrays recursively, stopping the recursion when any other item is reached (object or atomic). Also works on an input sequence, in a distributive way.

Returns (1, 2, 3, 4, 5, 6, 7, 8, 9).


### intersect

[JSONiq specification](https://www.jsoniq.org/docs/JSONiq/webhelp/index.html#ch08s01s10.html)

Fully implemented
```
intersect(({"a" : "abc", "b" : 2, "c" : [1, 2], "d" : "0"}, { "a" : 2, "b" : "ab", "c" : "foo" }))
```
returns 
```
{ "a" : [ "abc", 2 ], "b" : [ 2, "ab" ], "c" : [ [ 1, 2 ], "foo" ] }
```

### project
[JSONiq specification](https://www.jsoniq.org/docs/JSONiq/webhelp/index.html#ch08s01s11.html)

Fully implemented

```
project({"foo" : "bar", "bar" : "foobar", "foobar" : "foo" }, ("foo", "bar"))
```

returns the object {"foo" : "bar", "bar" : "foobar"}. Also works on an input sequence, in a distributive way.

```
project(({"foo" : "bar", "bar" : "foobar", "foobar" : "foo" }, {"foo": "bar2"}), ("foo", "bar"))
```

### remove-keys
[JSONiq specification](https://www.jsoniq.org/docs/JSONiq/webhelp/index.html#ch08s01s12.html)

Fully implemented

```
remove-keys({"foo" : "bar", "bar" : "foobar", "foobar" : "foo" }, ("foo", "bar"))
```

returns the object {"foobar" : "foo"}. Also works on an input sequence, in a distributive way.

```
remove-keys(({"foo" : "bar", "bar" : "foobar", "foobar" : "foo" }, {"foo": "bar2"}), ("foo", "bar"))
```

### values
[JSONiq specification](https://www.jsoniq.org/docs/JSONiq/webhelp/index.html#ch08s01s13.html)

Fully implemented
```
values({"foo" : "bar", "bar" : "foobar"})
```

returns ("bar", "foobar").  Also works on an input sequence, in a distributive way.

```
values(({"foo" : "bar", "bar" : "foobar"}, {"foo" : "bar2"}))
```

Values calls are pushed down to Spark, so this works on billions of items as well:

```
values(json-lines("file.json"))
```

### encode-for-roundtrip
[JSONiq specification](https://www.jsoniq.org/docs/JSONiq/webhelp/index.html#ch08s01s13.html)

Not implemented

### decode-from-roundtrip
[JSONiq specification](https://www.jsoniq.org/docs/JSONiq/webhelp/index.html#ch08s01s14.html)

Not implemented


### json-doc


```
json-doc("/Users/sheldon/object.json")
```

returns the (unique) JSON value parsed from a local JSON (but not necessarily JSON Lines) file where this value may be spread over multiple lines.


















