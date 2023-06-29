package org.apache.syncope.core.spring.security.defaultPasswordGeneratorTest.utilities;

import org.apache.syncope.common.lib.policy.DefaultPasswordRuleConf;
import org.apache.syncope.common.lib.policy.PasswordRuleConf;
import org.apache.syncope.core.persistence.api.dao.PasswordRule;
import org.apache.syncope.core.persistence.api.dao.PasswordRuleConfClass;
import org.apache.syncope.core.persistence.api.entity.user.LinkedAccount;
import org.apache.syncope.core.persistence.api.entity.user.User;

/**
 * Enforcing a given password rule to user.
 */

@PasswordRuleConfClass(DefaultPasswordRuleConf.class)
public class MyPasswordRule implements PasswordRule {

    protected DefaultPasswordRuleConf conf;

    @Override
    public PasswordRuleConf getConf() {
        return conf;
    }

    @Override
    public void setConf(final PasswordRuleConf conf) {
        if (conf instanceof DefaultPasswordRuleConf) {
            this.conf = (DefaultPasswordRuleConf) conf;

        } else {
            throw new IllegalArgumentException(
                    DefaultPasswordRuleConf.class.getName() + " expected, got " + conf.getClass().getName());
        }
    }

    @Override
    public void enforce(final User user) {
    }

    @Override
    public void enforce(LinkedAccount account) {
    }
}
