(:JIQS: ShouldRun; Output="true" :)
xquery version "3.1";
string(element a { [1, [2, 3]] }) eq "1 2 3"
and string(<a>{[1, [2, 3]]}</a>) eq "1 2 3"
and string(document { [1, [2, 3]] }) eq "1 2 3"
