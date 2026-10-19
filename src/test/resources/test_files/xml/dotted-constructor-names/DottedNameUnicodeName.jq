(:JIQS: ShouldRun; Output="true" :)
let $empty := <é.名 é.名="value"/>
let $paired := <é.名 é.名="value"></é.名>
return every $element in ($empty, $paired) satisfies (
    local-name($element) eq "é.名"
    and local-name($element/@*) eq "é.名"
    and string($element/@*) eq "value"
)
