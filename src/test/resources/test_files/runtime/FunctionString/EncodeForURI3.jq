(:JIQS: ShouldRun; Output="http://www.example.com/~b%C3%A9b%C3%A9" :)
concat("http://www.example.com/", encode-for-uri("~bébé"))

(: XQuery spec test :)
