package com.app.java;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.webserver.java.EmptyHttpResponse;
import com.webserver.java.FileHttpResponse;
import com.webserver.java.HeadHttpResponse;
import com.webserver.java.HttpMethod;
import com.webserver.java.HttpRequest;
import com.webserver.java.HttpResponse;
import com.webserver.java.HttpStatus;
import com.webserver.java.StreamHttpResponse;

/**
 * A static file server application.
 */
public class FileServingApp implements WebApp {

    private final String documentRoot;

    public FileServingApp(String documentRoot) {
        this.documentRoot = documentRoot;
    }

    @Override
    public HttpResponse handle(HttpRequest request) {

        String path = request.getPath();
        HttpResponse response;

        switch (request.getMethod()) {
            case HttpMethod.GET:
                Path requestedFile = Paths.get(documentRoot, path);
                if (requestedFile.normalize().startsWith(Paths.get(documentRoot).normalize())) {
                    if (Files.exists(requestedFile)) {
                        if (Files.isDirectory(requestedFile)) {
                            response = new EmptyHttpResponse(HttpStatus.FORBIDDEN);
                        } else {
                            response = new FileHttpResponse(HttpStatus.OK,
                                    new File(Paths.get(documentRoot, path).toString()));
                        }
                    } else {
                        response = new EmptyHttpResponse(HttpStatus.NOT_FOUND);
                    }
                } else {
                    response = new EmptyHttpResponse(HttpStatus.FORBIDDEN);
                }
                break;
            case HttpMethod.TRACE:
                response = new StreamHttpResponse(HttpStatus.OK, request.getInputStream());
                break;
            case HttpMethod.HEAD:
                if (Files.exists(Paths.get(documentRoot, path))) {
                    response = new HeadHttpResponse(HttpStatus.OK,
                            new File(Paths.get(documentRoot, path).toString()));
                } else {
                    response = new EmptyHttpResponse(HttpStatus.NOT_FOUND);
                }
                break;
            default:
                response = new EmptyHttpResponse(HttpStatus.NOT_IMPLEMENTED);
                break;
        }

        return response;
    }
}
