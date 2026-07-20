(:JIQS: ShouldRun; Output="one,two" :)
xquery version "3.1";
declare function local:tokens($value as xs:string) as xs:NMTOKEN* {
  xs:NMTOKENS($value)
};
string-join(local:tokens("one two"), ",")
