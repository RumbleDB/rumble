(:JIQS: ShouldRun; Output="({ "foo" : 2 }, { "foo" : 3 }, { }, true, Success)" :)
declare type local:x as { "foo" : "short" };
declare type local:y as jsound compact { "foo" : [ "positiveInteger" ] };
declare type local:z as jsound compact { "foo" : [ "positiveInteger" ] };
validate type local:x* {
  { "foo" : 2 },
  { "foo" : 3 },
  { }
},
(validate type local:y+ {
  { "foo" : [ 2 ] },
  { "foo" : [ 3, 4 ] },
  { "foo" : [ 23 ] },
  { }
}).foo[] instance of positiveInteger+,
try {
  validate type local:x {
    { "foo" : [ 2 ] },
      { "foo" : [ -3, 4 ] },
      { "foo" : [ 23 ] },
      { }
  }
} catch XQDY0027 {
  "Success"
}