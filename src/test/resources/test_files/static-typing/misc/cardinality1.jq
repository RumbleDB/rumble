jsoniq version "3.1";
(:JIQS: ShouldRun :)
declare variable $ints := (1, 2) treat as integer*;
declare variable $one := 1 treat as integer*;
declare variable $obj := {"a" : 1} treat as object*;
declare variable $opt := 1 treat as integer?;
declare variable $some := (1, 2) treat as integer+;
exactly-one($one) is statically integer,
zero-or-one($one) is statically integer?,
one-or-more($ints) is statically integer+,
exactly-one($obj) is statically object,
one-or-more(1) is statically integer,
zero-or-one(1) is statically integer,
one-or-more($opt) is statically integer+,
zero-or-one($some) is statically integer?,
one-or-more($some) is statically integer+,
zero-or-one($opt) is statically integer?
