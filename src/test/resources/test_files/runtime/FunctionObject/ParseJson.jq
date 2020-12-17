(:JIQS: ShouldRun; Output="[1]" :)
parse-json("[1]"),
parse-json("\"foo\""),
parse-json("true"),
parse-json("false"),
parse-json("null"),
parse-json("1.2"),
parse-json(),
parse-json("{\"foo\":\"bar\"}"),
parse-json(unparsed-text("../../../queries/SpreadOverMultipleLinesDocument.json")),
try { parse-json("{\"foo":\"bar\") } catch XPST0003 { () }
