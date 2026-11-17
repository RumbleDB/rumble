(:JIQS: ShouldCrash; ErrorCode="SEPM0004" :)
(: The separator creates top-level text, which is incompatible with doctype-system. :)
serialize(
    parse-xml("<!--before--><a/>")/node(),
    {
        "method": "xml",
        "doctype-system": "example.dtd",
        "item-separator": "X"
    }
)
