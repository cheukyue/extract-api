package us.ceka.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsoupTemplateTest {

	protected final Logger log = LoggerFactory.getLogger(JsoupTemplateTest.class);

	private Properties props = new Properties();

	@Before
	public void setup() {
		try {
			props.load(ClassLoader.getSystemResourceAsStream("config.properties"));
		}
		catch (IOException e) {
			fail("Cannot read config.properties during setup");
		}
	}

	@Test
	public void getDocumnetByAliasTest() {
		log.info("executing getDocumnetByAliasTest...");
		JsoupTemplate jt = new JsoupTemplate();
		
		jt.setTimeout(NumberUtils.toInt(props.getProperty("jsoup.timeout"))); 
		
		jt.setProxyEnable(BooleanUtils.toBoolean(props.getProperty("jsoup.proxy.enable")));
    	jt.setUserAgent(StringUtils.defaultString(props.getProperty("jsoup.userAgent"), "-")); 
    	jt.setProxyHost(props.getProperty("jsoup.proxy.host"));
    	jt.setProxyPort(NumberUtils.toInt(props.getProperty("jsoup.proxy.port")));
    	jt.setProxyUser(props.getProperty("jsoup.proxy.user"));
    	jt.setProxyPassword(props.getProperty("jsoup.proxy.password"));

		Document doc = jt.getDocument("http://google.com");
		log.info("Returned baseUri: {}", doc.baseUri());
		assertNotNull(doc.body());
	}
}
