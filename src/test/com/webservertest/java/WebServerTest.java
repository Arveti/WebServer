package com.webservertest.java;

import org.junit.Assert;
import org.junit.Test;

import com.webserver.java.MultiThreadedWebServer;

public class WebServerTest {

	@Test
	public void parseCorrectPortParam() {
		String[] args = new String[1];
		args[0] = "1234";
		Assert.assertEquals(1234, MultiThreadedWebServer.getDesiredPort(args));
	}

	@Test
	public void emptyParamsDefaultTo8080() {
		String[] args = new String[0];
		Assert.assertEquals(8080, MultiThreadedWebServer.getDesiredPort(args));
	}

	@Test(expected = NumberFormatException.class)
	public void wrongParamThrowException() {
		String[] args = new String[1];
		args[0] = "asda";
		Assert.assertEquals(8080, MultiThreadedWebServer.getDesiredPort(args));
	}

	@Test(expected = NumberFormatException.class)
	public void invalidPortThrowException() {
		String[] args = new String[1];
		args[0] = "0";
		Assert.assertEquals(8080, MultiThreadedWebServer.getDesiredPort(args));
	}

	@Test(expected = NumberFormatException.class)
	public void invalidPortTooHighThrowException() {
		String[] args = new String[1];
		args[0] = "65535";
		Assert.assertEquals(8080, MultiThreadedWebServer.getDesiredPort(args));
	}

}