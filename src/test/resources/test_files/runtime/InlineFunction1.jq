(:JIQS: ShouldRun; Output="(1, 1)" :)

declare function local:f() {};

(function() {})(),
(function() {()})(),
(function() {1})(),
(function($x) {})(1),
(function($x) {()})(1),
(function($x) {1})(1),
local:f()
