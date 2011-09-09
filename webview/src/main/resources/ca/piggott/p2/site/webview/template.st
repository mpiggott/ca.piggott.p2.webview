<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<title>Indigo repository</title>
<link rel="stylesheet" type="text/css" href="http://ajax.googleapis.com/ajax/libs/dojo/1.6/dojo/resources/dojo.css"> 
<link rel="stylesheet" type="text/css" href="http://ajax.googleapis.com/ajax/libs/dojo/1.6/dijit/themes/claro/claro.css">
<script src="http://ajax.googleapis.com/ajax/libs/dojo/1.6/dojo/dojo.xd.js" djConfig="parseOnLoad:true, isDebug:false" type="text/javascript"></script>
<script type="text/javascript">
dojo.require("dojo.data.ItemFileWriteStore");
dojo.require("dijit.Tree");
dojo.require("dijit.layout.AccordionContainer");
dojo.require("dijit.layout.BorderContainer");
dojo.require("dijit.layout.ContentPane");
dojo.require("dijit.form.Button");
var count = 0;
function iuNode(node) {
	var o = new Object();
	o.id = count++;
	o.iu = dojo.trim(dojo.query(".iuId", node)[0].innerHTML);
	o.label = dojo.trim(dojo.query(".iuLabel", node)[0].innerHTML);
	o.desc = dojo.trim(dojo.query(".iuDesc", node)[0].innerHTML);
	return o;
}

function buildTree() {
	var dataNodes = dojo.byId("iuTree").childNodes;
	
	var data = new Array();
	for (var i=0; i<dataNodes.length; i++) {
		var curNode = dataNodes.item(i);
		if (curNode.tagName == "DT") {	
			data.push(iuNode(curNode));
		} else if (curNode.tagName == "DD") {
			var parent = data.pop();
			if (parent.children == null) {
				parent.children = new Array();
			}
			parent.children.push(iuNode(curNode));
			data.push(parent);
		}
	}
	return data;
}

function changeSelection(item, selected) {
	if (selected) {
		if (dojo.byId(""+item.id) == null) {
			dojo.create("li", {id: item.id, iu: item.iu, innerHTML: item.label}, dojo.byId("iuSelected"));
		}
	} else {
		dojo.destroy(dojo.byId(""+item.id));
	}
}

