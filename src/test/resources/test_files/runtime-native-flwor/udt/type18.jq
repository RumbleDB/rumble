(:JIQS: ShouldRun; Output="({ "foo" : 2 }, { "foo" : 3 }, { }, true, Success" :)
declare type local:x as jsound verbose {
  "kind" : "object",
  "baseType" : "object",
  "content" : [
    {
      "name" : "foo",
      "type" : "integer",
      "maxExclusive": "15"
    }
  ]
};
try {
  validate type local:x* {
    { "foo" : 2, "bar" : 3 }
  }
} catch XQDY0027 {
  "Success"
},

declare type local:x as jsound verbose {
  "kind" : "array",
  "baseType" : "array",
  "content" : "string",
  "maxLength" : 2
};
validate type local:x* {
  [ ],
  [ "foo" ],
  [ "foo", "bar" ],
  [ "foo", "bar", 1, 2, 3 ]
}

declare type local:mystring as jsound verbose {
    "name": "local:mystring",
    "kind": "atomic",
    "baseType": "xs:string",
    "maxLength": 5
};
try {
  validate type local:mystring {
    "mytoolongstring"
  }
} catch XQDY0027 {
   "Success"
}



declare type local:digits as jsound verbose {
    "name": "digits",
    "kind" : "atomic",
    "baseType" : "integer",
    "minInclusive" : 1,
    "maxExclusive" : 10
};

try {
  validate type local:digits* {
    { "digits": 7}
  }
} catch XQDY0027 {
  "Success"
}
declare type my-type as jsound verbose {
   "name" : "my-type",
   "baseType" : "string",
   "kind" : "atomic"
};
validate type my-type* {
  { "my-type" : "Jam" }
}

declare type local:x as jsound verbose {
  "kind" : "object",
  "baseType" : "object",
  "content" : [
    {
      "name" : "foo",
      "type" : "integer"
    },
    {
      "name" : "nested",
      "type" : {
        "kind" : "object",
        "baseType" : "object",
        "content" : [
          {
            "name" : "bar",
            "type" : "foobar",
            "required" : true
          },
          {
            "name" : "foobar",
            "type" : "hexBinary",
            "required" : false
          }
        ],
        "closed" : false
      },
      "required" : false
    }
  ]
};
validate type local:x* {
  { "foo" : 2, "nested" : { "bar" : "a", "foobar" : "AABBCC" } }
}



declare type local:x as jsound verbose {
  "kind" : "array",
  "content" : "decimal"
};
declare type local:y as jsound verbose {
  "kind" : "atomic",
  "baseType" : "string",
  "maxLength" : 3
};
validate type local:y* {
  "hello"
}


