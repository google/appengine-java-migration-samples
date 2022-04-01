/*
 * Copyright 2022 Google Inc.
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

import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.cloud.datastore.Datastore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link DatastoreServlet}.
 */
@RunWith(JUnit4.class)
public class DatastoreServletTest {
    private static final String FAKE_URL = "fake.fk/hello";
    private static final String FAKE_IP = "192.158.1.38";
    // Set up a helper so that the ApiProxy returns a valid environment for local testing.
    private final LocalServiceTestHelper helper = new LocalServiceTestHelper();

    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private Datastore mockDatastore;
    private StringWriter responseWriter;
    private DatastoreServlet servletUnderTest;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        helper.setUp();

        //  Set up some fake HTTP requests
        when(mockRequest.getRequestURI()).thenReturn(FAKE_URL);

        // Set up a fake IP
        when(mockRequest.getRemoteAddr()).thenReturn(FAKE_IP);

        // Set up a fake HTTP response.
        responseWriter = new StringWriter();
        when(mockResponse.getWriter()).thenReturn(new PrintWriter(responseWriter));

        servletUnderTest = new DatastoreServlet();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    //    TODO: Uncomment this test when the Datastore object is properly mocked out.
    //     Curently it actually calls the Datastore.run function, and writes to datastore.
    //     This causes a permissions issue when Cloud Build runs the test, because its service account lacks permissions
    //     to edit Datastore.
    @Test
    public void doGetWritesResponse() throws Exception {
//    servletUnderTest.doGet(mockRequest, mockResponse);
//
//    // We expect our hello world response.
//    assertThat(responseWriter.toString())
//        .contains("Last 10 visits");
    }

}
