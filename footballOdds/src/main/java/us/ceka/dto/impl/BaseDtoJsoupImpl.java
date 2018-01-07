package us.ceka.dto.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import us.ceka.dto.AbstractDto;
import us.ceka.util.JsoupTemplate;


public class FootballDtoJsoupImpl<T> implements AbstractDto<T>{
	protected final Logger log = LoggerFactory.getLogger(getClass().getName());
	
	@Autowired
	private JsoupTemplate jsoupTemplate;

	protected JsoupTemplate getJsoupTemplate() {
		return jsoupTemplate;
	}
	
}
