(:JIQS: ShouldParse :)
let $x := 2
return <e
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
