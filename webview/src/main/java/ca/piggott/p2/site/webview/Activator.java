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

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	public void start(BundleContext ctx) throws Exception {
		context = ctx;
	}

	public void stop(BundleContext ctx) throws Exception {
		context = null;
	}

	public static BundleContext getContext() {
		return context;
	}
}
