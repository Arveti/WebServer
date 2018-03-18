package com.webserver.java;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Calendar;

import org.apache.log4j.Logger;

import com.app.java.WebApp;
import com.exceptions.java.BadRequestException;
import com.exceptions.java.ConnectionClosedException;

public class ClientHandler extends Thread {
	private static Logger log = Logger.getLogger(ClientHandler.class);

	private WebApp app;
	private Socket connection;
	private InputStream in;
	private OutputStream out;
	private boolean isKeepAlive = false;

	public ClientHandler(Socket socket, WebApp app) {
		this.connection = socket;
		this.app = app;
		start();
	}

	@Override
	public void run() {
		try {
			in = connection.getInputStream();
			out = connection.getOutputStream();
			HttpRequest request = HttpRequest.parse(in);

			if (request != null) {
				log.info(
						request.getRequestLine() + " from " + connection.getInetAddress() + ":" + connection.getPort());

				HttpResponse response = app.handle(request);

				response.getHeaders().put("Server", MultiThreadedWebServer.SERVER_NAME);
				response.getHeaders().put("Date", Calendar.getInstance().getTime().toString());
				if (request.isKeepAlive()) {
					isKeepAlive = true;
					response.getHeaders().put("Connection", "Keep-Alive");
					connection.setKeepAlive(true);
				} else {
					isKeepAlive = false;
					response.getHeaders().put("Connection", "close");
				}

				response.write(out);
			} else {
				isKeepAlive = false;
				log.error("Server accepts only HTTP protocol.");
				new RawHttpRequest(501, "Server only accepts HTTP protocol").write(out);
			}
		} catch (BadRequestException e) {
			isKeepAlive = false;
			log.error("Bad Request");
			new RawHttpRequest(400, "Server only accepts HTTP protocol").write(out);
		} catch (IOException e) {
			isKeepAlive = false;
			log.error("Error in client's IO.");
		} catch (ConnectionClosedException e) {
			isKeepAlive = false;
			log.info("Connection closed by client");
		} finally {
			try {
				if (!isKeepAlive)
					in.close();
			} catch (IOException e) {
				log.error("Error while closing input stream.");
			}
			try {
				if (!isKeepAlive)
					out.close();
			} catch (IOException e) {
				log.error("Error while closing output stream.");
			}
			try {
				if (!isKeepAlive)
					connection.close();
			} catch (IOException e) {
				log.error("Error while closing client socket.");
			}
		}

	}

}