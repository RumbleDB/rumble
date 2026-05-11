(:JIQS: ShouldRun; Output="(test[[op/add-dayTimeDurations.xml] cbcl-plus-005], test[[op/add-dayTimeDurations.xml] cbcl-plus-006])" :)
(doc("../../../queries/xml/OpTest.xml")/testsuite/testcase[contains($$/data(system-out), "1997-01-01T12")]/@name ! data())[position() lt 3]

(: mix of things :)