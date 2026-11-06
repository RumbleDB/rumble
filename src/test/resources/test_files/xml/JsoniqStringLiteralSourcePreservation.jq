(:JIQS: ShouldRun; Output="(true, true, true, true, true, true, true, true, true)" :)
let $strings := {
    "two  spaces": "two  spaces",
    "comment-like": "before (: not a comment :) after",
    "unicode": "\u0041",
    "escaped-quote": "quote: \"",
    "escaped-apostrophe": 'apostrophe: \'',
    "escaped-slash": "slash: \/"
}
return (
    $strings."two  spaces" eq "two  spaces",
    $strings."comment-like" eq "before (: not a comment :) after",
    $strings.unicode eq "A",
    $strings."escaped-quote" eq 'quote: "',
    $strings."escaped-apostrophe" eq "apostrophe: '",
    $strings."escaped-slash" eq "slash: /",

    (: Whitespace in keys and lookup literals must survive hidden-channel tokenization. :)
    { "key  with  spaces": 1 }."key  with  spaces" eq 1,

    (: Comment-like text inside a string remains literal within an enclosed attribute expression. :)
    data(<e x='{"before (: not a comment :) after"}'/>/@x) eq "before (: not a comment :) after",
    data(<e x="{'before (: not a comment :) after'}"/>/@x) eq "before (: not a comment :) after"
)
