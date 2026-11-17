(:JIQS: ShouldRun; Output="(true, true, true, true, true, true, true)" :)
let $values := map {
    "two  spaces": "two  spaces",
    "comment-like": "before (: not a comment :) after",
    "entity": "&amp;",
    "quote": """quoted""",
    "apostrophe": '''quoted'''
}
return (
    $values("two  spaces") eq "two  spaces",
    $values("comment-like") eq "before (: not a comment :) after",
    $values("entity") eq "&amp;",
    $values("quote") eq '"quoted"',
    $values("apostrophe") eq "'quoted'",
    data(<e x='{"before (: not a comment :) after"}'/>/@x) eq "before (: not a comment :) after",
    data(<e x="{'before (: not a comment :) after'}"/>/@x) eq "before (: not a comment :) after"
)
