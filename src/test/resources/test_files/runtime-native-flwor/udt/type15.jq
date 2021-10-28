(:JIQS: ShouldRun; Output="([ 1, 2 ], [ ], [ 1 ], [ 1, 2 ], [ 1.2, 2 ], [ 1.2, 2 ], Success, Success)" :)
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
},
validate type local:x {
  [ 1.2, 2 ]
},
validate type local:x* {
  [ 1.2, 2 ]
},
try {
  validate type local:y {
    [ 1.2, 2 ]
  }
} catch XQDY0027 {
  "Success"
},
try {
  validate type local:y* {
    [ 1.2, 2 ]
  }
} catch XQDY0027 {
  "Success"
}