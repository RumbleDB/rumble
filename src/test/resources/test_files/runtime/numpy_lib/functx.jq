(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:1:COLUMN:0:" :)
module namespace functx = "functx.jq";

declare function functx:distinct-deep($items as item*) as item* {
    for $seq in 1 to count($items)
    return $items[$seq][not(functx:is-node-in-sequence-deep-equal($$, $items[position() lt $seq]))]
};

declare function functx:is-node-in-sequence-deep-equal($item as item?, $seq as item*) as xs:boolean {
    some $itemInSeq in $seq satisfies deep-equal($itemInSeq, $item)
};