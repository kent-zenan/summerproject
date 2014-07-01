package server.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Request data model of a HTTP request. It consists of a RequestLine, headers
 * and entity body.
 * 
 * This is submitted as part of G52APR course work, 2012.
 * Modified for Ningbo Port Project, 2013.
 * 
 * @author Jiaqi LI
 * 
 */
public class HttpRequestModel {

	private HttpRequestLine reqLine;
	private HashMap<String, String> headers;
	private StringBuilder body;

	public HttpRequestModel(String m, String p) {
		this(m, p, "HTTP/0.9");
	}

	public HttpRequestModel(String m, String p, String v) {
		reqLine = new HttpRequestLine(m, p, v);
		headers = new HashMap<String, String>();
		body = new StringBuilder("");
	}

	public void addHeader(String key, String value) {
		headers.put(key.toLowerCase(), value.toLowerCase());
	}

	public HttpRequestLine getRequestLine() {
		return reqLine;
	}

	public String getHeader(String key) {
		return headers.get(key);
	}

	public StringBuilder getBody() {
		return body;
	}

	@Override
	public String toString() {
		return body.toString();
	}
}

