package us.ceka.dao.impl;

import org.springframework.stereotype.Repository;

import us.ceka.dao.PlayerDao;
import us.ceka.domain.Player;
import us.ceka.domain.PlayerId;


@Repository("playerDao")
public class PlayerDaoImpl extends BaseDaoImpl<PlayerId, Player> implements PlayerDao {

}
