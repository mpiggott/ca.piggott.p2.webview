<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<!--
 Copyright (c) 2011 Matthew Piggott. All rights reserved. This
 All rights reserved. This program and the accompanying materials
 are made available under the terms of the Eclipse Public License v1.0
 which accompanies this distribution, and is available at
 http://www.eclipse.org/legal/epl-v10.html

 Contributors:
     Matthew Piggott - initial API and implementation
-->
<title>$title$</title>

$template.styleDojo$
$template.styleCss$
$template.jsDojo$
$template.jsTree$
	</head>
	<body class="claro" style="width:100%">
		<div dojoType="dijit.layout.BorderContainer" style="width: 700px; height: 700px;">
			<div dojoType="dijit.layout.ContentPane" region="top">
				<h2>$title$</h2>
			</div>
			<div dojoType="dijit.layout.AccordionContainer" region="leading" style="height:50%; width:690px">
				<div dojoType="dijit.layout.AccordionPane" title="Available IUs" style="width:690px">
					<div id='treeNode'>
						<dl id='iuTree'>
$categories:{category|
	<dt>
		<div class='iuId'>$category.versionedId$</div>
		<div class='iuLabel'>$category.name$</div>
		<div class='iuDesc'>$category.description$</div>
	</dt>
	$category.ius:{iu|
	<dd>
		<div class='iuId'>$iu.versionedId$</div>
		<div class='iuLabel'>$iu.name$</div>
		<div class='iuDesc'>$iu.description$</div>
	</dd>
	}$
}$
						</dl>
					</div>
				</div>
			</div>
			<div dojoType="dijit.layout.AccordionContainer" region="bottom" style="height: 14EM">
				<div dojoType="dijit.layout.AccordionPane" title="Description" id='iuDescription'></div>
			</div>
		</div>
		<button dojoType="dijit.form.Button" id="help" iconClass="dijitEditorIcon helpEditorIcon" type="button">
		    Help!
            <script type="dojo/method" data-dojo-event="onClick" data-dojo-args="evt">
            	window.location = 'http://help.eclipse.org/helios/index.jsp?topic=/org.eclipse.platform.doc.user/tasks/tasks-127.htm';
            </script>
		</button>
$template.links$
	</body>
</html>