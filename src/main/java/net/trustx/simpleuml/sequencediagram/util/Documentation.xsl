<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:output method="html"/>

<xsl:template match="/">
	<html>
		<head>
			<title>Design Documentation for Sequence</title>
		</head>
		<body><xsl:apply-templates /></body>
	</html>
</xsl:template>

<xsl:template match="Links">

	<ol>
		<xsl:apply-templates />
	</ol>


</xsl:template>

<xsl:template match="Definition"/>

<xsl:template match="Link">
	<xsl:if test="@Return='false'">
	<p><b>
	<li>
		<u><xsl:value-of select="@Class"/> - <xsl:value-of select="@Method"/>()</u>
		</li><br/></b>
		<xsl:value-of disable-output-escaping="yes" select="Comment"/>
		<table >
			<tbody>
				<xsl:apply-templates select="Arguments"/>
				<xsl:apply-templates select="Throws"/>
				<xsl:apply-templates select="Return"/>
				<xsl:apply-templates select="CallComment"/>
			</tbody>
		</table>
		</p><br/>
	</xsl:if>
</xsl:template>

<xsl:template match="Throws">
	<tr>
		<td valign="top"><b>Throws</b></td>
		<td><xsl:value-of select="."/></td>
	</tr>
</xsl:template>

<xsl:template match="Return">
	<tr>
		<td><b>Return:</b></td>
		<xsl:if test="not(string-length()=0)">
			<td><xsl:value-of select="."/>: <xsl:value-of select="@Description"/></td>
		</xsl:if>
		<xsl:if test="string-length()=0">
			<td>void</td>
		</xsl:if>			
		
	</tr>
</xsl:template>

<xsl:template match="CallComment">
	<tr>
		<xsl:if test="not(string-length()=0)">
			<td valign="top"><b>Comments:</b></td>
			<td><xsl:value-of select="."/></td>
		</xsl:if>	
	</tr>
</xsl:template>

<xsl:template match="Arguments">
	<tr>
		<td  valign="top"><b>Arguments:</b></td>
			<xsl:if test="count(./Param) > 0">
				<td><table border="1">
					<tbody>
						<tr>
							<th>Type</th>
							<th>Name</th>
							<th>Description</th>
						</tr>
						<xsl:apply-templates select="Param"/>
					</tbody>
				</table></td>
			</xsl:if>
			<xsl:if test="count(./Param) = 0">
				<td><table>
					<i>none</i>
				</table></td>
			</xsl:if>
	</tr>
</xsl:template>

<xsl:template match="Param">
	<tr>
			<td><xsl:value-of select="@Type"/></td>
			<td><xsl:value-of select="@Name"/></td>
			<td><xsl:value-of select="@Description"/></td>

	</tr>
</xsl:template>


</xsl:stylesheet>
