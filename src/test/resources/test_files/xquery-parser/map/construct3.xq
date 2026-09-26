(:JIQS: ShouldRun; Output="1 1 1 1 1" :)
map:size(<a><b>x</b></a>/map{b:2}),
map:size(<a><b>x</b></a>/map{self::a: b}),
map:size(<a><b>x</b></a>/map{*:b:b}),
map:size(<a><b>x</b></a>/map{* :b}),
map:size(<a><self>x</self></a>/map{self:2})
