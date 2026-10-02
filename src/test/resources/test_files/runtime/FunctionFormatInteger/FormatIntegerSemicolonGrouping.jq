(:JIQS: ShouldRun; Output="(true, true)" :)
format-integer(1234, "#;##0;") eq "1;234",
format-integer(1000000, "#;##0;") eq "1;000;000"
