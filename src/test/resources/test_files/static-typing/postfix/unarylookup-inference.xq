(:JIQS: ShouldRun; Output="(1, 1, 1, 1, 2, 1, 2)" :)
map { "a": 1 } ! (?a is statically xs:integer?),
array { 1, 2 } ! (?1 is statically xs:integer),
map { "a": 1 } ! (?* is statically xs:integer*),
array { 1, 2 } ! (?* is statically xs:integer*),
array { 1, 2 } ! (?(1 to 2) is statically xs:integer*)
