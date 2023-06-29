package org.apache.syncope.core.spring.security.defaultPasswordGeneratorTest.utilities;

import org.apache.syncope.core.persistence.api.entity.Implementation;
import org.apache.syncope.core.persistence.api.entity.policy.PasswordPolicy;

import java.util.ArrayList;
import java.util.List;

public class MyPasswordPolicy implements PasswordPolicy {

    private Boolean allowNullPassword = false;
    private static final long serialVersionUID = 4L;

    public static final String TABLE = "PasswordPolicy";
    private int historyLength;
    private List<Implementation> rules = new ArrayList<>();


    @Override
    public boolean isAllowNullPassword() {
        return allowNullPassword;
    }

    @Override
    public void setAllowNullPassword(boolean allowNullPassword) {
        this.allowNullPassword = allowNullPassword;
    }

    @Override
    public int getHistoryLength() {
        return historyLength;
    }

    @Override
    public void setHistoryLength(final int historyLength) {
        this.historyLength = historyLength;
    }

    @Override
    public boolean add(final Implementation rule) {
        return rules.contains(rule) || rules.add(rule);
    }

    @Override
    public List<? extends Implementation> getRules() {
        return rules;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public String getKey() {
        return null;
    }
}

