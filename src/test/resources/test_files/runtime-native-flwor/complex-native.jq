(:JIQS: ShouldRun; Output="({ "x" : 10, "y" : 19 }, { "x" : 30, "y" : 20 }, { "x" : 50, "y" : 20 }, { "x" : 70, "y" : 20 }, { "x" : 90, "y" : 20 }, { "x" : 110, "y" : 1 })" :)
import module namespace hep = "hep.jq";
hep:histogram(annotate((1 to 100)!{"foo":$$}, {"foo":"float"}).foo, 0, 100, 5)
