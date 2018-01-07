package us.ceka.shop;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import us.ceka.service.ShoppingNotification;
import us.ceka.shop.config.AppConfig;


public class App {
	private static final Logger log = LoggerFactory.getLogger(App.class);

	public static void main( String[] args )
	{
		log.info("Application start");

		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AppConfig.class);
		ctx.refresh();		
		
		log.info("Shop notification version {}", ctx.getEnvironment().getProperty("app.version"));
		ShoppingNotification shoppingNotification = ctx.getBean(ShoppingNotification.class);
		shoppingNotification.scanPage();
		
		ctx.close();

		log.info("Application end");
	}
}
