<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ns="http://xml.netbeans.org/schema/movie">

    <xsl:output method="html"/>
    <xsl:template match="/">
        <xsl:apply-templates select="//ns:movies"/>
    </xsl:template>

    <xsl:template match="//ns:movies">

            <xsl:for-each select="ns:movie">
                <div class="movie-item" style="background: url('{ns:poster_link}')no-repeat center;background-size: cover   ;">
                    <div class="movie-item-overlay">

                    </div>
                    <div class="movie-item-title">
                        <xsl:value-of select="ns:title"/>
                    </div>
                    <div class="movie-item-description">
                        <a href="/thong-tin/{ns:id}">
                            <button type="button"
                                    style="background-color: rgb(58, 179, 58); border: none; width: 100px; color: white; padding: 4%; border-radius: 5px;">
                                Chi Tiet
                            </button>
                        </a>
                    </div>
                </div>

            </xsl:for-each>

    </xsl:template>


</xsl:stylesheet>