package us.ceka.shop.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.ConfigurableEnvironment;


@Configuration 
@ComponentScan(basePackages = "us.ceka")
@PropertySource(value = {"classpath:config.properties"} )
public class AppConfig {
	private static final Logger log = LoggerFactory.getLogger(AppConfig.class);
	
    @Autowired
    private ConfigurableEnvironment env;
//    @Autowired
//    private ApplicationContext applicationContext;
    
    @Bean
    public MessageSource messageSource() {
    	log.info("initialising resource bundle...");
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return messageSource;
    }
    
    @Bean WebDriver webDriver() {
    	System.setProperty("webdriver.gecko.driver", env.getProperty("geckodriver.path"));
    	return new FirefoxDriver();
    }
}
