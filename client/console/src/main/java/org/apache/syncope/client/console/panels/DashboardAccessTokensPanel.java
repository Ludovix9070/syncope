/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.syncope.client.console.panels;

import org.apache.syncope.client.console.wizards.WizardMgtPanel;
import org.apache.syncope.common.lib.to.AccessTokenTO;
import org.apache.syncope.common.lib.types.StandardEntitlement;
import org.apache.wicket.Component;
import org.apache.wicket.PageReference;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.html.panel.Panel;

public class DashboardAccessTokensPanel extends Panel {

    private static final long serialVersionUID = -5540744119461583586L;

    public DashboardAccessTokensPanel(final String id, final PageReference pageRef) {
        super(id);

        WizardMgtPanel<AccessTokenTO> accessTokens = new AccessTokenDirectoryPanel.Builder(pageRef) {

            private static final long serialVersionUID = -5960765294082359003L;

        }.disableCheckBoxes().build("accessTokens");
        MetaDataRoleAuthorizationStrategy.authorize(
                accessTokens, Component.RENDER, StandardEntitlement.ACCESS_TOKEN_LIST);
        add(accessTokens);
    }

}
