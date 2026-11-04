(:JIQS: ShouldRun; Output="(main-var, b-fn)" :)
(: A variable that is not imported does not collide with one the main module declares. :)
import module namespace a = "urn:import-regression:chain-a" at "chain-a.jq";
declare namespace b = "urn:import-regression:chain-b";
declare variable $b:v := "main-var";
($b:v, a:f())
