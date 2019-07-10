<html>
<head>
    <title>${fileName}</title>
</head>
<body>
<h1>${method}</h1>


<table >
<#list lines as line>
    <#if line.method == 1>
        <tr style="background-color:pink;">
            <td style="width:5%">${line.number}</td>
            <td>${line.content}</td>
        </tr>

    <#elseif line.method == 2>
        <tr style="background-color:greenyellow;">
            <td style="width:5%">${line.number}</td>
            <td>${line.content}</td>
        </tr>
    <#else>
        <tr >
            <td style="width:5%">${line.number}</td>
            <td>${line.content}</td>
        </tr>
    </#if>

</#list>
</table>
</body>
</html>