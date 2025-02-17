(:JIQS: ShouldRun; Output="" :)
declare function f() { 1 to 10 };
for $j in 1 to 10
where empty(f())
return $j
