package us.ceka.rule;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.easyrules.annotation.Action;
import org.easyrules.annotation.Condition;
import org.easyrules.annotation.Rule;
import org.easyrules.spring.SpringRule;

import us.ceka.domain.Analytics;
import us.ceka.domain.Match;
import us.ceka.domain.Odds;

@SpringRule
@Rule(name = "BigRateChange", description = "Detect big rate Change")
public class BigRateChangeRule extends AbstractRule{
	
	//private FootballMatch footballMatch;
	private Odds initialOdds;
	private Odds latestOdds;
	
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
		log.info("*** Away Rate has {} {} [H:{} D:{} A:{}]",  
				latestOdds.getAwayRate().compareTo(initialOdds.getAwayRate()) > 0 ? "increased" : "decreased",
				latestOdds.getAwayRate().subtract(initialOdds.getAwayRate()).abs(),
				latestOdds.getHomeRate(), latestOdds.getDrawRate(), latestOdds.getAwayRate() 
				);
		}
	}
	
	public BigRateChangeRule setIntput(Match footballMatch, Odds initialOdds, Odds latestOdds) {
		//this.footballMatch = footballMatch;
		this.initialOdds = initialOdds;
		this.latestOdds = latestOdds;
		return this;
	}

}
