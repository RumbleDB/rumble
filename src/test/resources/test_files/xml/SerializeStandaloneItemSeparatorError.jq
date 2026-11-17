(:JIQS: ShouldCrash; ErrorCode="SEPM0004" :)
(: The separator creates top-level text, which is incompatible with standalone=yes. :)
serialize(
    parse-xml("<!--before--><a/>")/node(),
    {
        "method": "xml",
        "standalone": true,
        "omit-xml-declaration": false,
        "item-separator": "X"
    }
)
