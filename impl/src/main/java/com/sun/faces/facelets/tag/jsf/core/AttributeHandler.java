/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.faces.facelets.tag.jsf.core;

import com.sun.faces.facelets.tag.TagHandlerImpl;

import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagException;
import java.io.IOException;

/**
 * Sets the specified name and attribute on the parent UIComponent. If the
 * "value" specified is not a literal, it will instead set the ValueExpression
 * on the UIComponent.
 * <p />
 * See <a target="_new"
 * href="http://java.sun.com/j2ee/javaserverfaces/1.1_01/docs/tlddocs/f/attribute.html">tag
 * documentation</a>.
 * 
 * @see javax.faces.component.UIComponent#getAttributes()
 * @see javax.faces.component.UIComponent#setValueExpression(java.lang.String,
 *      javax.el.ValueExpression)
 * @author Jacob Hookom
 */
public final class AttributeHandler extends TagHandlerImpl 
    implements javax.faces.view.facelets.AttributeHandler {

    private final TagAttribute name;

    private final TagAttribute value;

    /**
     * @param config
     */
    public AttributeHandler(TagConfig config) {
        super(config);
        this.name = this.getRequiredAttribute("name");
        this.value = this.getRequiredAttribute("value");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sun.facelets.FaceletHandler#apply(com.sun.facelets.FaceletContext,
     *      javax.faces.component.UIComponent)
     */
    @Override
    public void apply(FaceletContext ctx, UIComponent parent)
            throws IOException {
        if (parent == null) {
            throw new TagException(this.tag, "Parent UIComponent was null");
        }

        // only process if the parent is new to the tree
        if (parent.getParent() == null) {
            String n = getAttributeName(ctx);
            if (!parent.getAttributes().containsKey(n)) {
                if (this.value.isLiteral()) {
                    parent.getAttributes().put(n, this.value.getValue());
                } else {
                    parent.setValueExpression(n, this.value.getValueExpression(ctx, Object.class));
                }
            }
        }
    }


    // javax.faces.view.facelets.tag.AttributeHandler.getAttributeName()
    // implementation.
    @Override
    public String getAttributeName(FaceletContext ctxt) {
        return this.name.getValue(ctxt);
    }
}
