(:JIQS: ShouldRun; Output="" :)
(: With strict static typing disabled, every empty operand combination remains empty. :)
(
    (() is <a/>) is statically empty-sequence(),
    (<a/> is ()) is statically empty-sequence(),
    (() is ()) is statically empty-sequence(),
    (() << <a/>) is statically empty-sequence(),
    (<a/> << ()) is statically empty-sequence(),
    (() << ()) is statically empty-sequence(),
    (() >> <a/>) is statically empty-sequence(),
    (<a/> >> ()) is statically empty-sequence(),
    (() >> ()) is statically empty-sequence()
)
