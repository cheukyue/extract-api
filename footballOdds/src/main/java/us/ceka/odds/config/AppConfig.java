package us.ceka.odds.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    	
    	RulesEngineFactoryBean rulesEngineFactoryBean = new RulesEngineFactoryBean();
    	//rulesEngineFactoryBean.setRules(l);
    	rulesEngineFactoryBean.setSilentMode(true);
    	return rulesEngineFactoryBean.getObject();
    }
    
    @Bean
    public JsoupTemplate jsoupTemplate() {
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
    	jt.setUsaerAgent(env.getRequiredProperty("jsoup.userAgent"));
    	jt.setProxyEnable(env.getProperty("jsoup.proxy.enable", Boolean.class, false));
    	jt.setProxyHost(env.getProperty("jsoup.proxy.host"));
    	jt.setProxyPort(env.getProperty("jsoup.proxy.port", Integer.class, 80));
    	jt.setProxyUser(env.getProperty("jsoup.proxy.user"));
    	jt.setProxyPassword(env.getProperty("jsoup.proxy.password"));
    	
    	return jt;
    }

}
