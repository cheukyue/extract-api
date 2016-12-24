package us.ceka.rule;

import org.easyrules.core.BasicRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractRule {
	protected final Logger log = LoggerFactory.getLogger(getClass().getName());
}
