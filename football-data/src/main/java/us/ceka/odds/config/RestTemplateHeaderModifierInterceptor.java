package us.ceka.odds.config;

import java.io.IOException;
import java.util.Collections;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class RestTemplateHeaderModifierInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(
			HttpRequest request, 
			byte[] body, 
			ClientHttpRequestExecution execution) throws IOException {
		
		request.getHeaders().add("Cookie", "s_visit=1; gpv_p5=http://bet.hkjc.com/football/index.aspx");
		request.getHeaders().add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		request.getHeaders().add("Accept-Encoding", "gzip, deflate");
		request.getHeaders().add("Connection", "keep-alive");
		request.getHeaders().add("If-None-Match", "WA3b0005facbeff883");
		request.getHeaders().add("Upgrade-Insecure-Requests", "1");
		request.getHeaders().add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:62.0) Gecko/20100101 Firefox/62.0");
		
		System.out.println(request.getHeaders());
		ClientHttpResponse response = execution.execute(request, body);
		//response.getHeaders().add("Cookie", "s_visit=1; gpv_p5=http://bet.hkjc.com/football/index.aspx");
		System.out.println(response.getHeaders());
		return response;
	}
}