(:JIQS: ShouldRun; Output="(2, 2, 2)" :)
(
    (text {"2"} cast as xs:integer) is statically xs:integer,
    (<a>2</a> cast as xs:integer) is statically xs:integer,
    (attribute a {"2"} cast as xs:integer) is statically xs:integer
)
