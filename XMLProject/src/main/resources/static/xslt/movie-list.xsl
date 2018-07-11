<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ns="http://xml.netbeans.org/schema/movie">

    <xsl:output method="html"/>
    <xsl:template match="/">
        <xsl:apply-templates select="//ns:movies"/>
    </xsl:template>

    <xsl:template match="//ns:movies">

        <div class="row tm-albums-container grid">

            <xsl:for-each select="ns:movie">
                <div class="col-sm-6 col-12 col-md-6 col-lg-3 col-xl-3 tm-album-col">
                    <figure class="effect-sadie">
                        <img src="{ns:poster_link}" class="img-fluid"/>
                        <figcaption>
                            <h2 font-size="25px">
                                <strong>
                                    <xsl:value-of select="ns:title"/>
                                </strong>
                            </h2>
                            <p>
                                Rate:
                                <xsl:value-of select="ns:self_rate"/>
                                <br/>
                                <a href="/thong-tin/{ns:id}">
                                    <button class="btn btn-success">Chi tiết</button>
                                </a>
                            </p>
                        </figcaption>
                    </figure>
                </div>

            </xsl:for-each>

        </div>


    </xsl:template>


</xsl:stylesheet>