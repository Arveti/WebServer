package com.webserver.java;

import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * HttpResponse extension that only writes headers.
 */
public class HeadHttpResponse extends FileHttpResponse {

	private static Logger log = Logger.getLogger(HeadHttpResponse.class);

    /**
     * File to be sent to the user.
     */
    private File inputFile;

    public HeadHttpResponse(int statusCode, File inputFile) {
        super(statusCode, inputFile);
    }

    /**
     * This function writes the HTTP response to an output stream.
     *
     * @param out the target {@link OutputStream} for writing
     */
    public void write(OutputStream out) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(getResponseLine());
            writer.write("\r\n");

            for (String key: headers.keySet()) {
                writer.write(key + ":" + headers.get(key));
                writer.write("\r\n");
            }
            writer.write("\r\n");

            writer.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}

