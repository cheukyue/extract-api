package us.ceka.rule;

import org.easyrules.annotation.Action;
import org.easyrules.annotation.Condition;
import org.easyrules.annotation.Rule;
import org.easyrules.spring.SpringRule;

import us.ceka.domain.Match;
import us.ceka.domain.Odds;
import us.ceka.domain.Standing;
import us.ceka.domain.Tier;

@SpringRule
@Rule(name = "RankMatchUp", description = "Detect unexpected rate for strong team vs weak team")
public class RankMatchUpRule extends AbstractRule{


	private Match footballMatch;
	private Odds footballOdds;
	private Standing homeStanding;
	private Standing awayStanding;
	private Tier homeTier;
	private Tier awayTier;

	@Condition
	public boolean evaluate() {
		if(footballMatch.getLeague().isDomesticLeague()) {
			if(footballOdds.getHomeRate().compareTo(footballOdds.getAwayRate()) > 0) {
				if(homeStanding.getRank() < awayStanding.getRank() ) return true;
			}
			if(footballOdds.getHomeRate().compareTo(footballOdds.getAwayRate()) < 0) {
				if(homeStanding.getRank() > awayStanding.getRank()) return true;
			}
		}
		return false;
	}

	@Action
	public void execute() throws Exception {
		log.info("*** Unexpected rate {}, {} (Rank:{}, Tier:{}), {} (Rank:{}, Tier:{})", footballMatch.getMatchId(), footballMatch.getHomeTeam(), homeStanding.getRank(), homeTier.getTier(),
				footballMatch.getAwayTeam(), awayStanding.getRank(), awayTier.getTier());
	}

	public RankMatchUpRule setInput(Match footballMatch, Odds footballOdds, Standing homeStanding, Standing awayStanding, Tier homeTier, Tier awayTier) {
		this.footballMatch = footballMatch;
		this.footballOdds = footballOdds;
		this.homeStanding = homeStanding;
		this.awayStanding = awayStanding;
		this.homeTier = homeTier;
		this.awayTier = awayTier;
		return this;
	}




}
