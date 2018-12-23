package us.ceka.dao.impl;

import org.springframework.stereotype.Repository;

import us.ceka.dao.TeamNameDao;
import us.ceka.domain.TeamName;

@Repository("teamNameDao")
public class TeamNameDaoImpl extends BaseDaoImpl<String, TeamName> implements TeamNameDao {

}
