(:JIQS: ShouldRun; Output="Success" :)

declare type local:myPosInt as jsound verbose {
    "name": "local:myPosInt",
    "kind": "atomic",
    "baseType": "xs:positiveInteger",
    "minInclusive": 10
};
try {
  validate type local:myPosInt {
      5
  }
} catch XQDY0027 {
  "Success"
}
