# Function library

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

returns 4

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

## Mathematic functions

### abs

```
abs(-2)
```

returns 2.0

### acos
### asin
### atan
### atan2

### ceiling

```
ceiling(2)
```

returns 3.0

### cos

### exp

### exp10


### floor

```
floor(2)
```

returns 2.0

### log

### log10

### pow

```
round(10, 2)
```

returns 100.0

### round

```
round(2)
```

returns 2.0

```
round(2.2345, 2)
```

returns 2.23

### round-half-to-even

### sin

### tan

## String functions

### concat

```
concat("foo", "bar", "foobar")
```

returns "foobarfoobar"

### string-join

```
string-join(("foo", "bar", "foobar"))
```

returns "foobarfoobar"

```
string-join(("foo", "bar", "foobar"), "-")
```

returns "foo-bar-foobar"

### substring

```
substring("foobar", 4)
```

returns "bar"

```
substring("foobar", 4, 2)
```

returns "ba"

## Integration with HDFS and Spark

We support two more functions to read a JSON file from HDFS or send a large sequence to the cluster:

### json-file

Exists in unary and binary. The first parameter specifies the JSON file (or set of JSON files) to read.
The second, optional parameter specifies the number of partitions. This is also similar to Sparks textFile().

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

### parallelize

This function behaves like the Spark parallelize() you are familiar with and sends a large sequence to the cluster.
The rest of the FLWOR expression is then evaluated with Spark transformations on the cluster.

```
for $i in parallelize(1 to 1000000)
where $i mod 1000 eq 0
return $i
```

In the future, it will also have a second, optional parameter that specifies the number of partitions.

```
for $i in parallelize(1 to 1000000, 100)
where $i mod 1000 eq 0
return $i
```
