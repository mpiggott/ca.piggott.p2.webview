/*******************************************************************************
 * Copyright (c) 2011 Pascal Rapicault and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pascal Rapicault - initial API and implementation
 *******************************************************************************/
package ca.piggott.p2.site.webview;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.IProvisioningAgentProvider;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepository;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

public class Application implements IApplication {
	@Parameter( names = { "-r", "-repository"}, description = "Repository to generate the site for", required = true, converter=URIConverter.class )
	private URI repository;
	@Parameter( names = { "-o", "-output"}, description = "Folder in which the webpage is generated")
	private File outputLocation;
	
	private IProvisioningAgent agent;
	private ServiceReference<IProvisioningAgentProvider> agentProviderRef;
	
	public Object start(IApplicationContext context) throws Exception {
		initAgent();
		return run((String[]) context.getArguments().get("application.args")); //$NON-NLS-1$
	}

	//Return whether there are errors
	private boolean processArguments(String[] args) {
		JCommander commander = new JCommander(this);
		try {
			commander.parse(args);
		} catch(ParameterException e) {
			commander.usage();
			System.err.println(e.getMessage());
			return true;
		}
		return false;
	}

	private void initAgent() throws ProvisionException {
		BundleContext context = Activator.getContext();
		agentProviderRef = context.getServiceReference(IProvisioningAgentProvider.class);
		IProvisioningAgentProvider provider = context.getService(agentProviderRef);
		agent = provider.createAgent(null);
	}

	private IMetadataRepository loadMetadataRepository() {
		IMetadataRepositoryManager mgr = (IMetadataRepositoryManager) agent.getService(IMetadataRepositoryManager.SERVICE_NAME);
		System.out.println("Loading metadata repository: " + repository);
		try {
			return mgr.loadRepository(repository, new NullProgressMonitor());
		} catch (ProvisionException e1) {
			System.err.println("Problem loading metadata repository: " + repository);
		} catch (OperationCanceledException e1) {
			// Impossible from CLI
		}
		return null;
	}

	private IArtifactRepository loadArtifactRepository() {
		IArtifactRepositoryManager mgr = (IArtifactRepositoryManager) agent.getService(IArtifactRepositoryManager.SERVICE_NAME);
		System.out.println("Loading artifact repository: " + repository);
		try {
			return mgr.loadRepository(repository, new NullProgressMonitor());
		} catch (ProvisionException e1) {
			System.err.println("Problem loading artifact repository: " + repository);
		} catch (OperationCanceledException e1) {
			// Impossible from CLI
		}
		return null;
	}
	
	private Object run(String[] args) {
		if (processArguments(args))
			return new Integer(-13);
		
		IMetadataRepository repoGenerated = loadMetadataRepository();
		if (repoGenerated == null)
			return new Integer(-13);
		
		IArtifactRepository artifactRepo = loadArtifactRepository();
		if (artifactRepo == null)
			return new Integer(-13);
		
		File folder = getOutputFolder(outputLocation, repoGenerated);
		if (folder == null) {
			System.err.println("Could not find the output file");
			return new Integer(-13);
		}
		if (! folder.exists() && !folder.mkdirs()) {
			System.err.println("Coud not create folder:" + folder.getAbsolutePath());
			return new Integer(-13);
		}
		System.out.println("Writing out website.");
		try {
			P2SiteBuilder.writeMainPage(repoGenerated, folder);
			P2SiteBuilder.writeAllCapabilities(repoGenerated, folder);
			P2SiteBuilder.writeAllArtifacts(artifactRepo, folder);
		} catch (IOException e) {
			System.err.println("Problem writing the file: " + folder.getAbsolutePath());
			e.printStackTrace();
			return new Integer(-13);
		}
		System.out.println("Website generated successfully.");
		return IApplication.EXIT_OK;
	}

	private File getOutputFolder(File output, IMetadataRepository repo) {
		if (output != null)
			return output;
		if (URIUtil.isFileURI(repo.getLocation()))
			return URIUtil.toFile(repo.getLocation());
		return null;
	}

	public void stop() {
		Activator.getContext().ungetService(agentProviderRef);
		agent = null;
	}

}
