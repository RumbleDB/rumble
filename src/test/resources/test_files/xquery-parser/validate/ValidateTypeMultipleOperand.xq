(:JIQS: ShouldCrash; ErrorCode="XQTY0030" :)
(: validate type accepts one node, not a sequence of nodes. :)
validate type xs:string { (<a/>, <b/>) }
