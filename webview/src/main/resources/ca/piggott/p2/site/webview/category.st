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