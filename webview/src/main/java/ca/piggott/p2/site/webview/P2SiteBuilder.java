/*******************************************************************************
 * Copyright (c) 2011 Matthew Piggott. All rights reserved. This
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Matthew Piggott - initial API and implementation
 *******************************************************************************/
package ca.piggott.p2.site.webview;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.antlr.stringtemplate.NoIndentWriter;
import org.antlr.stringtemplate.StringTemplate;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.VersionedId;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;

public class P2SiteBuilder {
	
	public static class IU {
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

	public static class Category extends IU {
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

	public static void write(IMetadataRepository repository, OutputStream out) throws IOException {
		if (repository == null || out == null) {
			throw new IllegalArgumentException("Outputstream or Repository is null");
		}

		StringTemplate tree = new StringTemplate(getResource("category.st"));
		tree.setAttribute("categories", getRepository(repository));

		StringTemplate body = new StringTemplate(getResource("template.st"));
		body.setAttribute("body", tree.toString());
		body.setAttribute("title", repository.getName());
		Writer writer = null;
		try {
			writer = new OutputStreamWriter(out);
			body.write(new NoIndentWriter(writer));
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	private static final IProgressMonitor monitor = new NullProgressMonitor();

	public static Collection<Category> getRepository(IMetadataRepository repository) {
		Set<Category> repositoryData = new HashSet<Category>();
		for (IInstallableUnit cat : repository.query(QueryUtil.createIUCategoryQuery(), monitor).toSet()) {
			Category category = getCategory(cat);
			for (IInstallableUnit iu : repository.query(QueryUtil.createIUCategoryMemberQuery(cat), monitor).toSet()) {
				category.addIU(getIU(iu));
			}
			repositoryData.add(category);
		}

		return repositoryData;
	}

	private static Category getCategory(IInstallableUnit iu) {
		return new Category(iu.getId(), new VersionedId(iu.getId(), iu.getVersion()).toString(), iu.getProperty(IInstallableUnit.PROP_NAME, null), iu.getProperty(IInstallableUnit.PROP_DESCRIPTION, null));
	}

	private static IU getIU(IInstallableUnit iu) {
		return new IU(iu.getId(), new VersionedId(iu.getId(), iu.getVersion()).toString(), iu.getProperty(IInstallableUnit.PROP_NAME, null), iu.getProperty(IInstallableUnit.PROP_DESCRIPTION, null));
	}
	
	private static String getResource(String resource) throws IOException {
		Reader in = new InputStreamReader( P2SiteBuilder.class.getResourceAsStream(resource) );
		char[] ch = new char[512];
		int read = -1;
		StringBuilder sb = new StringBuilder();
		while ((read = in.read(ch)) > 0 ) {
			sb.append(ch, 0, read);
		}
		return sb.toString();
	}
}
