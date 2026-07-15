(:JIQS: ShouldRun; Output="(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true)" :)
let $json := {
    "entity": "&amp;",
    "raw-ampersand": "a & b",
    "braces": "{json}",
    "escaped-newline": "\n"
}
let $xml := <e decoded="&amp;" static-backslash="\n"/>
return (
    $json.entity eq "&amp;",
    $json."raw-ampersand" eq "a & b",
    $json.braces eq "{json}",
    string-length($json."escaped-newline") eq 1,
    data($xml/@decoded) eq "&",
    data($xml/@static-backslash) eq "\\n",

    (: JSONiq strings using either delimiter inside XML attributes. :)
    data(<e x="{"&amp;"}"/>/@x) eq "&amp;",
    data(<e x="{"a & b"}"/>/@x) eq "a & b",
    data(<e x='{"&amp;"}'/>/@x) eq "&amp;",
    data(<e x="{concat('{', '&amp;', '}')}"/>/@x) eq "{&amp;}",
    data(<e x='{concat("{", "&amp;", "}")}'/>/@x) eq "{&amp;}",

    (: XML references are decoded only in static XML attribute content. :)
    data(<e x="&lt;{"&amp;"}&gt;"/>/@x) eq "<&amp;>",

    (: JSON objects and JSON escapes inside enclosed expressions. :)
    data(<e x="{ {"value": "{&amp;}"}.value }"/>/@x) eq "{&amp;}",
    data(<e x="\n|{"\n"}"/>/@x) eq "\\n|\n",

    (: Values flowing from XML to JSONiq and through a nested constructor. :)
    { "value": data($xml/@decoded) }.value eq "&",
    data(<outer x="{data(<inner y="&amp;"/>/@y)}"/>/@x) eq "&"
)
