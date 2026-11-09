(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
(: A node alternative must not hide a known incompatible atomic alternative. :)
for $x in (xs:date('2000-01-01'), <a>4</a>)
return $x cast as xs:integer
