package com.app.java;

import com.webserver.java.HttpRequest;
import com.webserver.java.HttpResponse;
import com.webservertest.java.*;


/**
 * Applications that want to be compatible with the server must implement this interface.
 */
public interface WebApp {

    /**
     *
     * @param request {@link HttpRequest} sent by the client
     * @return a {@link HttpResponse}
     */
    HttpResponse handle(HttpRequest request);
}
