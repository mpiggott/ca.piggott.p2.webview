package ca.piggott.p2.site.webview.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.equinox.p2.metadata.Version;

public class Category extends IU {
	private final Map<String, IU> ius = new HashMap<String, IU>();
	
	public Category(String id, Version version, String name,	String description) {
		super(id, version, name, description);
	}

	public void addIU(IU iu) {
		IU alreadyReferenced = ius.get(iu.getId());
		if (alreadyReferenced == null) {
			ius.put(iu.getId(), iu);
			return;
		}
		
		//We only show the id of the most recent IU
		if (alreadyReferenced.getVersion().compareTo(iu.getVersion()) < 0) {
			ius.put(iu.getId(), iu);
			return;
		}
	}

	public Iterable<IU> getIus() {
		return Collections.unmodifiableCollection(ius.values());
	}
	
}