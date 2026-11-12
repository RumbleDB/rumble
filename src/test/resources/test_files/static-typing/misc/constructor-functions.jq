(:JIQS: ShouldRun :)
(: A constructor function returns one value for an argument that atomizes to one value, and at most one otherwise. :)
integer("1") is statically integer,
integer(zero-or-one(("1", "2")[$$ eq "1"])) is statically integer?
