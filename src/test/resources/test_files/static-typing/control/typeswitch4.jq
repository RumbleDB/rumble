(:JIQS: ShouldRun :)
(typeswitch((1,2,3))
case integer* return 1
default return 0) is statically integer