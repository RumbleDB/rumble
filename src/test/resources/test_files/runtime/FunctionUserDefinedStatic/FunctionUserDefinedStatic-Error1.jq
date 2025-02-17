(:JIQS: ShouldCrash; ErrorCode="XQST0039"; ErrorMetadata="LINE:2:COLUMN:38:" :)
declare function udf1 ($i as integer, $i as integer) as boolean {
    1 < $i
};
udf1(3), udf1(0)

(: Duplicate function param :)
