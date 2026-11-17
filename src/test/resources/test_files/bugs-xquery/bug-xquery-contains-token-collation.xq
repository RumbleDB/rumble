(:JIQS: ShouldRun; Output="true" :)
declare default collation "http://www.w3.org/2005/xpath-functions/collation/html-ascii-case-insensitive";
contains-token("red GREEN blue", "green")
and not(contains-token("red green", "GREEN", "http://www.w3.org/2005/xpath-functions/collation/codepoint"))
and collation-key("ABC") eq collation-key("abc")
and collation-key("ABC", "http://www.w3.org/2005/xpath-functions/collation/codepoint")
    ne collation-key("abc", "http://www.w3.org/2005/xpath-functions/collation/codepoint")
