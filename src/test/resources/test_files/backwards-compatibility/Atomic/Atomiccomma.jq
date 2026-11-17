jsoniq version "1.0";
(:JIQS: ShouldRun; Output="(1, 2, 3, 1, s, 12, 1, 1, 2, 1, 1, 2, 3, null, null, 1, 1, 2)" :)
(1,2,3) is statically integer+,
(1) is statically integer,
(: "atomic" is the JSONiq 1.0 alias for anyAtomicType. Declare it explicitly
   before asserting the exact type of this mixed sequence. :)
(("s",12) treat as atomic+) is statically atomic+,
() is statically (),
((),()) is statically (),
(1, (1,2)) is statically integer+,
(1, (( )), ()) is statically integer,
((1,2,3)) is statically integer+,
(null, null) is statically null+,
(1 treat as integer?) is statically integer?,
(1 treat as integer?, 2 treat as integer?) is statically integer*
