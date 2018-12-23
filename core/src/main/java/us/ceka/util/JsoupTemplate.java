package us.ceka.util;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JsoupTemplate {
	private static final Logger log = LoggerFactory.getLogger(JsoupTemplate.class);

	private int timeout;
	private String userAgent;
	private Map<String, String> endpoints;
	private boolean proxyEnable;
	private String proxyHost;
	private Integer proxyPort;
	private String proxyUser;
	private String proxyPassword;

	public JsoupTemplate setTimeout(int timeout) {
		this.timeout = timeout;
		return this;
	}
	public void setUserAgent(String usaerAgent) {
		this.userAgent = usaerAgent;
	}
	public void setProxyEnable(boolean proxyEnable) {
		this.proxyEnable = proxyEnable;
	}
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}
	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}
	public void setProxyUser(String proxyUser) {
		this.proxyUser = proxyUser;
	}
	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}
	public JsoupTemplate setEndpoints(Map<String, String> endpoints) {
		this.endpoints = endpoints;
		return this;
	}
	public int getTimeout() {
		return timeout;
	}
	public Map<String, String> getEndpoints() {
		return endpoints;
	}

	public Document getDocumnetByAlias(String alias) {
		return getDocumnetByAlias(alias, new Object[]{});
	}

	public Document getDocumnetByAlias(String alias, Object... args) {
		String connectUrl = args.length > 0 ? String.format(endpoints.get(alias), args) : endpoints.get(alias) ;	
		return getDocument(connectUrl);
	}

	public Document getDocument(String url) {
		Document documnet = null;
		if(log.isInfoEnabled()) log.debug("Connecting to '{}'...", url);
		try {
			Connection connection = Jsoup.connect(url).timeout(timeout).userAgent(userAgent);
			if(proxyEnable) {
				Authenticator.setDefault(new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return (new PasswordAuthentication(proxyUser, proxyPassword.toCharArray()));
					}
				});
				connection.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)));
			}
				
			Connection.Response response = connection.execute();
			//Map<String, String> cookies = response.cookies();
			documnet = response.parse();

		} catch (IOException e) {
			log.error("Error in retrieving Jsoup document", e);
		}
		return documnet;		
	}
	
	@Override
	public String toString() {
		return String.format("JsoupTemplate [timeout:%d]", timeout);
	}
}
