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

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.runtime.URIUtil;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

public class URIConverter implements IStringConverter<URI>{

	public URI convert(String value) {
		try {
			return URIUtil.fromString(value);
		} catch (URISyntaxException e) {
			throw new ParameterException(value);
		}
	}

}
