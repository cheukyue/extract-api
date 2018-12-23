package us.ceka.odds;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import us.ceka.odds.config.AppConfig;
import us.ceka.service.OddsAnalyseService;
import us.ceka.service.OddsRecordService;
import us.ceka.service.SquadRecordService;
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
		OddsRecordService recordService = ctx.getBean(OddsRecordService.class);
		MaintenanceService mainService = ctx.getBean(MaintenanceService.class);
		OddsAnalyseService analyseService = ctx.getBean(OddsAnalyseService.class);
		SquadRecordService squadService = ctx.getBean(SquadRecordService.class);
		
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
				//recordService.executeUpdateTeamMatches();
				recordService.executeUpdateTeamMatchesWithOdds();
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
			case "REFRESH_PLAYER_TABLE":
				mainService.executeRefreshPlayerTable();
				break;
			case "RECORD_SQUAD":
				squadService.executeRecordMajorSquad();
				break;
			case "RECORD_HISTORIC_SQUAD":
				squadService.recordPremierLeagueSquadByDateBefore();
				break;
			}
		}	

		ctx.close();

		log.info("Application end");
	}
}
