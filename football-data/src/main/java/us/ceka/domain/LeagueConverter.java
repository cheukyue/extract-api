package us.ceka.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class LeagueConverter implements AttributeConverter<League, String> {

	@Override
	public String convertToDatabaseColumn(League league) {
		return league.getId();
	}

	@Override
	public League convertToEntityAttribute(String leagueId) {
		return League.getById(leagueId);
	}

}
