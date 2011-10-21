package ca.piggott.p2.site.webview.internal;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class Category extends IU {
	private final Set<IU> ius = new HashSet<IU>();

	public Category(String id, String versionedId, String name,	String description) {
		super(id, versionedId, name, description);
	}

	public void addIU(IU iu) {
		ius.add(iu);
	}

	public Iterable<IU> getIus() {
		return Collections.unmodifiableCollection(ius);
	}
}