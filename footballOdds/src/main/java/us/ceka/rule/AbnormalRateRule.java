package us.ceka.rule;

import java.math.BigDecimal;
import java.util.Map;

import org.easyrules.annotation.Action;
import org.easyrules.annotation.Condition;
import org.easyrules.annotation.Rule;
import org.easyrules.spring.SpringRule;

import us.ceka.domain.Analytics;
import us.ceka.domain.FootballMatch;
import us.ceka.domain.FootballOdds;


@SpringRule
@Rule(name = "Abnormal Rate", description = "Unexpected rate according to match-up history")
public class AbnormalRateRule extends AbstractRule{

	private FootballMatch footballMatch;
	private FootballOdds footballOdds;
	private Map<String, Object> matchupStat;
	private boolean abnormalHomeRate;
	private boolean abnormalAwayRate;
	
	private int vsWin;
	private int vsDraw;
	private int vsLose;
	
	public AbnormalRateRule() {}

	@Condition
	public boolean evaluate() {
		
		vsWin = matchupStat.get("vsWin") == null ? 0 : Integer.parseInt((String)matchupStat.get("vsWin"));
		vsDraw = matchupStat.get("vsDraw") == null ? 0 : Integer.parseInt((String)matchupStat.get("vsDraw"));
		vsLose = matchupStat.get("vsLose") == null ? 0 : Integer.parseInt((String)matchupStat.get("vsLose"));

		if(vsWin + vsDraw + vsLose > Analytics.MIN_REFERENCE_MATCHES.getValue()) {
			if(footballOdds.getHomeRate().compareTo(new BigDecimal(Analytics.DOUBT_RATE.getValue())) > 0) {			
				if((vsWin + vsDraw) / (double) (vsWin + vsDraw + vsLose) > Analytics.HIGH_WIN_POSSIBILITY.getValue()) {											
					abnormalHomeRate = true;
				}
			}
			if(footballOdds.getAwayRate().compareTo(new BigDecimal(Analytics.DOUBT_RATE.getValue())) > 0) {
				if((vsLose + vsDraw) / (double) (vsWin + vsDraw + vsLose) > Analytics.HIGH_WIN_POSSIBILITY.getValue()) {
					abnormalAwayRate = true;
				}
			}
			return abnormalHomeRate || abnormalAwayRate;
		}

		return false;
	}

	@Action
	public void execute() throws Exception {
		if(abnormalHomeRate) 
			log.info("***[{} {}] Abnormal Home Rate {} ({},{} vs {}) with {}win, {}draw out of {}", 
				footballMatch.getMatchDate(), footballMatch.getMatchDay(), footballOdds.getHomeRate(), 
				footballMatch.getMatchId(), footballMatch.getHomeTeam(), footballMatch.getAwayTeam(), 
				vsWin, vsDraw, vsWin + vsDraw + vsLose);
		
		if(abnormalAwayRate) 
			log.info("***[{} {}] Abnormal Away Rate {} ({},{} vs {}) with {}win, {}draw out of {}", 
					footballMatch.getMatchDate(), footballMatch.getMatchDay(), footballOdds.getAwayRate(), 
					footballMatch.getMatchId(), footballMatch.getHomeTeam(), footballMatch.getAwayTeam(), 
					vsLose, vsDraw, vsWin + vsDraw + vsLose);
					
	}

	public AbnormalRateRule setInput(FootballMatch footballMatch, FootballOdds footballOdds, Map<String, Object> matchupStat) {
		this.footballMatch = footballMatch;
		this.footballOdds = footballOdds;
		this.matchupStat = matchupStat;
		return this;
	}

}
