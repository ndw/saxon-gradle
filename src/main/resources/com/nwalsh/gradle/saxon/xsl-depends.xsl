<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:f="https://nwalsh.com/ns/functions"
                exclude-result-prefixes="#all"
                version="3.0">

<xsl:output method="text" encoding="utf-8" indent="no"/>

<xsl:mode on-no-match="shallow-skip"/>

<xsl:template match="/">
  <xsl:variable name="uris" select="f:uris(/, resolve-uri(base-uri(/)))"/>
  <xsl:for-each select="$uris">
    <xsl:value-of select=". || '&#10;'"/>
  </xsl:for-each>
</xsl:template>

<xsl:function name="f:uris" as="xs:string*">
  <xsl:param name="node" as="node()"/>
  <xsl:param name="seen" as="xs:string*"/>

  <xsl:iterate select="$node//xsl:include|$node//xsl:import">
    <xsl:param name="local-seen" select="$seen"/>
    <xsl:on-completion select="$local-seen"/>

    <xsl:variable name="href"
                  select="if (contains(@href, '#'))
                          then substring-before(@href, '#')
                          else string(@href)"/>
    <xsl:variable name="href"
                  select="if (contains($href, '?'))
                          then substring-before($href, '?')
                          else $href"/>

    <xsl:variable name="uri" select="resolve-uri($href, base-uri(.))"/>
    <xsl:variable name="depends" as="xs:string*">
      <xsl:if test="not($uri = $local-seen) and starts-with($uri, 'file:')">
        <xsl:try>
          <xsl:sequence select="f:uris(doc($uri), ($local-seen, $uri))"/>
          <xsl:catch>
            <xsl:sequence select="()"/>
          </xsl:catch>
        </xsl:try>
      </xsl:if>
    </xsl:variable>

    <xsl:next-iteration>
      <xsl:with-param name="local-seen"
                      select="distinct-values(($local-seen, $depends))"/>
    </xsl:next-iteration>
  </xsl:iterate>
</xsl:function>

</xsl:stylesheet>
