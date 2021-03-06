<#import "masterTemplate.ftl" as layout />
<@layout.masterTemplate title="Search results">

<form class="form-inline" method="GET" action="/search/">
<div class="form-group">
  <input type="text" name="q" class="form-control" placeholder="Search for..." value="${searchResult.query?html}">
</div>
<input class="btn btn-default" type="submit" value="Search" />
</form>

<h2>${searchResult.numTotalHits} Results for: <span style="color:#aaa;">${searchResult.query}</span></h2>


<#if searchResult.results?size == 0>
<p>No results found. Try with another search.</p>
</#if>

<#list searchResult.results>
    <dl>
    <#items as result>
        <dt><a href="${result.url}">${result.title}</a></dt>
        <dd>
        ${result.description}<br>
        <a href="${result.url}">${result.url}</a><br />
        <small style="color:#aaa;">${result.topic}</small>
        </dd><br>
    </#items>
    </dl>
</#list>

<div class="search-pagination">
    <center>
    <ul class="pagination"><#list searchResult.pages>
      <#items as page>
        <li <#if page == searchResult.page>class="active"</#if>> <a href="?q=${searchResult.query?html}&p=${page}">${page + 1}</a></li>
        </#items>
      </#list>
    </ul>
    </center>
</div>

</@layout.masterTemplate>