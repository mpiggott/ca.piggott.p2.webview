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
