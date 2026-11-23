(:JIQS: ShouldRun; Output="true":)
(: Successful function conversion cases; invalid arguments have separate error tests. :)
deep-equal(
  (["x"](<index>1</index>), ["x"](([], [1]))),
  ("x", "x")
)
