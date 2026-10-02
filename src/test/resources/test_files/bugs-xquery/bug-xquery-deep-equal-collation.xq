(:JIQS: ShouldRun; Output="(true, false, true, true, false, false, false, true, true)" :)
deep-equal(("a", "A"), ("A", "a"), "http://www.w3.org/2005/xpath-functions/collation/html-ascii-case-insensitive"),
deep-equal(("a", "A"), ("A", "b"), "http://www.w3.org/2005/xpath-functions/collation/html-ascii-case-insensitive"),
deep-equal(<a><b x="abc"/></a>, <a><b x="ABC"/></a>, "http://www.w3.org/2005/xpath-functions/collation/html-ascii-case-insensitive"),
deep-equal(<a><b>abc</b></a>, <a><b>ABC</b></a>, "http://www.w3.org/2005/xpath-functions/collation/html-ascii-case-insensitive"),
deep-equal(<a><b>abc</b></a>, <a><B>abc</B></a>, "http://www.w3.org/2005/xpath-functions/collation/html-ascii-case-insensitive"),
deep-equal(<a><b x="abc"/></a>, <a><b X="abc"/></a>, "http://www.w3.org/2005/xpath-functions/collation/html-ascii-case-insensitive"),
fn:deep-equal(map{"a":1}, map{"A":1}, 'http://www.w3.org/2013/collation/UCA?strength=secondary'),
fn:deep-equal(map{1:"a"}, map{1:"A"}, 'http://www.w3.org/2013/collation/UCA?strength=secondary'),
let $deep := deep-equal#3 return $deep(<a><b>abc</b></a>, <a><b>ABC</b></a>, "http://www.w3.org/2005/xpath-functions/collation/html-ascii-case-insensitive")
