
<script type="text/javascript" >
$(function () {
	$("#searchText").keyup(function() {
		if ($(this).val().length > 0)
			$("#repoTree").jstree("search",$(this).val());
		if ($(this).val().length == 0) {
			$("#repoTree").jstree("clear_search");
			$("#repoTree").jstree("close_all",null, false);
		}
	});
	$("#repoTree").jstree({ 
		"ui" : {
			"select_limit" : 2,
			"select_multiple_modifier" : "alt",
			"selected_parent_close" : "select_parent",
			"initially_select" : [ "phtml_2" ]
		},
		"core" : { "initially_open" : [ "phtml_1" ] },
		"plugins" : [ "themes", "html_data", "ui", "checkbox", "search", "types", "ui" ],
		"search" : { "case_insensitive" : true, "show_only_matches" : true },
		"themes" : { "dots" : false, "icons" : true },
		"types" : {
			"types" : {
				"max_children" : -2,
				"p2Category" : {
					"icon" : { 
						"image" : "data:image/gif;base64,R0lGODlhEAAQAOYAALC91117rl99sF99r2OBs2OBsmeFtmeFtWuJuW+NvW+NvHORwGuKunORv3WTwbzF0uPr8t7o8N7o79fk7M/f6Mba487f573V37fR277V3rfR2p27tWmdi2eaiWygjmygjWmciqnNwG+kkHKpk3Opk3aulnmymXy1nHy2nHmymH64nbXMlb3Rl8bWmMfWmdDcm9nhneHmnuDlnubpoLeTJLmWJbuYJtC7eq6HH7GLIbSPIreSJPzejfzejvzhmf3mqPzmqP3qt/zqt/7vxv3uxf7y0v7z0v712qmBHKmAHauDHquEHq6HIP///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAAE0ALAAAAAAQABAAAAeZgE2Cg4SFhoeIiYQhKiohTTc2NjdNDw4OD4InMzMoTTZHRzZNDhAQDoImMTIpTTVGRTVNCxESDYIlMDAlTTtEQzRNChMTCYIjLy8kTTpCQTpNDBYUCIIiLS4iTTlAPzlNBhUVB4IfLCweTTg+PkxNBBcZBYIgKyscTUs9PEpNAxgaBAja0KHDBkhIklACECAAAEUQI0qcWCgQADs=" 
					},
					"hover_node" : false,
					"select_node" :
						function (value) {if (value.attr("description") == undefined) $("#description").text(""); else $("#description").text(value.attr("description"));}
				},
				"p2IU" : {
					"icon" : { 
						"image" : "data:image/gif;base64,R0lGODlhEAAQAOYAAMDN5DRhqz1qsUh0u26MvG6Mu3eTv5mv1Jiv06S42Ki72TtqsUh1u1SAxF2Iy36cyoejzoikzoijzYmkzZWu05Wt0qS52Ke72q7A3K/B3Iqlyv321frpnvvtsv/1zP/767WRJPvfifvfivrgjPvilPbflvzoqPvoqPzqr//xwsGOAL6LANK3ZvrafPvdg//rs//rtP/34f/5576KANKtUOzRi//lpP/127V+ALR+AK94ANKoR//y1ax0AOPBfd26fd27fdmyccOJNseLN76ENL2DNLNwJv///////wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAAEgALAAAAAAQABAAAAemgEiCQ4RDgoeIgkZHjEdGiYhDHBwlNT5BhYSRHpweHx+dnIaDKaUyDg4ypaWjSEIvsA4PDw4xMTAvQodENjY3DhETDioqNzdFhz88PKgIBw4oKMRAh6gOFxoKFw4nJjQjMzMrSAQEBQDoAA0bGywdIC4uSAP0FwYYGQwkJDstOTg5EC0QYCGBABEhdOiAJCgAhQoBevRgeCgABAkBKCYKkFGjR0SBAAA7" 
					},
					"hover_node" : false,
					"select_node" :
						function (value) {if (value.attr("description") == undefined) $("#description").text(""); else $("#description").text(value.attr("description"));}
				},
			}
		},
	});
	$("#export").click(function() {
		selection = $("#repoTree").jstree("get_checked", null, true);
		if (selection.size() == 0) {
			alert("Nothing to export. You need to select an item in the tree.");
			return;
		}
		var fileContent = "data:application/p2f+xml,";
		window.location.href=fileContent + generateP2F(selection, repoURL); //
	})
});

function generateP2F(selections, repoLocation) {
 	var header = "<?xml version='1.0' encoding='UTF-8'?>";
	header += "<?p2f version='1.0.0'?>";
	header += "<p2f version='1.0.0'>";
	
	var entry ="<ius size='" + selections.size() + "'>";
	selections.each(function(index, value) {
		if ($(this).attr("rel")=="p2IU") {
			entry += "<iu    id='" + $(this).attr("iu_id") + "'     name='" + $.trim($(this).text()) + "'    version='" + $(this).attr("iu_version") + "'>";
			entry += "<repositories size='1'>";
			entry += "<repository location='" + repoLocation + "'/>";
			entry += "</repositories>";
			entry += "</iu>";
		}
	});
	
	var footer = "</ius>";
	footer += "</p2f>";
	
	return header + entry + footer;
}

</script>
