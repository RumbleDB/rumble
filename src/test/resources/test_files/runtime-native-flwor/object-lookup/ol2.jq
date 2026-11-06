(:JIQS: ShouldRun; Output="([ "Lao", "Latvian", "Russian", "Swahili" ], [ "Croatian", "Nepali", "Russian", "Slovenian" ], [ "Maori", "Czech", "Korean", "Turkish" ], [ "German", "Greek", "Kannada", "Serbian" ], [ "Dari", "Serbian", "Sinhalese", "Vietnamese" ])" :)
for $i in structured-json-lines("../../../queries/conf-ex.json")
let $c := $i.choices
return $c