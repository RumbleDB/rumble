(:JIQS: ShouldRun; Output="true" :)
string(<e>"'</e>) eq "&quot;&apos;"
and string(<e>He said "it's fine".</e>) eq "He said &quot;it's fine&quot;."
and string(<e>""''"'</e>) eq "&quot;&quot;&apos;&apos;&quot;&apos;"
and string(<outer>before "<inner>it's</inner>" after</outer>) eq "before &quot;it's&quot; after"
and string(<e>{"}", "it's"}</e>) eq "} it's"
