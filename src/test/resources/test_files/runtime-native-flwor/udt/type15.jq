(:JIQS: ShouldRun; Output="({ }, { "foo" : { } }, { "bar" : { } }, { "foo" : { }, "bar" : { } }, { "foo" : { } }, { "bar" : { } }, { "foo" : { }, "bar" : { } }, { }, { }, Success, Success, Success, Success, Success, Success)" :)
declare type local:x as jsound verbose {
  "kind" : "array",
  "content" : "decimal"
};

declare type local:y as jsound verbose {
  "kind" : "array",
  "baseType" : "local:x",
  "content" : "integer"
};

validate type local:y* {
  [ 1, 2 ],
  [ ],
  [ 1 ]
},
validate type local:y {
  [ 1, 2 ]
}