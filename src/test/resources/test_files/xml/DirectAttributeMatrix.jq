(:JIQS: ShouldRun; Output="(true, true, true, true, true, true, true, true, true, true, true)" :)
let $x := 2
let $e := <e
    empty=""
    single='single'
    whitespace=" a b "
    entities="&lt;&gt;&amp;&quot;&apos;"
    decimal="&#65;"
    hexadecimal='&#x42;'
    quote=""""
    apostrophe=''''
    mixed="before {$x} between {$x + 1} after"
    braces="{{left}}-{{right}}"
    nested="{data(<n value="inside"/>/@value)}"
/>
return (
    data($e/@empty) eq "",
    data($e/@single) eq "single",
    data($e/@whitespace) eq " a b ",
    data($e/@entities) eq "<>&\"'",
    data($e/@decimal) eq "A",
    data($e/@hexadecimal) eq "B",
    data($e/@quote) eq '"',
    data($e/@apostrophe) eq "'",
    data($e/@mixed) eq "before 2 between 3 after",
    data($e/@braces) eq "{left}-{right}",
    data($e/@nested) eq "inside"
)
