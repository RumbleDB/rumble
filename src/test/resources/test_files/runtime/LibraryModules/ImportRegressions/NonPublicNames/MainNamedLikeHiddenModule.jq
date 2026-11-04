(:JIQS: ShouldRun; Output="(main-fn, hidden-fn)" :)
(: The main module does not import this namespace, so it may declare the same function as a module that is there. :)
import module namespace a = "urn:import-regression:hidden-user" at "hidden-user.jq";
declare namespace h = "urn:import-regression:hidden";
declare function h:f() { "main-fn" };
(h:f(), a:f())
