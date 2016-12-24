package us.ceka.odds;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import us.ceka.odds.config.AppConfig;
import us.ceka.service.FootballOddsAnalyseService;
import us.ceka.service.FootballOddsRecordService;
import us.ceka.service.MaintenanceService;

public class App 
{
	private static final Logger log = LoggerFactory.getLogger(App.class);
	
	public static void main( String[] args )
	{
		log.info("Application start");
		
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AppConfig.class);
		ctx.refresh();
		log.info(ctx.getMessage("app.name", null, Locale.getDefault()));
		FootballOddsRecordService recordService = ctx.getBean(FootballOddsRecordService.class);
		MaintenanceService mainService = ctx.getBean(MaintenanceService.class);
		FootballOddsAnalyseService analyseService = ctx.getBean(FootballOddsAnalyseService.class);
		
		if(args.length > 0) {
			switch (args[0]) {
			case "ANALYSE_ODDS":
				analyseService.executeOddsAnalysis();
				break;
			case "RECORD_ODDS":
				recordService.executeRecordOdds();
				break;
			case "UPDATE_MATCH_RESULT":
				recordService.executeUpdateMatchResult();
				break;
			case "UPDATE_MATCH_RESULT_ARCHIVE":
				recordService.executeUpdateTeamMatches();
				break;
			case "REFRESH_SEASON_TABLE":
				mainService.executeRefreshFootballLeague();
				break;
			case "REFRESH_TEAM_TABLE":
				mainService.executeRefreshTeamTable();
				break;
			case "REFRESH_STANDING":
				mainService.executeRefreshStanding();
				break;
			}
		}	

		ctx.close();

		log.info("Application end");
	}
}
