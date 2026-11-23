(:JIQS: ShouldRun; Output="(1, 2, 1, 2, 1, 2, 1, 2, 1, 1)" :)
(: A broader input type or a nonarray alternative must not hide a sequence-valued member. :)
(array:append([], (1, 2)) treat as item)[[1]] is statically item*,
(array:append([], (1, 2)) treat as function(*))[[1]] is statically item*,
(if (true) then array:append([], (1, 2)) else 0)[[1]] is statically item*,
(array:append([], (1, 2)), [3])[[1]][position() le 2] is statically item*,
(: Arrays of single items still contribute at most one item per position. :)
[1, 2][[1]] is statically integer?,
([1, 2] treat as array(integer))[[1]] is statically item?
