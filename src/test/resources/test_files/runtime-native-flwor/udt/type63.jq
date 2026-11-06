(:JIQS: ShouldRun; Output="(5, 6)" :)
declare type local:p as jsound verbose { "kind": "atomic", "baseType": "xs:byte", "maxInclusive": 120 };
declare type local:q as jsound verbose { "kind": "atomic", "baseType": "local:p", "maxInclusive": 100 };
declare type local:x2 as jsound verbose { "kind": "atomic", "baseType": "xs:byte", "maxInclusive": 120 };
declare type local:x1 as jsound verbose { "kind": "atomic", "baseType": "local:x2", "maxInclusive": 100 };
validate type local:q { 5 },
validate type local:x1 { 6 }
