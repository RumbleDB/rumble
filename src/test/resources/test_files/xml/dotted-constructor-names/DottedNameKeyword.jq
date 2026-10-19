(:JIQS: ShouldRun; Output="true" :)
let $empty := <element.name element.name="value"/>
let $paired := <element.name element.name="value"></element.name>
return every $element in ($empty, $paired) satisfies (
    local-name($element) eq "element.name"
    and local-name($element/@*) eq "element.name"
    and string($element/@*) eq "value"
)
