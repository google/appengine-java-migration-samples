/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.appengine;


import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// With @WebServlet annotation the webapp/WEB-INF/web.xml is no longer required.
@SuppressWarnings("serial")
@WebServlet(name = "datastore", value = "")
public class DatastoreServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        // store only the first two octets of a users ip address
        String userIp = req.getRemoteAddr();
        InetAddress address = InetAddress.getByName(userIp);
        if (address instanceof Inet6Address) {
            // nest indexOf calls to find the second occurrence of a character in a string
            // an alternative is to use Apache Commons Lang: StringUtils.ordinalIndexOf()
            userIp = userIp.substring(0, userIp.indexOf(":", userIp.indexOf(":") + 1)) + ":*:*:*:*:*:*";
        } else if (address instanceof Inet4Address) {
            userIp = userIp.substring(0, userIp.indexOf(".", userIp.indexOf(".") + 1)) + ".*.*";
        }

        // Initialize a client
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        // Prepare a new entity
        String kind = "visit";
        Entity visit = new Entity(kind);
        visit.setProperty("user_ip", userIp);
        visit.setProperty("timestamp", Instant.now().toString());

        // Save the entity
        datastore.put(visit);

        // Retrieve the last 10 visits from the datastore, ordered by timestamp.
        Query query = new Query(kind).addSort("timestamp", Query.SortDirection.DESCENDING);
        List<Entity> results = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(10));

        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();
        out.print("Last 10 visits:\n");
        for(Entity entity : results) {
            Map<String, Object> properties = entity.getProperties();
            out.format(
                "Time: %s Addr: %s\n", properties.get("timestamp"), properties.get("user_ip"));
        }
    }
}