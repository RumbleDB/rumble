(:JIQS: ShouldRun; Output="(true, true, false, true, true)" :)
true and ( true or not true ),
1 + 1 eq 2 or 1 + 1 eq 3,
0 and true, not (not 1e42),
"foo" or false




(: boolean(()), boolean(null), boolean("foo"), boolean("") tests are omitted as boolean is not implemented :)
