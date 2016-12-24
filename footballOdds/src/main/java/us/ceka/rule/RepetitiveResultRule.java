package us.ceka.rule;

import java.util.List;

import org.easyrules.annotation.Action;
import org.easyrules.annotation.Condition;
import org.easyrules.annotation.Rule;
import org.easyrules.spring.SpringRule;

import us.ceka.domain.FootballMatch;

@SpringRule
@Rule(name = "RepetitiveResultRule", description = "Detect consecutive win/lose")
public class RepetitiveResultRule extends AbstractRule{

	private FootballMatch footballMatch;
	private List<FootballMatch> homeTeamPastMatches;
	private List<FootballMatch> awayTeamPastMatches;
	private int homeUnbeatenCount = 0;
	private int homeUnWinCount = 0;
	private int awayUnbeatenCount = 0;
	private int awayUnWinCount = 0;
	private final static int CONSECUTIVE_LEVEL = 3;


	@Condition
	public boolean evaluate() {

		if(footballMatch.getLeague().isDomesticLeague()) {
			for(FootballMatch match : homeTeamPastMatches) {
				if(match.getResult() == FootballMatch.MATCH_RESULT.WIN.getCode() || match.getResult() == FootballMatch.MATCH_RESULT.DRAW.getCode()) 
					homeUnbeatenCount++;
				else 
					homeUnbeatenCount = 0;
			}

			for(FootballMatch match : homeTeamPastMatches) {
				if(match.getResult() == FootballMatch.MATCH_RESULT.LOSE.getCode() || match.getResult() == FootballMatch.MATCH_RESULT.DRAW.getCode()) 
					homeUnWinCount++;
				else 
					homeUnWinCount = 0;
			}

			for(FootballMatch match : awayTeamPastMatches) {
				if(match.getResult() == FootballMatch.MATCH_RESULT.LOSE.getCode() || match.getResult() == FootballMatch.MATCH_RESULT.DRAW.getCode()) 
					awayUnbeatenCount++;
				else 
					awayUnbeatenCount = 0;
			}

			for(FootballMatch match : awayTeamPastMatches) {
				if(match.getResult() == FootballMatch.MATCH_RESULT.WIN.getCode() || match.getResult() == FootballMatch.MATCH_RESULT.DRAW.getCode()) 
					awayUnWinCount++;
				else 
					awayUnWinCount = 0;
			}
		}

		return homeUnbeatenCount > CONSECUTIVE_LEVEL || homeUnWinCount > CONSECUTIVE_LEVEL || awayUnbeatenCount > CONSECUTIVE_LEVEL || awayUnWinCount > CONSECUTIVE_LEVEL;
	}

	@Action
	public void execute() throws Exception {
		if(homeUnbeatenCount > CONSECUTIVE_LEVEL) log.info("*** {} has {} matchees unbeaten", homeTeamPastMatches.get(0).getHomeTeam(), homeUnbeatenCount);
		if(homeUnWinCount > CONSECUTIVE_LEVEL) log.info("*** {} has {} matchees unWin", homeTeamPastMatches.get(0).getHomeTeam(), homeUnWinCount);
		if(awayUnbeatenCount > CONSECUTIVE_LEVEL) log.info("*** {} has {} matchees unbeaten", awayTeamPastMatches.get(0).getAwayTeam(), awayUnbeatenCount);
		if(awayUnWinCount > CONSECUTIVE_LEVEL) log.info("*** {} has {} matchees unWin", awayTeamPastMatches.get(0).getAwayTeam(), awayUnWinCount);
	}

	public RepetitiveResultRule setInput(FootballMatch footballMatch, List<FootballMatch> homeTeamPastMatches, List<FootballMatch> awayTeamPastMatches) {
		this.footballMatch = footballMatch;
		this.homeTeamPastMatches = homeTeamPastMatches;
		this.awayTeamPastMatches = awayTeamPastMatches;
		return this;
	}

}
