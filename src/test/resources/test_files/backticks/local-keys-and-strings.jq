(:JIQS: ShouldRun; Output="(a`b, 7, oh cmon, why?)" :)
(: Backticks are ordinary characters in strings and local object keys. :)
"a`b",
{ "backtick`" : 7 }."backtick`",
json-lines("../../queries/backtick.json")."backtick`"
