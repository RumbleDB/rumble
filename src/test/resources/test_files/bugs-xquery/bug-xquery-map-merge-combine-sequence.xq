(:JIQS: ShouldRun; Output="true" :)
deep-equal(
  map:get(
    map:merge(
      (map:entry("foo", 3), map:entry("foo", 4)),
      map { "duplicates" : "combine" }
    ),
    "foo"
  ),
  (3, 4)
)
