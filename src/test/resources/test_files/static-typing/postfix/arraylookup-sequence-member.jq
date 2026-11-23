(:JIQS: ShouldRun; Output="(1, 2, 1, 2)" :)
(: A single array can contain a member with multiple items. :)
array:append([], (1, 2))[[1]] is statically item*,
one-or-more(array:append([], (1, 2))[[1]]) is statically item+
