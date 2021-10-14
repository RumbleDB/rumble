(:JIQS: ShouldRun; Output="({ "foo" : 2 }, Success)" :)
declare type local:x as jsound verbose {
  "kind" : "object",
  "baseType" : "object",
  "content" : [
    { "name" : "foo", "type" : "integer", "required" : true }
  ],
  "closed" : true
};

declare type local:y as jsound verbose {
  "kind" : "object",
  "baseType" : "local:x",
  "content" : [
    { "name" : "foo", "type" : "int" }
  ]
};

validate type local:y* {
  { "foo" : 2 }
},
try {
  validate type local:y* {
    { }
  }
} catch XQDY0027 {
  "Success"
}