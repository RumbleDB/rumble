(:JIQS: ShouldRun; Output="(1, 2, 3, 1, s, 12, 1, 1, 2, 1, 1, 2, 3, null, null, 1, 1, 2)" :)
(1,2,3) is statically integer+ , (1) is statically integer, ("s",12) is statically atomic+, () is statically (), ((),()) is statically (), (1, (1,2)) is statically integer+, (1, (( )), ()) is statically integer, ((1,2,3)) is statically integer+, (null, null) is statically null+, (1 treat as integer?) is statically integer?, (1 treat as integer?, 2 treat as integer?) is statically integer*

(: anyAtomicType was atomic in jsoniq1.0 :)