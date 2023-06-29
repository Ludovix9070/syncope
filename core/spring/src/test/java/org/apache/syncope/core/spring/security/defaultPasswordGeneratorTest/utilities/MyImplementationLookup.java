package org.apache.syncope.core.spring.security.defaultPasswordGeneratorTest.utilities;

import org.apache.syncope.common.lib.policy.PasswordRuleConf;
import org.apache.syncope.common.lib.policy.PullCorrelationRuleConf;
import org.apache.syncope.common.lib.policy.PushCorrelationRuleConf;
import org.apache.syncope.common.lib.report.ReportletConf;
import org.apache.syncope.core.persistence.api.ImplementationLookup;
import org.apache.syncope.core.persistence.api.dao.AccountRule;
import org.apache.syncope.common.lib.policy.AccountRuleConf;
import org.apache.syncope.core.persistence.api.dao.PullCorrelationRule;
import org.apache.syncope.core.persistence.api.dao.PushCorrelationRule;
import org.apache.syncope.core.persistence.api.dao.Reportlet;
import org.apache.syncope.core.persistence.api.dao.PasswordRule;

import java.util.Set;

public class MyImplementationLookup implements ImplementationLookup {
    @Override
    public Set<String> getClassNames(String type) {return Set.of();
    }

    @Override
    public Set<Class<?>> getJWTSSOProviderClasses() {return Set.of();
    }

    @Override
    public Class<? extends Reportlet> getReportletClass(Class<? extends ReportletConf> reportConfClass) {
        return null;
    }

    @Override
    public Class<? extends AccountRule> getAccountRuleClass(Class<? extends AccountRuleConf> accountRuleConfClass) {
        return null;
    }

    @Override
    public Class<? extends PasswordRule> getPasswordRuleClass(Class<? extends PasswordRuleConf> passwordRuleConfClass) {
        return MyPasswordRule.class;
    }

    @Override
    public Class<? extends PullCorrelationRule> getPullCorrelationRuleClass(Class<? extends PullCorrelationRuleConf> pullCorrelationRuleConfClass) {
        return null;
    }

    @Override
    public Class<? extends PushCorrelationRule> getPushCorrelationRuleClass(Class<? extends PushCorrelationRuleConf> pushCorrelationRuleConfClass) {
        return null;
    }

    @Override
    public Set<Class<?>> getAuditAppenderClasses() {
        return Set.of();
    }

    @Override
    public int getOrder() {return 0;
    }
}
