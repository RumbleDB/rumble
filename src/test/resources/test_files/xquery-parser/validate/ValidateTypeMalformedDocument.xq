(:JIQS: ShouldCrash; ErrorCode="XQDY0061" :)
(: A document validated by type must contain exactly one element child. :)
validate type xs:string { document { <a/>, <b/> } }
