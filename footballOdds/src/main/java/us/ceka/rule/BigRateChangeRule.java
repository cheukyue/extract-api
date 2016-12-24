package us.ceka.rule;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.easyrules.annotation.Action;
import org.easyrules.annotation.Condition;
import org.easyrules.annotation.Rule;
import org.easyrules.spring.SpringRule;
import org.springframework.context.annotation.Scope;

import us.ceka.domain.Analytics;
import us.ceka.domain.FootballMatch;
import us.ceka.domain.FootballOdds;

@SpringRule
@Rule(name = "BigRateChange", description = "Detect big rate Change")
public class BigRateChangeRule extends AbstractRule{
	
	private FootballMatch footballMatch;
	private FootballOdds initialOdds;
	private FootballOdds latestOdds;
	
	boolean handicapLineChange = false;
	boolean bigRateChange = false;
	
	@Condition
	public boolean evaluate() {
		if(!StringUtils.equals(latestOdds.getHandicapLine(), initialOdds.getHandicapLine())) { 
			this.handicapLineChange = true;
		}

		if(latestOdds.getAwayRate().subtract(initialOdds.getAwayRate()).abs()
				.compareTo(new BigDecimal(Analytics.BIG_RATE_CHANGE.getValue()) ) > 0) {
			this.bigRateChange = true;
		}
		return this.handicapLineChange || this.bigRateChange;
	}

	@Action
	public void execute() throws Exception {
		
		if(this.handicapLineChange) {
			log.info("***handicap line change from ({}) {} to ({}) {}", initialOdds.getHandicapLine(), initialOdds.getHandicapHomeRate(), 
					latestOdds.getHandicapLine(), latestOdds.getHandicapAwayRate());
			
		}
		
		if(this.bigRateChange) {
		log.info("***[{} {}] Away Rate ({},{} vs {}) has {} {} [H:{} D:{} A:{}]",  
				footballMatch.getMatchDate(), footballMatch.getMatchDay(), footballMatch.getMatchId(), footballMatch.getHomeTeam(), footballMatch.getAwayTeam(),
				latestOdds.getAwayRate().compareTo(initialOdds.getAwayRate()) > 0 ? "increased" : "decreased",
				latestOdds.getAwayRate().subtract(initialOdds.getAwayRate()).abs(),
				latestOdds.getHomeRate(), latestOdds.getDrawRate(), latestOdds.getAwayRate() 
				);
		}
	}
	
	public BigRateChangeRule setIntput(FootballMatch footballMatch, FootballOdds initialOdds, FootballOdds latestOdds) {
		this.footballMatch = footballMatch;
		this.initialOdds = initialOdds;
		this.latestOdds = latestOdds;
		return this;
	}

}
