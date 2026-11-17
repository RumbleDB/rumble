(:JIQS: ShouldRun :)
(: These functions return items of their first argument, so they keep its item type. :)
exactly-one((1, 2)[$$ gt 1]) is statically integer,
one-or-more((1, 2)[$$ gt 1]) is statically integer+,
zero-or-one((1, 2)[$$ gt 1]) is statically integer?,
head((1, 2)) is statically integer,
tail((1, 2)) is statically integer+,
reverse((1, 2)) is statically integer+,
sort((2, 1)) is statically integer+,
subsequence((1, 2, 3), 2) is statically integer*,
insert-before((1, 2), 1, 3) is statically integer+,
distinct-values((1, 1)) is statically integer+
