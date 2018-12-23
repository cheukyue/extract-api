package us.ceka.odds.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.easyrules.api.RulesEngine;
import org.easyrules.spring.RulesEngineFactoryBean;
import org.easyrules.spring.SpringRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import us.ceka.util.JsoupTemplate;

@Configuration 
@ComponentScan(basePackages = "us.ceka")
@PropertySource(name = "urlProperties", value = { "classpath:url.properties" })
@PropertySource(value = {"classpath:config.properties"} )
public class AppConfig {

	private static final Logger log = LoggerFactory.getLogger(AppConfig.class);

	@Autowired
	private ConfigurableEnvironment env;
	@Autowired
	private ApplicationContext applicationContext;

	@Bean
	public MessageSource messageSource() {
		log.info("initialising resource bundle...");
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages");
		return messageSource;
	}

	@Bean
	public RulesEngine rulesEngine() {
		List<Object> l = new ArrayList<Object>();
		applicationContext.getBeansWithAnnotation(SpringRule.class).forEach((name, rule) -> {
			l.add(rule);
		});

		log.info("initialising rule engine...");

		RulesEngineFactoryBean rulesEngineFactoryBean = new RulesEngineFactoryBean();
		//rulesEngineFactoryBean.setRules(l);
		rulesEngineFactoryBean.setSilentMode(true);
		return rulesEngineFactoryBean.getObject();
	}

	@Bean
	public JsoupTemplate jsoupTemplate() {
		log.info("initialising Jsoup template...");
		JsoupTemplate jt = new JsoupTemplate();

		Map<String, String> endpointMap = new HashMap<String, String>();   	
		MutablePropertySources propertySources = env.getPropertySources();

		if(propertySources.get("urlProperties") instanceof EnumerablePropertySource) {
			EnumerablePropertySource<?> propertySource = (EnumerablePropertySource<?>) propertySources.get("urlProperties");
			for(String key : propertySource.getPropertyNames()) {
				Object value = propertySource.getProperty(key); 
				if(value instanceof String) endpointMap.put(key, (String)value);
			}
		}
		jt.setEndpoints(endpointMap);
		jt.setTimeout(env.getProperty("jsoup.timeout", Integer.class, 3000));
		jt.setUserAgent(env.getRequiredProperty("jsoup.userAgent"));
		jt.setProxyEnable(env.getProperty("jsoup.proxy.enable", Boolean.class, false));
		jt.setProxyHost(env.getProperty("jsoup.proxy.host"));
		jt.setProxyPort(env.getProperty("jsoup.proxy.port", Integer.class, 80));
		jt.setProxyUser(env.getProperty("jsoup.proxy.user"));
		jt.setProxyPassword(env.getProperty("jsoup.proxy.password"));

		return jt;
	}

	@Bean
	public RestTemplate restTemplate() {
		log.info("initialising Rest Template...");
		
	    BasicCookieStore cookieStore = new BasicCookieStore();
	    BasicClientCookie cookie = new BasicClientCookie("s_visit", "1");
	    cookie.setDomain(".hkjc.com");
	    cookie.setPath("/");
	    cookieStore.addCookie(cookie);
		
		RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();

		RestTemplate restTemplate = new RestTemplate(
				new HttpComponentsClientHttpRequestFactory(
						HttpClientBuilder.create().setDefaultCookieStore(cookieStore).setDefaultRequestConfig(config).build()
						));

		//restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
/*
		List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
		if (CollectionUtils.isEmpty(interceptors)) {
			interceptors = new ArrayList<>();
		}
		interceptors.add(new RestTemplateHeaderModifierInterceptor());
		restTemplate.setInterceptors(interceptors);
	*/	
		//return new RestTemplateWithCookies();
		return restTemplate;
	}

}
