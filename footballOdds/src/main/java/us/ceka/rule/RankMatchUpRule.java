package us.ceka.rule;

import org.easyrules.annotation.Action;
import org.easyrules.annotation.Condition;
import org.easyrules.annotation.Rule;
import org.easyrules.spring.SpringRule;

import us.ceka.domain.FootballMatch;
import us.ceka.domain.FootballOdds;
import us.ceka.domain.FootballStanding;
import us.ceka.domain.FootballTier;

@SpringRule
@Rule(name = "RankMatchUp", description = "Detect unexpected rate for strong team vs weak team")
public class RankMatchUpRule extends AbstractRule{


	private FootballMatch footballMatch;
	private FootballOdds footballOdds;
	private FootballStanding homeStanding;
	private FootballStanding awayStanding;
	private FootballTier homeTier;
	private FootballTier awayTier;

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

	public RankMatchUpRule setInput(FootballMatch footballMatch, FootballOdds footballOdds, FootballStanding homeStanding, FootballStanding awayStanding, FootballTier homeTier, FootballTier awayTier) {
		this.footballMatch = footballMatch;
		this.footballOdds = footballOdds;
		this.homeStanding = homeStanding;
		this.awayStanding = awayStanding;
		this.homeTier = homeTier;
		this.awayTier = awayTier;
		return this;
	}




}
