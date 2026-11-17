(:JIQS: ShouldRun; Output="({ "table" : "customer", "columns" : "custkey", "data_sample" : [ "custkey", "customer" ] }, { "table" : "lineitem", "columns" : "orderkey", "data_sample" : [ "orderkey", "lineitem" ] })" :)

declare variable $tables := ("customer", "lineitem");
declare variable $table_columns := {
    "customer": "custkey",
    "lineitem": "orderkey"
};

for $table in $tables
let $columns := $table_columns.$table
let $data :=
    for $line in (parallelize(1))
    return ($columns, $table)
return {
    "table": $table,
    "columns": $columns,
    "data_sample": $data
}
