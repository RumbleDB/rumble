(:JIQS: ShouldRun; Output="a.b/a.b a..b/a..b a./a. a.123/a.123 a.123b/a.123b a.1.2/a.1.2 a.1e2/a.1e2 a.1e-2/a.1e-2 a.-b/a.-b é.名/é.名 a.é/a.é element.name/element.name" :)
string-join(
    for $element in (
        <a.b a.b=""></a.b>,
        <a..b a..b=""></a..b>,
        <a. a.=""></a.>,
        <a.123 a.123=""></a.123>,
        <a.123b a.123b=""></a.123b>,
        <a.1.2 a.1.2=""></a.1.2>,
        <a.1e2 a.1e2=""></a.1e2>,
        <a.1e-2 a.1e-2=""></a.1e-2>,
        <a.-b a.-b=""></a.-b>,
        <é.名 é.名=""></é.名>,
        <a.é a.é=""></a.é>,
        <element.name element.name=""></element.name>
    )
    return local-name($element) || "/" || local-name($element/@*),
    " "
)
