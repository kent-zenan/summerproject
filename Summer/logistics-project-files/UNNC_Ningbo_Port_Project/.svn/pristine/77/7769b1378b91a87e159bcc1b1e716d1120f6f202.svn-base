package server.model;

/**
 * Request line of a HTTP request.
 * 
 * This is submitted as part of G52APR course work, 2012.
 * Modified for Ningbo Port Project, 2013.
 * 
 * @author Jiaqi LI
 */
public class HttpRequestLine {
	private final String method;
	private final String path;
	private final String version;

	public HttpRequestLine(String m, String p, String v) {
		this.method = m.toUpperCase();
		this.path = p;
		this.version = v.toUpperCase();
	}

	public String getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}

	public String getVersion() {
		return version;
	}

	@Override
	public String toString() {
		if (version.compareTo("HTTP/0.9") == 0) {
			return method + " " + path + " \r\n";
		} else {
			return method + " " + path + " " + version + " \r\n";
		}
	}
}
