(:JIQS: ShouldRun; Output="(Success, 15, Success, tree)" :)
declare type local:myPosInt as jsound verbose {
    "name": "local:myPosInt",
    "kind": "anyAtomicType",
    "baseType": "xs:positiveInteger",
    "minInclusive": 10
};
declare type local:shortString as jsound verbose {
    "name": "local:shortString",
    "kind": "anyAtomicType",
    "baseType": "xs:string",
    "maxLength" : 10
};
try {
  validate type local:myPosInt {
      5
  }
} catch XQDY0027 {
  "Success"
},
validate type local:myPosInt {
  15
},
try {
  validate type local:shortString {
    "mytoolongstring"
  }
} catch XQDY0027 {
   "Success"
},
validate type local:shortString {
  "tree"
}