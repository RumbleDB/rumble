(:JIQS: ShouldRun; Output="({ "Rank" : 1, "Count" : 0 }, { "Rank" : 2, "Count" : 2 }, { "Rank" : 3, "Count" : 2 }, { "Rank" : 4, "Count" : 1 })" :)
for $rank in 1 to 4
return {
  "Rank" : $rank,
  "Count" : count(
  for $i in json-lines("../../../queries/conf-ex.json", 100)
  where $i.target eq $i.choices[[$rank]]
  return 1)
}
