(:JIQS: ShouldRun; Output="(true, abc###def, true, true, true)" :)
codepoints-to-string(65536) eq "&#65536;",
replace("abc&#x1D157;def", "[^a-f]", "###"),
"&amp;&quot;" eq "&\"",
"\u0026#65;" eq "&#38;#65;",
"&#92;u0041" eq "\\u0041"
