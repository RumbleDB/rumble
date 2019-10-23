(:JIQS: ShouldRun; Output="(1, 2.14, 1, aa, 1, { "a" : "b" }, 1, 2, 3, false, null, [ 1, 2, 3 ], { "aa" : "bb" }, { "cc" : "dd" }, P3Y5M)" :)
1 treat as integer,
2.14 treat as decimal,
1 treat as decimal,
"aa" treat as string+,
1 treat as item,
{"a": "b"} treat as object,
(1,2,3) treat as integer*,
false treat as boolean,
null treat as null,
[1,2,3] treat as array,
({"aa": "bb"}, {"cc": "dd"}) treat as json-item+,
() treat as string?,
() treat as string*,
() treat as (),
duration("P3Y5M") treat as duration

(: general tests :)

