(:JIQS: ShouldRun; Output="(true, true, true, true, true)" :)
let $mixed := <root>left{{brace}}&amp;{1}right<empty/>tail<child>inside</child>end</root>
return (
    (: Exercise mixed text, references, escaped braces, expressions, and nested elements. :)
    string($mixed) eq "left{brace}&1righttailinsideend",
    count($mixed/empty) eq 1,
    count($mixed/child) eq 1,

    (: Nested attributes can freely switch delimiters at each constructor boundary. :)
    data(<outer x="{data(<inner y='{1 + 1}'/>/@y)}"/>/@x) eq "2",
    data(<outer x='{data(<inner y="{1 + 2}"/>/@y)}'/>/@x) eq "3"
)
