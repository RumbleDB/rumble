(:JIQS: ShouldRun; Output="true" :)
(: Catching the error requires successful compilation and a failed runtime conversion. :)
try {
    text {"abc"} cast as xs:integer
} catch err:FORG0001 {
    true()
}
