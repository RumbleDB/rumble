# Function library

We list here the functions supported by RumbleDB, and introduce them by means of examples. Highly detailed specifications can be found in the [underlying W3C standard](https://www.w3.org/TR/xpath-functions-30/#func-floor), unless the function is marked as specific to JSON or RumbleDB, in which case it can be found [here](http://www.jsoniq.org/docs/JSONiq/html-single/index.html#idm34604304).

For the sake of ease of use, all W3C standard builtin functions and JSONiq builtin functions are in the
RumbleDB namespace, which is the default function namespace and does not require any prefix in front of function names.

It is recommended that user-defined functions are put in the local namespace, i.e., their name should have the local: prefix (which is predefined). Otherwise, there is the risk that your code becomes incompatible with subsequent releases if new (unprefixed) builtin functions are introduced.

## Sequence functions

### distinct-values

Eliminates duplicates from a sequence of atomic items.

```
distinct-values((1, 1, 4, 3, 1, 1, "foo", 4, "foo", true, 3, 1, true, 5, 3, 1, 1))
```

returns (1, 4, 3, "foo", true, 5).


This is pushed down to Spark and works on big sequences.

```
distinct-values(json-file("file.json").foo)
```

```
distinct-values(text-file("file.txt"))
```

### empty

Returns a boolean whether the input sequence is empty or not.

```
empty(1 to 10)
```

returns false.


```
empty(())
```

returns true.

This is pushed down to Spark and works on big sequences.


```
empty(json-file("file.json"))
```


### exists

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
exists(json-file("file.json"))
```


### head

Returns the first item of a sequence, or the empty sequence if it is empty.

```
head(1 to 10)
```

returns true.


```
head(())
```

returns ().

This is pushed down to Spark and works on big sequences.

```
head(json-file("file.json"))
```

## Aggregation functions

### sum

```
let $x := (1, 2, 3, 4)
return sum($x)
```

returns 10.

Sum calls are pushed down to Spark, so this works on billions of items as well:

```
sum(json-file("file.json").foo)
```

### count

```
let $x := (1, 2, 3, 4)
return count($x)
```

returns 4.

Count calls are pushed down to Spark, so this works on billions of items as well:

```
count(json-file("file.json"))
```

```
count(
  for $i in json-file("file.json")
  where $i.foo eq "bar"
  return $i
)
```


### min

```
let $x := (1, 2, 3, 4)
return min($x)
```

returns 1.

Min calls are pushed down to Spark, so this works on billions of items as well:

```
min(json-file("file.json").foo)
```

### max

```
let $x := (1, 2, 3, 4)
return max($x)
```

returns 4.

Max calls are pushed down to Spark, so this works on billions of items as well:

```
max(json-file("file.json").foo)
```

### avg

```
let $x := (1, 2, 3, 4)
return avg($x)
```

returns 2.5.

Avg calls are pushed down to Spark, so this works on billions of items as well:

```
avg(json-file("file.json").foo)
```

## Object functions

### keys

```
keys({"foo" : "bar", "bar" : "foobar"})
```

returns ("foo", "bar").  Also works on an input sequence, eliminating duplicates

```
keys(({"foo" : "bar", "bar" : "foobar"}, {"foo": "bar2"}))
```

Keys calls are pushed down to Spark, so this works on billions of items as well:

```
keys(json-file("file.json"))
```

### json-doc

```
json-doc("/Users/sheldon/object.json")
```

returns the (unique) JSON value parsed from a local JSON (but not necessarily JSON Lines) file where this value may be spread over multiple lines.


### project

```
project({"foo" : "bar", "bar" : "foobar", "foobar" : "foo" }, ("foo", "bar"))
```

returns the object {"foo" : "bar", "bar" : "foobar"}. Also works on an input sequence, in a distributive way.

```
project(({"foo" : "bar", "bar" : "foobar", "foobar" : "foo" }, {"foo": "bar2"}), ("foo", "bar"))
```


### remove-keys

```
remove-keys({"foo" : "bar", "bar" : "foobar", "foobar" : "foo" }, ("foo", "bar"))
```

returns the object {"foobar" : "foo"}. Also works on an input sequence, in a distributive way.

```
remove-keys(({"foo" : "bar", "bar" : "foobar", "foobar" : "foo" }, {"foo": "bar2"}), ("foo", "bar"))
```

### values

```
values({"foo" : "bar", "bar" : "foobar"})
```

returns ("bar", "foobar").  Also works on an input sequence, in a distributive way.

```
values(({"foo" : "bar", "bar" : "foobar"}, {"foo" : "bar2"}))
```

Values calls are pushed down to Spark, so this works on billions of items as well:

```
values(json-file("file.json"))
```

## Array functions

### size

```
size([1 to 100])
```

returns 100. Also works if the empty sequence is supplied, in which case it returns the empty sequence.

```
size(())
```

### members

```
members([1 to 100])
```

This function returns the members as an array, but not recursively, i.e., nested arrays are not unboxed.

Returns the first 100 integers as a sequence.  Also works on an input sequence, in a distributive way.

```
members(([1 to 100], [ 300 to 1000 ]))
```


### flatten

```
flatten(([1, 2], [[3, 4], [5, 6]], [7, [8, 9]]))
```

Unboxes arrays recursively, stopping the recursion when any other item is reached (object or atomic). Also works on an input sequence, in a distributive way.

Returns (1, 2, 3, 4, 5, 6, 7, 8, 9).

## Atomic functions

```
null()
```

Returns a JSON null (also available as the literal null).

## Mathematic functions

### abs

```
abs(-2)
```

returns 2.0

### acos

```
acos(1)
```

### asin

```
asin(1)
```

### atan

```
atan(1)
```

### atan2

```
atan2(1)
```


### ceiling

```
ceiling(2.3)
```

returns 3.0

### cos

```
cos(pi())
```

### exp

```
exp(10)
```

### exp10

```
exp10(10)
```



### floor

```
floor(2.3)
```

returns 2.0

### log

```
log(100)
```


### log10

```
log10(100)
```

### number

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

### pow

```
pow(10, 2)
```

returns 100.0

### round

```
round(2.3)
```

returns 2.0

```
round(2.2345, 2)
```

returns 2.23

### round-half-to-even


```
round-half-to-even(2.2345, 2), round-half-to-even(2.2345)
```

### sqrt

```
sqrt(4)
```

returns 100.0



### sin


```
sin(pi())
```

### tan


```
tan(pi())
```

## String functions

### concat

```
concat("foo", "bar", "foobar")
```

returns "foobarfoobar"

### contains

```
contains("foobar", "ob")
```

returns true.

### ends-with

```
ends-with("foobar", "bar")
```

returns true.

### matches

Regular expression matching. The semantics of regular expressions are those of Java's Pattern class.

```
matches("foobar", "o+")
```

returns true.

```
matches("foobar", "^fo+.*")
```

returns true.

###normalize-spaces

Normalization of spaces in a string.

```
normalize-space(" The    wealthy curled darlings                                         of    our    nation. "),
```

returns "The wealthy curled darlings of our nation."

### starts-with

```
starts-with("foobar", "foo")
```

returns true

### string-join

```
string-join(("foo", "bar", "foobar"))
```

returns "foobarfoobar"

```
string-join(("foo", "bar", "foobar"), "-")
```

returns "foo-bar-foobar"

### string-length

Returns the length of the supplied string, or 0 if the empty sequence is supplied.

```
string-length("foo")
```

returns 3.

```
string-length(())
```

returns 0.

### substring

```
substring("foobar", 4)
```

returns "bar"

```
substring("foobar", 4, 2)
```

returns "ba"

### tokenize

```
tokenize("aa bb cc dd")
```

returns ("aa", "bb", "cc", "dd")

```
tokenize("aa;bb;cc;dd", ";")
```

returns ("aa", "bb", "cc", "dd")

### replace

Regular expression matching and replacing. The semantics of regular expressions are those of Java's Pattern class.

```
replace("abracadabra", "bra", "*")
```

returns "a\*cada\*"

```
replace("abracadabra", "a(.)", "a$1$1")
```

returns "abbraccaddabbra"

### translate

```
translate("bar","abc","ABC")
```
returns "BAr"

```
translate("--aaa--","abc-","ABC")
```
returns "AAA"

### codepoint-equal

```
codepoint-equal("abcd", "abcd")
```
returns true

```
codepoint-equal("", ())
```
returns ()

### string-to-codepoint

```
string-to-codepoints("Thérèse")
```
returns (84, 104, 233, 114, 232, 115, 101)

```
string-to-codepoints("")
```
returns ()

### codepoints-to-string

```
codepoints-to-string((2309, 2358, 2378, 2325))
```
returns "अशॊक"

```
codepoints-to-string(())
```
returns ""

### upper-case

```
upper-case("abCd0")
```
returns "ABCD0"

### lower-case

```
lower-case("ABc!D")
```
returns "abc!d"

### serialize

Serializes the supplied input sequence, returning the serialized representation of the sequence as a string

```
serialize({hello: "world"})
```
returns { "hello" : "world" }

### normalize-unicode

Returns the value of the input after applying Unicode normalization.

```
normalize-unicode("hello world", "NFC")
```

returns the unicode-normalized version of the input string. Normalization forms NFC, NFD, NFKC, and NFKD are supported. "FULLY-NORMALIZED" though supported, should be used with caution as only the composition exclusion characters supported FULLY-NORMALIZED are which are uncommented in the [following file](https://www.unicode.org/Public/UCD/latest/ucd/CompositionExclusions.txt).

## Date and time functions

### dateTime

```
dateTime("2004-04-12T13:20:00+14:00")
```

returns 2004-04-12T13:20:00+14:00

### date

```
date("2004-04-12+14:00")
```

returns 2004-04-12+14:00

### time

```
time("13:20:00-05:00")
```

returns 13:20:00-05:00

## Formatting dates and times functions

The functions in this section accept a simplified version of the picture string, in which a variable marker accepts only:

* One of the following component specifiers: Y, M, d, D, F, H, m, s, P
* A first presentation modifier, for which the value can be:
	* Nn, for all supported component specifiers, besides P
	* N, if the component specifier is P
	* a format token that indicates a numbering sequence of the the following form: '0001'
* A second presentation modifier, for which the value can be t or c, which are also the default values
* A width modifier, both minimum and maximum values

### format-dateTime

```
format-dateTime(dateTime("2004-04-12T13:20:00"), "[m]-[H]-[D]-[M]-[Y]")
```

returns 20-13-12-4-2004

### format-date

```
format-date(date("2004-04-12"), "[D]-[M]-[Y]")
```

returns 12-4-2004

### format-time

```
format-time(time("13:20:00"), "[H]-[m]-[s]")
```

returns 13-20-0

## Context functions

### position

```
(1 to 10)[position() eq 5]
```

returns 5

### last

```
(1 to 10)[position() eq last()]
```
returns 10


```
(1 to 10)[last()]
```
returns 10

### current-dateTime

```
current-dateTime()
```

returns 2020-02-26T11:22:48.423+01:00

### current-date

```
current-date()
```

returns 2020-02-26Europe/Zurich

### current-time

```
current-time()
```

returns 11:24:10.064+01:00
