(:JIQS: ShouldRun; Output="2" :)
(: Each module of an import cycle may declare the query's context item. :)
import module namespace a = "urn:import-regression:context-a" at "context-a.jq";
declare context item := 5;
a:f()
