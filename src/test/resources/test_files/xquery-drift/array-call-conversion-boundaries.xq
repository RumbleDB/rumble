(:JIQS: ShouldRun; Output="true":)
deep-equal(
  (
    ["x"](<index>1</index>),
    ["x"](([], [1])),
    try { string(["x"](1.0)) } catch * { local-name-from-QName($err:code) },
    try { string(["x"](xs:untypedAtomic("bad"))) } catch * { local-name-from-QName($err:code) },
    try { string(["x"](())) } catch * { local-name-from-QName($err:code) },
    try { string(["x"]((1, 1))) } catch * { local-name-from-QName($err:code) },
    try { string(["x"](0)) } catch * { local-name-from-QName($err:code) }
  ),
  ("x", "x", "XPTY0004", "FORG0001", "XPTY0004", "XPTY0004", "FOAY0001")
)
