package us.ceka.rule;

import java.util.List;

import org.easyrules.annotation.Action;
import org.easyrules.annotation.Condition;
import org.easyrules.annotation.Rule;
import org.easyrules.spring.SpringRule;

import us.ceka.domain.Match;

@SpringRule
@Rule(name = "RepetitiveResultRule", description = "Detect consecutive win/lose")
public class RepetitiveResultRule extends AbstractRule{

	private Match footballMatch;
	private List<Match> homeTeamPastMatches;
	private List<Match> awayTeamPastMatches;
	private int homeUnbeatenCount = 0;
	private int homeUnWinCount = 0;
	private int awayUnbeatenCount = 0;
	private int awayUnWinCount = 0;
	private final static int CONSECUTIVE_LEVEL = 3;


	@Condition
	public boolean evaluate() {

		if(footballMatch.getLeague().isDomesticLeague()) {
			for(Match match : homeTeamPastMatches) {
				if(match.getResult() == Match.MATCH_RESULT.WIN.getCode() || match.getResult() == Match.MATCH_RESULT.DRAW.getCode()) 
					homeUnbeatenCount++;
				else 
					homeUnbeatenCount = 0;
			}

			for(Match match : homeTeamPastMatches) {
				if(match.getResult() == Match.MATCH_RESULT.LOSE.getCode() || match.getResult() == Match.MATCH_RESULT.DRAW.getCode()) 
					homeUnWinCount++;
				else 
					homeUnWinCount = 0;
			}

			for(Match match : awayTeamPastMatches) {
				if(match.getResult() == Match.MATCH_RESULT.LOSE.getCode() || match.getResult() == Match.MATCH_RESULT.DRAW.getCode()) 
					awayUnbeatenCount++;
				else 
					awayUnbeatenCount = 0;
			}

			for(Match match : awayTeamPastMatches) {
				if(match.getResult() == Match.MATCH_RESULT.WIN.getCode() || match.getResult() == Match.MATCH_RESULT.DRAW.getCode()) 
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

	public RepetitiveResultRule setInput(Match footballMatch, List<Match> homeTeamPastMatches, List<Match> awayTeamPastMatches) {
		this.footballMatch = footballMatch;
		this.homeTeamPastMatches = homeTeamPastMatches;
		this.awayTeamPastMatches = awayTeamPastMatches;
		return this;
	}

}
