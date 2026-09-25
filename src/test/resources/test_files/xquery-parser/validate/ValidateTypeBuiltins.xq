(:JIQS: ShouldRun; Output="(true, true, true, true, true, true)" :)
(
    data(validate type xs:string { <value>hello</value> }) instance of xs:string,
    data(validate type xs:integer { <value>42</value> }) instance of xs:integer,
    data(validate type xs:integer { <value> 42 </value> }) eq 42,
    namespace-uri-from-QName(
        data(validate type xs:QName { <value xmlns:p="urn:test">p:name</value> })
    ) eq "urn:test",
    let $input := <value>42</value>
    let $validated := validate type xs:integer { $input }
    return $validated instance of element ( ) and not($input is $validated),
    data(validate type xs:integer { document { <value>42</value> } }) instance of xs:untypedAtomic
)
