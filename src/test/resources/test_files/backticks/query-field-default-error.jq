(:JIQS: ShouldCrash; ErrorCode="PARSE_SYNTAX_ERROR" :)
(: A key constructed in the query has the same issue after conversion to a DataFrame. :)
for $row in annotate({ "backtick`" : 7 }, { "backtick`" : "integer" })
return $row."backtick`"
