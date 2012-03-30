<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<!--
 Copyright (c) 2011 Pascal Rapicault and others. This
 All rights reserved. This program and the accompanying materials
 are made available under the terms of the Eclipse Public License v1.0
 which accompanies this distribution, and is available at
 http://www.eclipse.org/legal/epl-v10.html

 Contributors:
     Pascal Rapicault - initial API and implementation
-->

<title>All provided capabilities</title>

<!-- redirect before the page is completely loaded so the users never have to load it all -->
<script type="text/javascript">
<!--
window.location.replace("./index.html");
//-->
</script>

</head>
<body>
<div>
	<dl id='ProvidedCapabilities'>
$capabilities:{capability|
	
	<dt>
		$capability.namespace$/$capability.name$/$capability.version$
		<div class='capability.namespace'>$capability.namespace$</div>
		<div class='capability.name'>$capability.name$</div>
		<div class='capability.version'>$capability.version$</div>
	</dt>	
}$
	</dl>
</div>
</body>
</html>
