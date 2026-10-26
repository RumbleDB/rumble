(:JIQS: ShouldRun; Output="true" :)
(: Empty strings joined with an empty separator produce no top-level text node. :)
serialize(
    ("", ""),
    {
        "method": "xml",
        "standalone": true,
        "omit-xml-declaration": false,
        "item-separator": ""
    }
) eq "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
