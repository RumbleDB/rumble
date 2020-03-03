# Function library

We list here the functions supported by Rumble, and introduce them by means of examples. Highly detailed specifications can be found in the [underlying W3C standard](https://www.w3.org/TR/xpath-functions-30/#func-floor), unless the function is marked as specific to JSON or Rumble, in which case it can be found [here](http://www.jsoniq.org/docs/JSONiq/html-single/index.html#idm34604304).

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

returns 10

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

returns 1

### max

```
let $x := (1, 2, 3, 4)
return max($x)
```

returns 4

### avg

```
let $x := (1, 2, 3, 4)
return avg($x)
```

returns 2.5

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

Returns (1, 2, 3, 4, 5, 6, 7, 8, 9, 10).

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


## I/O functions

### json-doc

```
json-doc("file.json")
```

returns the (single) JSON value read from the supplied JSON file. This will also work for structures spread over multiple lines, as the read is local and not sharded.

## Integration with HDFS and Spark

We support two more functions to read a JSON file from HDFS or send a large sequence to the cluster:

### json-file (Rumble specific)

Exists in unary and binary. The first parameter specifies the JSON file (or set of JSON files) to read.
The second, optional parameter specifies the minimum number of partitions. It is recommended to use it in a local setup, as the default is only one partition, which does not fully use the parallelism. If the input is on HDFS, then blocks are taken as splits by default. This is also similar to Spark's textFile().

Example of usage:
```
for $my-json in json-file("hdfs://host:port/directory/file.json")
where $my-json.property eq "some value"
return $my-json
```

If a host and port are set:

```
for $my-json in json-file("/absolute/directory/file.json")
where $my-json.property eq "some value"
return $my-json
```

For a set of files:

```
for $my-json in json-file("/absolute/directory/file-*.json")
where $my-json.property eq "some value"
return $my-json
```

If a working directory is set:

```
for $my-json in json-file("file.json")
where $my-json.property eq "some value"
return $my-json
```

Several files or whole directories can be read with the same pattern syntax as in Spark.


```
for $my-json in json-file("*.json")
where $my-json.property eq "some value"
return $my-json
```

### structured-json-file (Rumble specific)

Parses one or more json files that follow [JSON-lines](http://jsonlines.org/) format and returns a sequence of objects. This enables better performance with fully structured data and is recommended to use only when such data is available. When data has multiple types for the same field, this field and contained values will be treated as strings. This is also similar to Spark's spark.read.json().

Example of usage:
```
for $my-structured-json in structured-json-file("hdfs://host:port/directory/structured-file.json")
where $my-structured-json.property eq "some value"
return $my-structured-json
```

### text-file (Rumble specific)

Exists in unary and binary. The first parameter specifies the text file (or set of text files) to read and return as a sequence of strings.
The second, optional parameter specifies the minimum number of partitions. It is recommended to use it in a local setup, as the default is only one partition, which does not fully use the parallelism. If the input is on HDFS, then blocks are taken as splits by default. This is also similar to Spark's textFile().

Example of usage:
```
count(
  for $my-string in text-file("hdfs://host:port/directory/file.txt")
  for $token in tokenize($my-string, ";")
  where $token eq "some value"
  return $token
)
```

Several files or whole directories can be read with the same pattern syntax as in Spark.

(Also see examples for json-file for host and port, sets of files and working directory).

### parquet-file (Rumble specific)

Parses one or more parquet files and returns a sequence of objects. This is also similar to Spark's spark.read.parquet()

```
for $my-object in parquet-file("file.parquet")
where $my-object.property eq "some value"
return $my-json
```

Several files or whole directories can be read with the same pattern syntax as in Spark.

```
for $my-object in parquet-file("*.parquet")
where $my-object.property eq "some value"
return $my-json
```

### parallelize (Rumble specific)

This function behaves like the Spark parallelize() you are familiar with and sends a large sequence to the cluster.
The rest of the FLWOR expression is then evaluated with Spark transformations on the cluster.

```
for $i in parallelize(1 to 1000000)
where $i mod 1000 eq 0
return $i
```

There is also be a second, optional parameter that specifies the minimum number of partitions.

```
for $i in parallelize(1 to 1000000, 100)
where $i mod 1000 eq 0
return $i
```
