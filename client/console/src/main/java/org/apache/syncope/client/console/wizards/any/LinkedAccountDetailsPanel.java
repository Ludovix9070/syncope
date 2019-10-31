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
package org.apache.syncope.client.console.wizards.any;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.syncope.client.console.commons.ConnIdSpecialName;
import org.apache.syncope.client.console.commons.Constants;
import org.apache.syncope.client.console.rest.BaseRestClient;
import org.apache.syncope.client.console.rest.ResourceRestClient;
import org.apache.syncope.client.console.wicket.markup.html.form.AjaxDropDownChoicePanel;
import org.apache.syncope.client.console.wicket.markup.html.form.AjaxTextFieldPanel;
import org.apache.syncope.client.lib.SyncopeClient;
import org.apache.syncope.common.lib.to.ConnObjectTO;
import org.apache.syncope.common.lib.to.LinkedAccountTO;
import org.apache.syncope.common.lib.types.AnyTypeKind;
import org.apache.syncope.common.rest.api.beans.ConnObjectTOQuery;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.ThrottlingSettings;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinkedAccountDetailsPanel extends WizardStep {

    private static final long serialVersionUID = 1221037007528732347L;

    private static final Logger LOG = LoggerFactory.getLogger(LinkedAccountDetailsPanel.class);

    private final ResourceRestClient resourceRestClient = new ResourceRestClient();

    private ArrayList<String> connObjectKeyFieldValues;

    public LinkedAccountDetailsPanel(final LinkedAccountTO linkedAccountTO) {
        super();
        setOutputMarkupId(true);

        AjaxDropDownChoicePanel<String> dropdownResourceField = new AjaxDropDownChoicePanel<>(
                "resource",
                "resource",
                new PropertyModel<>(linkedAccountTO, "resource"),
                false);
        dropdownResourceField.setChoices(resourceRestClient.list().stream().
                filter(resource -> resource.getProvision(AnyTypeKind.USER.name()).isPresent()
                && resource.getProvision(AnyTypeKind.USER.name()).get().getMapping() != null
                && !resource.getProvision(AnyTypeKind.USER.name()).get().getMapping().getItems().isEmpty()).
                map(resource -> resource.getKey()).
                collect(Collectors.toList()));
        dropdownResourceField.setOutputMarkupId(true);
        dropdownResourceField.addRequiredLabel();
        dropdownResourceField.setNullValid(false);
        dropdownResourceField.setRequired(true);
        add(dropdownResourceField);

        final String connObjectKeyFieldId = "connObjectKeyValue";
        AjaxTextFieldPanel connObjectKeyField = new AjaxTextFieldPanel(
                "connObjectKeyValue",
                "connObjectKeyValue",
                new PropertyModel<>(linkedAccountTO, "connObjectKeyValue"),
                false);
        connObjectKeyField.setOutputMarkupId(true);
        connObjectKeyField.addRequiredLabel();
        connObjectKeyField.setChoices(Collections.<String>emptyList());
        connObjectKeyField.setEnabled(false);
        add(connObjectKeyField);

        dropdownResourceField.getField().add(new AjaxFormComponentUpdatingBehavior(Constants.ON_CHANGE) {

            private static final long serialVersionUID = -1107858522700306810L;

            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                boolean enabled = dropdownResourceField.getModelObject() != null
                        && !dropdownResourceField.getModelObject().isEmpty();
                connObjectKeyField.setEnabled(enabled);
                if (enabled) {
                    setConnObjectFieldChoices(connObjectKeyField, dropdownResourceField.getModelObject(), null);
                }
                target.add(connObjectKeyField);
            }
        });

        connObjectKeyField.getField().setMarkupId(connObjectKeyFieldId);
        connObjectKeyField.getField().add(new AjaxEventBehavior(Constants.ON_KEYDOWN) {

            private static final long serialVersionUID = 3533589614190959822L;

            @Override
            protected void onEvent(final AjaxRequestTarget target) {
                String searchTerm = connObjectKeyField.getField().getInput();
                if (StringUtils.isNotBlank(searchTerm) && searchTerm.length() > 1) {
                    setConnObjectFieldChoices(connObjectKeyField, dropdownResourceField.getModelObject(), searchTerm);

                    // If elements are found, send an "arrow down" key event to open input autocomplete dropdown
                    target.appendJavaScript(connObjectKeyFieldValues.isEmpty()
                            ? "$('#" + connObjectKeyFieldId + "-autocomplete-container').hide();"
                            : "var simulatedEvent = new KeyboardEvent('keydown', {keyCode: 40, which: 40}); "
                            + "document.getElementById('" + connObjectKeyFieldId + "').dispatchEvent(simulatedEvent);");
                }
            }

            @Override
            protected void updateAjaxAttributes(final AjaxRequestAttributes attributes) {
                super.updateAjaxAttributes(attributes);

                AjaxCallListener listener = new AjaxCallListener() {

                    private static final long serialVersionUID = 2208168001920794667L;

                    @Override
                    public CharSequence getPrecondition(final Component component) {
                        // Eevaluates weather an ajax call will be executed or not.
                        // If the key code is "arrow down" or "arrow up" do NOT trigger the event callback
                        return "var keycode = Wicket.Event.keyCode(attrs.event); "
                                + "if ((keycode == 40) || (keycode == 38)) {return false;} return true;";
                    }
                };
                attributes.getAjaxCallListeners().add(listener);
                attributes.setThrottlingSettings(new ThrottlingSettings("id", Duration.seconds(1.2), true));
            }
        });
    }

    private void setConnObjectFieldChoices(
            final AjaxTextFieldPanel ajaxTextFieldPanel,
            final String resource,
            final String searchTerm) {

        Set<String> choices = new HashSet<>();
        String resourceRemoteKey = ConnIdSpecialName.NAME;
        try {
            resourceRemoteKey = resourceRestClient.read(resource).getProvision(AnyTypeKind.USER.name()).get().
                    getMapping().getConnObjectKeyItem().getExtAttrName();
        } catch (Exception ex) {
            LOG.error("While reading mapping for resource {}", resource, ex);
        }

        ConnObjectTOQuery.Builder builder = new ConnObjectTOQuery.Builder().
                size(2).
                orderBy(BaseRestClient.toOrderBy(new SortParam<>(resourceRemoteKey, true)));
        if (StringUtils.isNotBlank(searchTerm)) {
            builder.fiql(SyncopeClient.getConnObjectTOFiqlSearchConditionBuilder().
                    is(resourceRemoteKey).equalTo(searchTerm + "*").query()).build();
        }
        Pair<String, List<ConnObjectTO>> items = resourceRestClient.searchConnObjects(resource,
                AnyTypeKind.USER.name(),
                builder.build());

        choices.addAll(items.getRight().stream().map(item -> {
            return item.getAttr(ConnIdSpecialName.UID).get().getValues().get(0);
        }).collect(Collectors.toSet()));

        connObjectKeyFieldValues = new ArrayList<>(choices);
        Collections.sort(connObjectKeyFieldValues);
        ajaxTextFieldPanel.setChoices(connObjectKeyFieldValues);
    }

}
