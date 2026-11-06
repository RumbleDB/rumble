(:JIQS: ShouldRun; Output="(true, false, true, false, true, false, true, false, true, false, true, false, true, true, true, false, false, false, false, false, false)" :)
"ab" eq "ab",
"ab" eq "de",
"ab" ne "de",
"ab" ne "ab",
"ab" lt "de",
"de" lt "ab",
"de" gt "ab",
"ab" gt "de",
"ab" le "ab",
"ab" le "a",
"ab" ge "ab",
"a" ge "ab",
string("example.com/") eq anyURI("example.com/"),
anyURI("example.com/") eq string("example.com/"),
anyURI("example.com/") eq anyURI("example.com/"),
string("example.com/") ne anyURI("example.com/"),
anyURI("example.com/") ne string("example.com/"),
anyURI("example.com/") ne anyURI("example.com/"),
string("example.com/") lt anyURI("example.com/"),
anyURI("example.com/") lt string("example.com/"),
anyURI("example.com/") lt anyURI("example.com/")


