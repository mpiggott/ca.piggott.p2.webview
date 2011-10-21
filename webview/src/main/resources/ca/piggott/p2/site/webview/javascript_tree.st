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