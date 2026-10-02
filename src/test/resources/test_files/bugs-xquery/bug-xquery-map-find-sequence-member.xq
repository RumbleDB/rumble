(:JIQS: ShouldRun; Output="true" :)
deep-equal(
  map:find(
    [map {
      7 : ("Saturday", "Sat"),
      "fr" : [map { 7 : ("Samedi", "Sa") }, 78]
    }, 82],
    7
  ),
  [("Saturday", "Sat"), ("Samedi", "Sa")]
)
