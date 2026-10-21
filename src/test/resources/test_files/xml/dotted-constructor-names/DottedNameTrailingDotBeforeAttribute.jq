(:JIQS: ShouldRun; Output="true" :)
let $element := <a. b.c="v"/>
return local-name($element) eq "a." and local-name($element/@*) eq "b.c"
