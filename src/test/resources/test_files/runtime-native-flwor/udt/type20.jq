(:JIQS: ShouldRun; Output="([ 1, 2, 3, 4, 5, 6, 7 ], [ 1 ], [ 1, 2, 3 ], Success, Success, Success)" :)
declare type local:a as jsound verbose {
  "kind" : "array",
  "baseType" : "array",
  "content" : "integer"
};

declare type local:b as jsound verbose {
  "kind" : "array",
  "baseType" : "local:a",
  "content" : "int",
  "minLength" : 1
};

declare type local:c as jsound verbose {
  "kind" : "array",
  "baseType" : "local:b",
  "content" : "short",
  "maxLength" : 3
};

validate type local:a* {
  [ 1, 2, 3, 4, 5, 6, 7 ]
},
validate type local:b* {
  [ 1 ]
},
validate type local:c* {
  [ 1, 2, 3 ]
},
try {
  validate type local:b* {
    [ ]
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:c* {
    [ ]
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:c* {
    [ 1, 2, 3, 4 ]
  }
} catch XQDY0027 {
  "Success"
}