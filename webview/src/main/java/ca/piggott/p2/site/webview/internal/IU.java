package ca.piggott.p2.site.webview.internal;

import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionedId;

public class IU {
	private String id;
	private Version version;
	private String name;
	private String description;

	public IU(String id, Version version, String name, String description) {
		this.id = id;
		this.version = version;
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description == null ? name : description;
	}

	public String getId() {
		return id;
	}

	public String getVersionedId() {
		return new VersionedId(id, version).toString();
	}

	public Version getVersion() {
		return version;
	}
	
	public int hashCode() {
		return id.hashCode() + version.hashCode();
	}
}