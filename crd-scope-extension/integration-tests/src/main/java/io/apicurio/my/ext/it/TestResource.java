/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.apicurio.my.ext.it;

import io.apicurio.registry.fleetshard.CRScopeContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@ApplicationScoped
@Path("/test")
@Consumes({"text/plain"})
@Produces({"text/plain"})
public class TestResource {

    @Inject
    Data data;

    @POST
    @Path("/{scope}/{key}")
    public void put(@PathParam("scope") String scope, @PathParam("key") String key, String value) {
        CRScopeContext.getInstance().with(scope, () -> {
            data.getData().put(key, value);
            return null;
        });
    }

    @GET
    @Path("/{scope}/{key}")
    public String get(@PathParam("scope") String scope, @PathParam("key") String key) {
        return CRScopeContext.getInstance().with(scope, () -> data.getData().get(key));
    }

    @DELETE
    @Path("/{scope}/{key}")
    public void remove(@PathParam("scope") String scope, @PathParam("key") String key) {
        CRScopeContext.getInstance().with(scope, () -> {
            data.getData().remove(key);
            return null;
        });
    }
}
