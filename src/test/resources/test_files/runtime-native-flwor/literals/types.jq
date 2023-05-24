(:JIQS: ShouldRun; Output="({ "boolean" : true, "boolean2" : false, "boolean3" : false, "boolean4" : false, "boolean5" : false, "boolean6" : true, "boolean7" : true, "boolean8" : true, "boolean9" : true, "boolean10" : true, "byte" : 12, "byte2" : 1, "byte3" : 0, "byte4" : 1, "byte5" : 2, "byte6" : 3, "short" : 12345, "short2" : 1, "short3" : 0, "short4" : 1, "short5" : 2, "short6" : 3, "int" : 12345, "int2" : 122345, "int3" : 1, "int4" : 0, "int5" : 1, "int6" : 2, "int7" : 3, "long" : 12345, "long2" : 12345, "long3" : 123546789000000, "long4" : 1, "long5" : 0, "long6" : 1, "long7" : 2, "long8" : 3, "integer" : 1235467890000001230498761230948, "integer2" : 12345, "integer3" : 1, "integer4" : 0, "integer5" : 1, "integer6" : 2, "integer7" : 3, "decimal" : 12354678900000.01230498761230948, "decimal2" : 12, "decimal3" : 12345, "decimal4" : 12345, "decimal5" : 12354678900000, "decimal6" : 1, "decimal7" : 0, "decimal8" : 1, "decimal9" : 2, "double" : 1.2354678900000012E13, "double2" : 0, "double3" : 12, "double4" : 12345, "double5" : 12345, "double6" : 12345, "double7" : 1.2354678900000012E30, "double8" : 1.2354678900000012E13, "double9" : 1, "double10" : 0, "float" : 1.23546789E13, "float2" : 0, "float3" : 12, "float4" : 12345, "float5" : 12345, "float6" : 12345, "float7" : 1.23546786E30, "float8" : 1.23546789E13, "float9" : 1, "float10" : 0, "string" : "123465789", "string2" : "12", "string3" : "12345", "string4" : "12345", "string5" : "12345", "string6" : "1235467890000001230498761230948", "string7" : "12354678900000.0123049876123094800", "string8" : "1.2354678900000012E13", "string9" : "1.23546789E13", "string10" : "2023-05-12", "string11" : "2023-05-12 21:34:56", "string12" : "1ABF", "anyURI" : "123465789", "date" : "2023-05-12", "date2" : "2023-05-12", "dateTimeStamp" : "2023-05-12T12:34:56.000+0", "dateTimeStamp2" : "2023-05-12T00:00:00.000+0", "hexBinary" : "1ABF", "null" : null }, { "boolean" : true, "boolean2" : false, "boolean3" : false, "boolean4" : false, "boolean5" : false, "boolean6" : true, "boolean7" : true, "boolean8" : true, "boolean9" : true, "boolean10" : true, "byte" : 12, "byte2" : 1, "byte3" : 0, "byte4" : 1, "byte5" : 2, "byte6" : 3, "short" : 12345, "short2" : 1, "short3" : 0, "short4" : 1, "short5" : 2, "short6" : 3, "int" : 12345, "int2" : 122345, "int3" : 1, "int4" : 0, "int5" : 1, "int6" : 2, "int7" : 3, "long" : 12345, "long2" : 12345, "long3" : 123546789000000, "long4" : 1, "long5" : 0, "long6" : 1, "long7" : 2, "long8" : 3, "integer" : 1235467890000001230498761230948, "integer2" : 12345, "integer3" : 1, "integer4" : 0, "integer5" : 1, "integer6" : 2, "integer7" : 3, "decimal" : 12354678900000.01230498761230948, "decimal2" : 12, "decimal3" : 12345, "decimal4" : 12345, "decimal5" : 12354678900000, "decimal6" : 1, "decimal7" : 0, "decimal8" : 1, "decimal9" : 2, "double" : 1.2354678900000012E13, "double2" : 0, "double3" : 12, "double4" : 12345, "double5" : 12345, "double6" : 12345, "double7" : 1.2354678900000012E30, "double8" : 1.2354678900000012E13, "double9" : 1, "double10" : 0, "float" : 1.23546789E13, "float2" : 0, "float3" : 12, "float4" : 12345, "float5" : 12345, "float6" : 12345, "float7" : 1.23546786E30, "float8" : 1.23546789E13, "float9" : 1, "float10" : 0, "string" : "123465789", "string2" : "12", "string3" : "12345", "string4" : "12345", "string5" : "12345", "string6" : "1235467890000001230498761230948", "string7" : "12354678900000.0123049876123094800", "string8" : "1.2354678900000012E13", "string9" : "1.23546789E13", "string10" : "2023-05-12", "string11" : "2023-05-12 21:34:56", "string12" : "1ABF", "anyURI" : "123465789", "date" : "2023-05-12", "date2" : "2023-05-12", "dateTimeStamp" : "2023-05-12T12:34:56.000+0", "dateTimeStamp2" : "2023-05-12T00:00:00.000+0", "hexBinary" : "1ABF", "null" : null })" :)
for $i in parallelize((1 to 2), 10)
let $boolean := boolean("false")
let $boolean := boolean(anyURI("false"))
let $boolean2 := boolean(integer(0))
let $boolean3 := boolean(decimal(0))
let $boolean4 := boolean(double(0))
let $boolean5 := boolean(float(0))
let $boolean6 := boolean("true")
let $boolean6 := boolean(anyURI("true"))
let $boolean7 := boolean(integer(1))
let $boolean8 := boolean(decimal(1))
let $boolean9 := boolean(double(1))
let $boolean10 := boolean(float(1))
let $byte := byte("12")
let $byte2 := byte(true)
let $byte3 := byte(false)
let $byte4 := byte(double(1))
let $byte5 := byte(float(2))
let $byte6 := byte(decimal(3))
let $short := short("12345")
let $short2 := short(true)
let $short3 := short(false)
let $short4 := short(double(1))
let $short5 := short(float(2))
let $short6 := short(decimal(3))
let $int := int($short)
let $int2 := int("122345")
let $int3 := int(true)
let $int4 := int(false)
let $int5 := int(double(1))
let $int6 := int(float(2))
let $int7 := int(decimal(3))
let $long := long($int)
let $long2 := long($short)
let $long3 := long("123546789000000")
let $long4 := long(true)
let $long5 := long(false)
let $long6 := long(double(1))
let $long7 := long(float(2))
let $long8 := long(decimal(3))
let $integer := integer("1235467890000001230498761230948")
let $integer2 := integer($int)
let $integer3 := integer(true)
let $integer4 := integer(false)
let $integer5 := integer(double(1))
let $integer6 := integer(float(2))
let $integer7 := integer(decimal(3))
let $decimal := decimal("12354678900000.01230498761230948")
let $decimal2 := decimal($byte)
let $decimal3 := decimal($short)
let $decimal4 := decimal($int)
let $decimal5 := decimal(integer("12354678900000"))
let $decimal6 := decimal(true)
let $decimal7 := decimal(false)
let $decimal8 := decimal(double(1))
let $decimal9 := decimal(float(2))
let $double := double($decimal)
let $double2 := double("1234.345e-2345")
let $double3 := double($byte)
let $double4 := double($short)
let $double5 := double($int)
let $double6 := double($long)
let $double7 := double($integer)
let $double8 := double($decimal)
let $double9 := double(true)
let $double10 := double(false)
let $float := float($decimal)
let $float2 := float("1234.345e-2345")
let $float3 := float($byte)
let $float4 := float($short)
let $float5 := float($int)
let $float6 := float($long)
let $float7 := float($integer)
let $float8 := float($decimal)
let $float9 := float(true)
let $float10 := float(false)
let $string := string(123465789)
let $string2 := string($byte)
let $string3 := string($short)
let $string4 := string($int)
let $string5 := string($long)
let $string6 := string($integer)
let $string7 := string($decimal)
let $string8 := string($double)
let $string9 := string($float)
let $string10 := string(date("2023-05-12"))
let $string11 := string(dateTimeStamp("2023-05-12T12:34:56-07:00"))
let $string12 := string(hexBinary("1ABF"))
let $anyURI := anyURI($string)
let $date := date("2023-05-12")
let $date2 := date(dateTimeStamp("2023-05-12T12:34:56Z"))
let $dateTimeStamp := dateTimeStamp("2023-05-12T12:34:56+02:00")
let $dateTimeStamp2 := dateTimeStamp($date)
let $hexBinary := hexBinary("1ABF")
let $null := "null" cast as null
return {
  "boolean" : $boolean,
  "boolean2" : $boolean2,
  "boolean3" : $boolean3,
  "boolean4" : $boolean4,
  "boolean5" : $boolean5,
  "boolean6" : $boolean6,
  "boolean7" : $boolean7,
  "boolean8" : $boolean8,
  "boolean9" : $boolean9,
  "boolean10" : $boolean10,
  "byte" : $byte,
  "byte2" : $byte2,
  "byte3" : $byte3,
  "byte4" : $byte4,
  "byte5" : $byte5,
  "byte6" : $byte6,
  "short" : $short,
  "short2" : $short2,
  "short3" : $short3,
  "short4" : $short4,
  "short5" : $short5,
  "short6" : $short6,
  "int" : $int,
  "int2" : $int2,
  "int3" : $int3,
  "int4" : $int4,
  "int5" : $int5,
  "int6" : $int6,
  "int7" : $int7,
  "long" : $long,
  "long2" : $long2,
  "long3" : $long3,
  "long4" : $long4,
  "long5" : $long5,
  "long6" : $long6,
  "long7" : $long7,
  "long8" : $long8,
  "integer" : $integer,
  "integer2" : $integer2,
  "integer3" : $integer3,
  "integer4" : $integer4,
  "integer5" : $integer5,
  "integer6" : $integer6,
  "integer7" : $integer7,
  "decimal" : $decimal,
  "decimal2" : $decimal2,
  "decimal3" : $decimal3,
  "decimal4" : $decimal4,
  "decimal5" : $decimal5,
  "decimal6" : $decimal6,
  "decimal7" : $decimal7,
  "decimal8" : $decimal8,
  "decimal9" : $decimal9,
  "double" : $double,
  "double2" : $double2,
  "double3" : $double3,
  "double4" : $double4,
  "double5" : $double5,
  "double6" : $double6,
  "double7" : $double7,
  "double8" : $double8,
  "double9" : $double9,
  "double10" : $double10,
  "float" : $float,
  "float2" : $float2,
  "float3" : $float3,
  "float4" : $float4,
  "float5" : $float5,
  "float6" : $float6,
  "float7" : $float7,
  "float8" : $float8,
  "float9" : $float9,
  "float10" : $float10,
  "string": $string,
  "string2": $string2,
  "string3": $string3,
  "string4": $string4,
  "string5": $string5,
  "string6": $string6,
  "string7": $string7,
  "string8": $string8,
  "string9": $string9,
  "string10": $string10,
  "string11": $string11,
  "string12": $string12,
  "anyURI" : $anyURI,
  "date" : $date,
  "date2" : $date2,
  "dateTimeStamp" : $dateTimeStamp,
  "dateTimeStamp2" : $dateTimeStamp2,
  "hexBinary" : $hexBinary,
  "null" : $null
}
