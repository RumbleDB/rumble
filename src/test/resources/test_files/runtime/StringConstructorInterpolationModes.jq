(:JIQS: ShouldRun; Output="(true, true, true, true, true, true)" :)
(
    (``[a`{ {"x": 42}.x }`b]``) eq "a42b",
    (``[`{if (true) then null else false}`]``) eq "null",
    (``[`{ ``[inner `{1 + 1}`]`` }`]``) eq "inner 2",
    (``[`{ (1, 2) ! $$ }`]``) eq "1 2",
    (``[`{ (:hidden:) 42 }`]``) eq "42",
    string(<e>before }}` after</e>) eq "before }` after"
)
