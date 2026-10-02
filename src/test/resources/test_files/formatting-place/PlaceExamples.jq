(:JIQS: ShouldRun; Output="(-10:00, -05:00, +00:00, +05:30, +13:00, -10, -5, +0, +5:30, +13, -10:00, -5:00, +0:00, +5:30, +13:00, -10:00, -05:00, +00:00, +05:30, +13:00, -1000, -0500, +0000, +0530, +1300, -10:00, -05:00, Z, +05:30, +13:00, GMT-10:00, GMT-05:00, GMT+00:00, GMT+05:30, GMT+13:00, W, R, Z, +05:30, +13:00, 17:00 EST, 12:00 EST, 07:00 EST, 01:30 EST, 18:00 EST)" :)
let $times := (time("12:00:00-10:00"), time("12:00:00-05:00"), time("12:00:00+00:00"),
               time("12:00:00+05:30"), time("12:00:00+13:00"))
return (
  (for $t in $times return format-time($t, "[Z]")),
  (for $t in $times return format-time($t, "[Z0]")),
  (for $t in $times return format-time($t, "[Z0:00]")),
  (for $t in $times return format-time($t, "[Z00:00]")),
  (for $t in $times return format-time($t, "[Z0000]")),
  (for $t in $times return format-time($t, "[Z00:00t]")),
  (for $t in $times return format-time($t, "[z]")),
  (for $t in $times return format-time($t, "[ZZ]")),
  (for $t in $times return format-time($t, "[H00]:[m00] [ZN]", (), (), "America/New_York"))
)
