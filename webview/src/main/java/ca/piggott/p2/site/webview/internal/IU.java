package ca.piggott.p2.site.webview.internal;

public class IU {
	private String id;
	private String versionedId;
	private String name;
	private String description;

	public IU(String id, String versionedId, String name, String description) {
		this.id = id;
		this.versionedId = versionedId;
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
		return versionedId;
	}

	public int hashCode() {
		return versionedId.hashCode();
	}
}