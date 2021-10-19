(:JIQS: ShouldRun; Output="mylongstring" :)
declare type local:mystring as jsound verbose {
    "name": "local:mystring",
    "kind": "atomic",
    "baseType": "xs:string",
    "maxLength": 5
};
validate type local:mystring {
    "mylongstring"
}


