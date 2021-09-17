package com.dfki.services.RmlMappingService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.nio.charset.Charset;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.graalvm.compiler.core.common.SuppressFBWarnings;

public final class Utils {

	private Utils() {

	}

	public static final String TURTLE_MEDIA_TYPE = "text/turtle";

	public static boolean isValidXml(final String xml) {
		try {
			SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
			InputStream inputStream = new ByteArrayInputStream(xml.getBytes(Charset.defaultCharset()));
			saxParser.parse(inputStream, new DefaultHandler());
		} catch (ParserConfigurationException | IOException | SAXException e) {
			return false;
		}
		return true;

	}
	public static boolean isValidJson(final String json) {
		try {
			new JSONObject(json);
		} catch (JSONException ex) {
			try {
				new JSONArray(json);
			} catch (JSONException ex1) {
				return false;
			}
		}
		return true;
	}

	@SuppressFBWarnings(value = "RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE",
		justification = "Unfixable external code")
	public static String sendGetRequest(final String uri) throws IOException {
		HttpGet request = new HttpGet(uri);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		request.addHeader("Accept", "text/turtle");
		try (CloseableHttpResponse response = httpClient.execute(request)) {
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity);
			return result;
		}
	}
}
