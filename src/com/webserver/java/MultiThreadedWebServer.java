package com.webserver.java;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.app.java.FileServingApp;

public class MultiThreadedWebServer extends Thread {

	private static final int DEFAULT_PORT = 8080;
	private static final int MAX_CLIENT_WAIT_QUEUE = 25;
	private static final String DEFAULT_ROOT_FOLDER = "root";
	private static Logger log = Logger.getLogger(MultiThreadedWebServer.class);
	public static final String SERVER_NAME = "Adobe Server";

	public static void main(String[] args) throws IOException {
		try {
			new MultiThreadedWebServer().start(getDesiredPort(args), getFileDirectory(args));
		} catch (Exception exception) {
			log.error("Error Occured While Starting Web Server ", exception);
		}
	}

	private static String getFileDirectory(String[] args) {
		if (args.length > 1) return String.valueOf(args[1]);
		return DEFAULT_ROOT_FOLDER;
	}

	public static int getDesiredPort(String[] args) {
		if (args.length > 0) {
			int port = Integer.parseInt(args[0]);
			if (port > 0 && port < 65535)
				return port;
			else
				throw new NumberFormatException("Invalid port! Port value should be between 0 and 65535");
		}
		return DEFAULT_PORT;
	}

	public void start(int port, String fileDirectory) throws IOException {
		ServerSocket serverSocket = new ServerSocket(port, MAX_CLIENT_WAIT_QUEUE);
		log.info("Server Started on port - " + port);
		while (true) {
			Socket socket = serverSocket.accept();
			log.info("Client request accepted...");
			ClientHandler clientHandler = new ClientHandler(socket, new FileServingApp(fileDirectory));
		}
	}
}