dojo.addOnLoad(function(){
	//register();
    var store = new dojo.data.ItemFileWriteStore({
        data: {
            identifier: 'id',
            label: 'label',
            items: buildTree()
        }
    });
    var treeModel = new dijit.tree.ForestStoreModel({
        store: store
    });
    var treeControl = new dijit.Tree({
        model: treeModel,
        showRoot: false,
        openOnClick: true,
        getIconClass: function(item, opened){
        	return (!item || this.model.mayHaveChildren(item)) ? 
					"category" : 
					"iu";
        },
        onClick : function(item, node, evt){
   			dojo.byId("iuDescription").innerHTML = item.desc;
        },
        onOpen : function(item, node) {
				dojo.byId("iuDescription").innerHTML = '';
        },
        onClose : function(item, node) {
				dojo.byId("iuDescription").innerHTML = '';
        }
    },
    "treeNode");
	dojo.connect(dijit.byId("exportButton"), "onClick", function() {
		   var ius = dojo.query("#iuSelected > li");
    });
  }
);
		</script>
        <style type="text/css">
            body, html { font-family:helvetica,arial,sans-serif; font-size:90%; }
            .iu {
				background-image: url(data:image/gif;base64,R0lGODlhEAAQAOYAAMDN5DRhqz1qsUh0u26MvG6Mu3eTv5mv1Jiv06S42Ki72TtqsUh1u1SAxF2Iy36cyoejzoikzoijzYmkzZWu05Wt0qS52Ke72q7A3K/B3Iqlyv321frpnvvtsv/1zP/767WRJPvfifvfivrgjPvilPbflvzoqPvoqPzqr//xwsGOAL6LANK3ZvrafPvdg//rs//rtP/34f/5576KANKtUOzRi//lpP/127V+ALR+AK94ANKoR//y1ax0AOPBfd26fd27fdmyccOJNseLN76ENL2DNLNwJv///////wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAAEgALAAAAAAQABAAAAemgEiCQ4RDgoeIgkZHjEdGiYhDHBwlNT5BhYSRHpweHx+dnIaDKaUyDg4ypaWjSEIvsA4PDw4xMTAvQodENjY3DhETDioqNzdFhz88PKgIBw4oKMRAh6gOFxoKFw4nJjQjMzMrSAQEBQDoAA0bGywdIC4uSAP0FwYYGQwkJDstOTg5EC0QYCGBABEhdOiAJCgAhQoBevRgeCgABAkBKCYKkFGjR0SBAAA7);
				width: 16px;
				height: 16px;
			}
            .category {
				background-image: url(data:image/gif;base64,R0lGODlhEAAQAOYAALC91117rl99sF99r2OBs2OBsmeFtmeFtWuJuW+NvW+NvHORwGuKunORv3WTwbzF0uPr8t7o8N7o79fk7M/f6Mba487f573V37fR277V3rfR2p27tWmdi2eaiWygjmygjWmciqnNwG+kkHKpk3Opk3aulnmymXy1nHy2nHmymH64nbXMlb3Rl8bWmMfWmdDcm9nhneHmnuDlnubpoLeTJLmWJbuYJtC7eq6HH7GLIbSPIreSJPzejfzejvzhmf3mqPzmqP3qt/zqt/7vxv3uxf7y0v7z0v712qmBHKmAHauDHquEHq6HIP///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAAE0ALAAAAAAQABAAAAeZgE2Cg4SFhoeIiYQhKiohTTc2NjdNDw4OD4InMzMoTTZHRzZNDhAQDoImMTIpTTVGRTVNCxESDYIlMDAlTTtEQzRNChMTCYIjLy8kTTpCQTpNDBYUCIIiLS4iTTlAPzlNBhUVB4IfLCweTTg+PkxNBBcZBYIgKyscTUs9PEpNAxgaBAja0KHDBkhIklACECAAAEUQI0qcWCgQADs=);
				width: 16px;
				height: 16px;
			}
			.helpEditorIcon {
				background-image: url(data:image/gif;base64,R0lGODlhEAAQANUAALSxsuzk7OTk7Gx1jJWgt3eIpnGAnFpsi6u3y0Vqn0tigIKNmNnl656xuMPN0PT8/ODj483Mwvz1yfz32fzrlvzvt/vpqPvadvvcjd+/d/CyMPfEWefRptipU494Taakoa+JVsWeZ6NzWuPNw+zk5P///8TExP///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAACcALAAAAAAQABAAAAaSwJMwYlAYIsKk8tRYPJ6LhlDjAYUyp4hzwH0sIppOpWIKnQwPB/pAeBjECYsFdFI8HwgF46GoxC0YHmd3ek8GFXIYGxpZTg8FUBOJi0lNj14QIoCUSiNFBiQAAB6cSSAgIwECoh8ejEsaFBIgogCtSyexEiUlIra3Srq8JRIewEoXu7wSFB6CuBzDzBfPuNbXJ0EAOw==);
				width: 16px;
				height: 16px;
			}
			#iuSelected li {
				list-style-image: url(data:image/gif;base64,R0lGODlhEAAQAOYAAMDN5DRhqz1qsUh0u26MvG6Mu3eTv5mv1Jiv06S42Ki72TtqsUh1u1SAxF2Iy36cyoejzoikzoijzYmkzZWu05Wt0qS52Ke72q7A3K/B3Iqlyv321frpnvvtsv/1zP/767WRJPvfifvfivrgjPvilPbflvzoqPvoqPzqr//xwsGOAL6LANK3ZvrafPvdg//rs//rtP/34f/5576KANKtUOzRi//lpP/127V+ALR+AK94ANKoR//y1ax0AOPBfd26fd27fdmyccOJNseLN76ENL2DNLNwJv///////wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAAEgALAAAAAAQABAAAAemgEiCQ4RDgoeIgkZHjEdGiYhDHBwlNT5BhYSRHpweHx+dnIaDKaUyDg4ypaWjSEIvsA4PDw4xMTAvQodENjY3DhETDioqNzdFhz88PKgIBw4oKMRAh6gOFxoKFw4nJjQjMzMrSAQEBQDoAA0bGywdIC4uSAP0FwYYGQwkJDstOTg5EC0QYCGBABEhdOiAJCgAhQoBevRgeCgABAkBKCYKkFGjR0SBAAA7);
			}
        </style>
		<style type="text/css">body, html { font-family:helvetica,arial,sans-serif; font-size:90%; }.iu {background-image: url(data:image/gif;base64,R0lGODlhEAAQAOYAAMDN5DRhqz1qsUh0u26MvG6Mu3eTv5mv1Jiv06S42Ki72TtqsUh1u1SAxF2Iy36cyoejzoikzoijzYmkzZWu05Wt0qS52Ke72q7A3K/B3Iqlyv321frpnvvtsv/1zP/767WRJPvfifvfivrgjPvilPbflvzoqPvoqPzqr//xwsGOAL6LANK3ZvrafPvdg//rs//rtP/34f/5576KANKtUOzRi//lpP/127V+ALR+AK94ANKoR//y1ax0AOPBfd26fd27fdmyccOJNseLN76ENL2DNLNwJv///////wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAAEgALAAAAAAQABAAAAemgEiCQ4RDgoeIgkZHjEdGiYhDHBwlNT5BhYSRHpweHx+dnIaDKaUyDg4ypaWjSEIvsA4PDw4xMTAvQodENjY3DhETDioqNzdFhz88PKgIBw4oKMRAh6gOFxoKFw4nJjQjMzMrSAQEBQDoAA0bGywdIC4uSAP0FwYYGQwkJDstOTg5EC0QYCGBABEhdOiAJCgAhQoBevRgeCgABAkBKCYKkFGjR0SBAAA7);width: 16px;height: 16px;}.category {background-image: url(data:image/gif;base64,R0lGODlhEAAQAOYAALC91117rl99sF99r2OBs2OBsmeFtmeFtWuJuW+NvW+NvHORwGuKunORv3WTwbzF0uPr8t7o8N7o79fk7M/f6Mba487f573V37fR277V3rfR2p27tWmdi2eaiWygjmygjWmciqnNwG+kkHKpk3Opk3aulnmymXy1nHy2nHmymH64nbXMlb3Rl8bWmMfWmdDcm9nhneHmnuDlnubpoLeTJLmWJbuYJtC7eq6HH7GLIbSPIreSJPzejfzejvzhmf3mqPzmqP3qt/zqt/7vxv3uxf7y0v7z0v712qmBHKmAHauDHquEHq6HIP///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAAE0ALAAAAAAQABAAAAeZgE2Cg4SFhoeIiYQhKiohTTc2NjdNDw4OD4InMzMoTTZHRzZNDhAQDoImMTIpTTVGRTVNCxESDYIlMDAlTTtEQzRNChMTCYIjLy8kTTpCQTpNDBYUCIIiLS4iTTlAPzlNBhUVB4IfLCweTTg+PkxNBBcZBYIgKyscTUs9PEpNAxgaBAja0KHDBkhIklACECAAAEUQI0qcWCgQADs=);width: 16px;height: 16px;}</style>
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
$body$
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
	</body>
</html>