(:JIQS: ShouldRun; Output="Success" :)
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

