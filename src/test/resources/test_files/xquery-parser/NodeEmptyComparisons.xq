(:JIQS: ShouldRun; Output="" :)
(: With strict static typing disabled, an empty operand on either side makes the comparison empty. :)
(
    (() is <a/>) is statically empty-sequence(),
    (<a/> is ()) is statically empty-sequence()
)
