package us.ceka.dao.impl;

import org.springframework.stereotype.Repository;

import us.ceka.dao.TeamURLDao;
import us.ceka.domain.TeamURL;

@Repository("teamURLDao")
public class TeamURLDaoImpl extends BaseDaoImpl<String, TeamURL> implements TeamURLDao{

}
