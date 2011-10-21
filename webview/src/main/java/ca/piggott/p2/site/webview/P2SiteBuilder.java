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
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.antlr.stringtemplate.NoIndentWriter;
import org.antlr.stringtemplate.StringTemplate;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IProvidedCapability;
import org.eclipse.equinox.p2.metadata.VersionedId;
import org.eclipse.equinox.p2.metadata.expression.ExpressionUtil;
import org.eclipse.equinox.p2.query.ExpressionMatchQuery;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;

import ca.piggott.p2.site.webview.internal.Category;
import ca.piggott.p2.site.webview.internal.IU;
import ca.piggott.p2.site.webview.internal.Template;

public class P2SiteBuilder {

	private static final IProgressMonitor monitor = new NullProgressMonitor();

	/**
	 * Populate the default template and write it to the provided {@code OutputStream}.
	 *
	 * @param repository repository to serialize
	 * @param out the output stream
	 * @throws IOException
	 */
	public static void writeIndex(IMetadataRepository repository, OutputStream out) throws IOException {
		InputStream in = null;
		try {
			in = P2SiteBuilder.class.getResourceAsStream("template.st");
			writeIndex(repository, in, out);
		} finally {
			if (in != null) {
				in.close();
			}
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
	public static void writeIndex(IMetadataRepository repository, InputStream templateInputStream, OutputStream out) throws IOException {
		writeIndex(repository, templateInputStream, out, true);
	}

	/**
	 * Populate a {@code StringTemplate} provided by an {@code InputStream} and write it to the provided {@code OutputStream}.
	 *
	 * @param repository repository to serialize
	 * @param templateInputStream an {@code InputStream} of a {@code StringTemplate} template
	 * @param out the output stream
	 * @param categorizedOnly include categories
	 * @throws IOException
	 */
	public static void writeIndex(IMetadataRepository repository, InputStream templateInputStream, OutputStream out, boolean categorizedOnly) throws IOException {
		if (repository == null || out == null) {
			throw new IllegalArgumentException("Outputstream or Repository is null");
		}

		Template template = new Template();
		template.setJsDojo(getResource("javascript_dojo.st"));
		template.setJsTree(getResource("javascript_tree.st"));
		template.setStyleCss(getResource("style_css.st"));
		template.setStyleDojo(getResource("style_dojo.st"));
		template.setLinks(categorizedOnly ? "" : getResource("links.st"));

		StringTemplate body = new StringTemplate(toString(templateInputStream));
		body.setAttribute("categories", getCategories(repository));
		body.setAttribute("title", repository.getName());
		body.setAttribute("template", template);

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

	public static void writeAllCapabilities(IMetadataRepository repository, File file) {
		InputStream in = null;
		try {
			try {
				in = P2SiteBuilder.class.getResourceAsStream("allProvidedCapabilities.st");

				Collection<IProvidedCapability> allCapabilities = getAllCapabilities(repository);
				StringTemplate body = new StringTemplate(toString(in));
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

	public static void writeAllArtifacts(IArtifactRepository artifactRepo, File file) {
		Set<IArtifactKey> allArtifacts = artifactRepo.query(new ExpressionMatchQuery<IArtifactKey>(IArtifactKey.class, ExpressionUtil.TRUE_EXPRESSION), new NullProgressMonitor()).toUnmodifiableSet();

		InputStream in = null;
		try {
			try {
				in = P2SiteBuilder.class.getResourceAsStream("allArtifacts.st");

				StringTemplate body = new StringTemplate(toString(in));
				body.setAttribute("artifacts", allArtifacts);

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

	private static Collection<Category> getCategories(IMetadataRepository repository) {
		Set<Category> repositoryData = new HashSet<Category>();
		for (IInstallableUnit cat : repository.query(QueryUtil.createIUCategoryQuery(), monitor).toSet()) {
			Category category = toCategory(cat);
			for (IInstallableUnit iu : repository.query(QueryUtil.createIUCategoryMemberQuery(cat), monitor).toSet()) {
				category.addIU(toIU(iu));
			}
			repositoryData.add(category);
		}
		return repositoryData;
	}

	private static String getResource(String file) throws IOException {
		InputStream in = null;
		try {
			return toString(P2SiteBuilder.class.getResourceAsStream(file));
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}
	
	private static String toString(InputStream stream) throws IOException {
		Reader in = new InputStreamReader(stream);
		char[] ch = new char[512];
		int read = -1;
		StringBuilder sb = new StringBuilder();
		while ((read = in.read(ch)) > 0 ) {
			sb.append(ch, 0, read);
		}
		return sb.toString();
	}

	private static Category toCategory(IInstallableUnit iu) {
		return new Category(iu.getId(), new VersionedId(iu.getId(), iu.getVersion()).toString(), iu.getProperty(IInstallableUnit.PROP_NAME, null), iu.getProperty(IInstallableUnit.PROP_DESCRIPTION, null));
	}

	private static IU toIU(IInstallableUnit iu) {
		return new IU(iu.getId(), new VersionedId(iu.getId(), iu.getVersion()).toString(), iu.getProperty(IInstallableUnit.PROP_NAME, null), iu.getProperty(IInstallableUnit.PROP_DESCRIPTION, null));
	}
}
