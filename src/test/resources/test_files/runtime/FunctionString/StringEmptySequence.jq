(:JIQS: ShouldRun; Output="(true, true, true, true, true, true, true)" :)
declare function local:string-value($item as item()?) as string {
    string($item)
};
let $empty := ()
return (
    count(string(())) eq 1,
    string(()) eq "",
    string(()) instance of string,
    exists(string($empty)),
    string-length(string($empty)) eq 0,
    count(local:string-value($empty)) eq 1,
    local:string-value($empty) eq ""
)
