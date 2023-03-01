declare variable $multiplier as xs:integer external;

<doc>{
count(//*) * $multiplier
}</doc>
