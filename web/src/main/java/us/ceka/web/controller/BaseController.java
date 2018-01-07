package us.ceka.web.controller;

import us.ceka.service.OddsAnalyseService;
import us.ceka.service.impl.OddsAnalyseServiceImpl;

public class BaseController {
	
	public String toString() {
		OddsAnalyseService x = new OddsAnalyseServiceImpl();
		x.toString();
		return null;
	}

}
