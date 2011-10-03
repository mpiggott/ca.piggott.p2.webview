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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.antlr.stringtemplate.NoIndentWriter;
import org.antlr.stringtemplate.StringTemplate;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IProvidedCapability;
import org.eclipse.equinox.p2.metadata.VersionedId;
import org.eclipse.equinox.p2.query.IQueryResult;
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

	public static void writeMainPage(IMetadataRepository repository, File folder) throws FileNotFoundException, IOException {
		writeMainPage(repository, new FileOutputStream(new File(folder, "repoIndex.html")));
	}
	/**
	 * Populate the default template and write it to the provided {@code OutputStream}.
	 *
	 * @param repository repository to serialize
	 * @param out the output stream
	 * @throws IOException
	 */
	public static void writeMainPage(IMetadataRepository repository, OutputStream out) throws IOException {
		InputStream in = null;
		try {
			in = P2SiteBuilder.class.getResourceAsStream("template.st");
			write(repository, in, out);
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}
	
	private static Collection<IProvidedCapability> getAllCapabilities(IMetadataRepository repository) {
		IQueryResult<IInstallableUnit> allIUs = repository.query(QueryUtil.ALL_UNITS, new NullProgressMonitor());
		Iterator<IInstallableUnit> iterator = allIUs.iterator();
		Collection<IProvidedCapability> allCapabilities = new HashSet<IProvidedCapability>();
		while (iterator.hasNext()) {
			IInstallableUnit iu = (IInstallableUnit) iterator.next();
			allCapabilities.addAll(iu.getProvidedCapabilities());			
		}
		return allCapabilities;
	}
	
	public static void writeAllCapabilities(IMetadataRepository repository, File folder) throws IOException {
			Collection<IProvidedCapability> allCapabilities = getAllCapabilities(repository);
			writeAllCapabilities(allCapabilities, new File(folder, "allCapabilities.html"));
	}

	private static void writeAllCapabilities(Collection<IProvidedCapability> allCapabilities, File file) {
		InputStream in = null;
		try {
			try {
				in = P2SiteBuilder.class.getResourceAsStream("allProvidedCapabilities.st");

				StringTemplate body = new StringTemplate(getResource(in));
				body.setAttribute("capabilities", allCapabilities);

				Writer writer = new FileWriter(file);
				try {
					body.write(new NoIndentWriter(writer));
				} finally {
					if (writer != null) {
						writer.close();
					}
				}
			} finally {
				if (in != null) {
					in.close();
				}
			}
		} catch (IOException e) {

		}
	}
	/**
	 * Populate a {@code StringTemplate} provided by an {@code InputStream} and write it to the provided {@code OutputStream}.
	 *
	 * @param repository repository to serialize
	 * @param templateInputStream an {@code InputStream} of a {@code StringTemplate} template
	 * @param out the output stream
	 * @throws IOException
	 */
	public static void write(IMetadataRepository repository, InputStream templateInputStream, OutputStream out) throws IOException {
		if (repository == null || out == null) {
			throw new IllegalArgumentException("Outputstream or Repository is null");
		}

		StringTemplate body = new StringTemplate(getResource(templateInputStream));
		body.setAttribute("categories", getRepository(repository));
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
	
	private static String getResource(InputStream stream) throws IOException {
		Reader in = new InputStreamReader(stream);
		char[] ch = new char[512];
		int read = -1;
		StringBuilder sb = new StringBuilder();
		while ((read = in.read(ch)) > 0 ) {
			sb.append(ch, 0, read);
		}
		return sb.toString();
	}
}
