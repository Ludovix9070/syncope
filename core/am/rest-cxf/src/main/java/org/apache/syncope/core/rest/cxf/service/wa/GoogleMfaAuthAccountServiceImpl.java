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
package org.apache.syncope.core.rest.cxf.service.wa;

import java.net.URI;
import java.util.List;
import javax.ws.rs.core.Response;
import org.apache.syncope.common.lib.to.PagedResult;
import org.apache.syncope.common.lib.wa.GoogleMfaAuthAccount;
import org.apache.syncope.common.rest.api.RESTHeaders;
import org.apache.syncope.common.rest.api.service.wa.GoogleMfaAuthAccountService;
import org.apache.syncope.core.logic.wa.GoogleMfaAuthAccountLogic;
import org.apache.syncope.core.rest.cxf.service.AbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoogleMfaAuthAccountServiceImpl extends AbstractServiceImpl implements GoogleMfaAuthAccountService {

    @Autowired
    private GoogleMfaAuthAccountLogic logic;

    @Override
    public void deleteFor(final String owner) {
        logic.deleteFor(owner);
    }

    @Override
    public void delete(final String key) {
        logic.delete(key);
    }

    @Override
    public void delete() {
        logic.deleteAll();
    }

    @Override
    public Response create(final String owner, final GoogleMfaAuthAccount acct) {
        String key = logic.create(owner, acct);
        URI location = uriInfo.getAbsolutePathBuilder().path(key).build();
        return Response.created(location).
                header(RESTHeaders.RESOURCE_KEY, key).
                build();
    }

    @Override
    public void update(final String owner, final GoogleMfaAuthAccount acct) {
        logic.update(owner, acct);
    }

    private PagedResult<GoogleMfaAuthAccount> build(final List<GoogleMfaAuthAccount> read) {
        PagedResult<GoogleMfaAuthAccount> result = new PagedResult<>();
        result.setPage(1);
        result.setSize(read.size());
        result.setTotalCount(read.size());
        result.getResult().addAll(read);
        return result;
    }

    @Override
    public PagedResult<GoogleMfaAuthAccount> readFor(final String owner) {
        return build(logic.readFor(owner));
    }

    @Override
    public GoogleMfaAuthAccount read(final String key) {
        return logic.read(key);
    }

    @Override
    public GoogleMfaAuthAccount read(final long id) {
        return logic.read(id);
    }

    @Override
    public PagedResult<GoogleMfaAuthAccount> list() {
        return build(logic.list());
    }
}