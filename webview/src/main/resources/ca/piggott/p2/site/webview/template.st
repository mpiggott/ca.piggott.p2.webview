<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<script type="text/javascript" src="_lib/jquery.js"></script>
	<script type="text/javascript" src="_lib/jquery.cookie.js"></script>
	<script type="text/javascript" src="_lib/jquery.hotkeys.js"></script>
	<script type="text/javascript" src="jquery.jstree.js"></script>

<!-- 	<link type="text/css" rel="stylesheet" href="syntax/!style.css"/> -->
<!-- 	<link type="text/css" rel="stylesheet" href="!style.css"/> -->
<!-- 	<script type="text/javascript" src="syntax/!script.js"></script> -->
</head>
<body>


<div class="panel">

<h3>$repoName$</h3>
<div>
Search: <input type="text" class="text" id="searchText" style=""/>
</div>
<div id="repoTree" class="p2RepoContainer" style="height:500px; overflow: auto; width: 500px; resize: both">
	<ul>
	$categories:{category|
	<li rel="p2Category" description="$category.description$">
		<a href="#">$category.name$</a>
			$category.ius:{iu|
			<ul>
				<li iu_id="$iu.id$" iu_version="$iu.version$" rel="p2IU" description="$iu.description$">
					<a href="#">$iu.name$</a>
				</li>
			</ul>
			}$
	</li>
	}$
	</ul>
</div>


<div id="description"></div>
<input type="button" class="button" value="Export" id="export" style=""/>

<a href="allArtifacts.html"></a>
<a href="allCapabilities.html"></a>

</div>

$template.treeScript$
<script type="text/javascript" >
	repoURL = '$repoURL$';
</script>
</body>
</html>