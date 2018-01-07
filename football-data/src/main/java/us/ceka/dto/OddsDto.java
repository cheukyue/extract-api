package us.ceka.dto;


import us.ceka.domain.Odds;

public interface OddsDto extends AbstractDto<Odds> {
	public Odds getLatestById(String matchId);
	public Odds getArchiveById(String matchId);
}
