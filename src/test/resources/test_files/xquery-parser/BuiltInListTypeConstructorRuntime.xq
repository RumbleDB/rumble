(:JIQS: ShouldRun; Output="one,two" :)
xquery version "3.1";
string-join(xs:NMTOKENS(" one   two "), ",")
