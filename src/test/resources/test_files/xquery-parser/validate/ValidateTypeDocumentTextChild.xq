(:JIQS: ShouldCrash; ErrorCode="XQDY0061" :)
(: A validated document cannot have text-node siblings of its document element. :)
validate type xs:string { document { text { "text" }, <a/> } }
