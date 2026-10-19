(:JIQS: ShouldRun; Output="true" :)
let $empty := <a. a.="value"/>
let $paired := <a. a.="value"></a.>
return every $element in ($empty, $paired) satisfies (
    local-name($element) eq "a."
    and local-name($element/@*) eq "a."
    and string($element/@*) eq "value"
)
