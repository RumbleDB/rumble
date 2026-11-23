(:JIQS: ShouldCrash; ErrorCode="PARSE_SYNTAX_ERROR" :)
(: Backticks in JSON keys are valid, but need escaping in generated Spark SQL. :)
for $row in structured-json-lines("../../queries/backtick.json")
return $row."backtick`"
