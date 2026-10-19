(:JIQS: ShouldRun; Output="(true, true, true, true, true)" :)
(
    (``[`{map {"x": 42}?x}`]``) eq "42",
    (``[`{ ``[inner `{1 + 1}`]`` }`]``) eq "inner 2",
    (``[`{ (1, 2) ! . }`]``) eq "1 2",
    (``[`{ (:hidden:) 42 }`]``) eq "42",
    string(<e>before }}` after</e>) eq "before }` after"
)
