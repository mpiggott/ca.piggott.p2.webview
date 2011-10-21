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
package ca.piggott.p2.site.webview.ui;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

public class P2SiteBuilderHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		new P2SiteBuilderDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell()).open();
		return null;
	}
}
