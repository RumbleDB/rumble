(:JIQS: ShouldRun; Output="http://www.example.com/100%25%20organic" :)
concat("http://www.example.com/", encode-for-uri("100% organic"))

(: XQuery spec test :)
