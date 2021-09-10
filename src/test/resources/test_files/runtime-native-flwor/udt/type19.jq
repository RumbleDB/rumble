(:JIQS: ShouldRun; Output="({ "foo" : [ 2, 3 ] }, Success)" :)
declare type local:x as jsound verbose {
  "kind" : "object",
  "baseType" : "object",
  "content" : [
    { "name" : "foo", "type" : "local:a" }
  ],
  "closed" : true
};

declare type local:a as jsound verbose {
  "kind" : "array",
  "baseType" : "array",
  "content" : "integer"
};

declare type local:b as jsound verbose {
  "kind" : "array",
  "baseType" : "local:a",
  "content" : "int"
};

declare type local:y as jsound verbose {
  "kind" : "object",
  "baseType" : "local:x",
  "content" : [
    { "name" : "foo", "type" : "local:b" }
  ]
};

validate type local:y* {
  { "foo" : [ 2, 3 ] }
},
try {
  validate type local:y* {
    { "foo" : [ 2103294587134059823, 3 ] }
  }
} catch XQDY0027 {
  "Success"
}