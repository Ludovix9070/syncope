package org.apache.syncope.core.spring.security.defaultPasswordGeneratorTest;

import junit.framework.TestCase;
import org.apache.syncope.common.lib.policy.DefaultPasswordRuleConf;
import org.apache.syncope.core.persistence.api.ImplementationLookup;
import org.apache.syncope.core.persistence.api.entity.policy.PasswordPolicy;
import org.apache.syncope.core.provisioning.api.serialization.POJOHelper;
import org.apache.syncope.core.spring.ApplicationContextProvider;
import org.apache.syncope.core.spring.security.DefaultPasswordGenerator;
import org.apache.syncope.core.spring.security.SecurityProperties;
import org.apache.syncope.core.spring.security.defaultPasswordGeneratorTest.utilities.MyExternalResource;
import org.apache.syncope.core.spring.security.defaultPasswordGeneratorTest.utilities.MyImplementation;
import org.apache.syncope.core.spring.security.defaultPasswordGeneratorTest.utilities.MyImplementationLookup;
import org.apache.syncope.core.spring.security.defaultPasswordGeneratorTest.utilities.MyPasswordPolicy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.*;

import static org.mockito.Mockito.when;

@RunWith(value = Parameterized.class)
public class DefaultPasswordGeneratorGTest extends TestCase {

    private List<PasswordPolicy> policies;
    private DefaultPasswordGenerator defaultPasswordgenerator;
    private Types type;
    private Object expected;
    private static final int minLength = 8;
    private static final int maxLength = 64;

    @Mock
    ConfigurableApplicationContext myContext;

    public DefaultPasswordGeneratorGTest (List<PasswordPolicy> policies, Types type, Object expected){
        configure(policies, type, expected);
    }

    private void configure(List<PasswordPolicy> policies, Types type, Object expected){
        this.policies = policies;
        this.expected = expected;
        this.type = type;
    }

    @Parameterized.Parameters
    public static Collection parameters() throws Exception {

        return Arrays.asList(new Object[][] {

                //policies, type, expected
                //tests based on monodimensional analysis
                {createPolicies(Types.NULL), Types.NULL, NullPointerException.class},
                {createPolicies(Types.VALID), Types.VALID, null},
                {createPolicies(Types.EMPTY), Types.EMPTY, null},
                {createPolicies(Types.INVALID), Types.INVALID, IllegalArgumentException.class},

                //tests after jacoco to have better coverage
                {createPolicies(Types.MIN_LENGTH), Types.MIN_LENGTH, null},
                {createPolicies(Types.MAX_LENGTH), Types.MAX_LENGTH, null},
                {createPolicies(Types.UPPERCASE), Types.UPPERCASE, null},
                {createPolicies(Types.LOWERCASE), Types.LOWERCASE, null},
                {createPolicies(Types.DIGIT), Types.DIGIT, null},
                {createPolicies(Types.SPECIAL), Types.SPECIAL, IllegalArgumentException.class}, //lista vuota
                {createPolicies(Types.SPECIAL_CHARS), Types.SPECIAL_CHARS, null},
                {createPolicies(Types.ILLEGAL_CHARS), Types.ILLEGAL_CHARS, null}, //to check
                //{createPolicies(Types.REPEAT_SAME), Types.REPEAT_SAME, null},
                {createPolicies(Types.WORD_NOT_PERMITTED), Types.WORD_NOT_PERMITTED, null},
                {createPolicies(Types.MIN_MAX), Types.MIN_MAX, null},
                {createPolicies(Types.NOT_UPPERCASE), Types.NOT_UPPERCASE, null},
                {createConflict(),null, NullPointerException.class},

                //tests for external resources
                {createPolicies(Types.MIN_LENGTH),Types.EXTERNAL, null},
                {null,Types.EXTERNAL_NULL, null},

                //tests to have better branch coverage
                {createPolicies(Types.MAX_MIN_IF_ZERO), Types.MAX_MIN_IF_ZERO, null},
                {createPolicies(Types.MAX_BRANCH), Types.MAX_BRANCH, null},
                {createLambda(), Types.LAMBDA, null},



        });
    }



    @Before
    public void setUp(){
        this.defaultPasswordgenerator = new DefaultPasswordGenerator();
        SecurityProperties myProperties = new SecurityProperties();
        ImplementationLookup implementationLookup = new MyImplementationLookup();
        myContext = Mockito.mock(ConfigurableApplicationContext.class);
        when(myContext.getBean(ImplementationLookup.class)).thenReturn(implementationLookup);
        when(myContext.getBean(SecurityProperties.class)).thenReturn(myProperties);
        ApplicationContextProvider.setBeanFactory(new DefaultListableBeanFactory());
        ApplicationContextProvider.setApplicationContext(myContext);
    }

    public static List<PasswordPolicy> createPolicy(DefaultPasswordRuleConf config){
        MyImplementation rule = new MyImplementation();
        rule.setBody(POJOHelper.serialize(config));
        MyPasswordPolicy myPassPolicy = new MyPasswordPolicy();
        myPassPolicy.add(rule);
        List<PasswordPolicy> policy = List.of(myPassPolicy);
        return policy;
    }

    public static List<PasswordPolicy> createConflict(){
        DefaultPasswordRuleConf config = new DefaultPasswordRuleConf();
        config.setMaxLength(12);
        MyImplementation rule = new MyImplementation();
        rule.setBody(POJOHelper.serialize(config));
        MyPasswordPolicy myPassPolicy = new MyPasswordPolicy();
        myPassPolicy.add(rule);


        DefaultPasswordRuleConf config2 = new DefaultPasswordRuleConf();
        config2.setUppercase(15);
        MyImplementation rule2 = new MyImplementation();
        rule.setBody(POJOHelper.serialize(config));
        MyPasswordPolicy myPassPolicy2 = new MyPasswordPolicy();
        myPassPolicy2.add(rule2);

        List<PasswordPolicy> policiesConflict = List.of(myPassPolicy,myPassPolicy2);
        return policiesConflict;

    }

    public static List<PasswordPolicy> createLambda(){
        DefaultPasswordRuleConf config = new DefaultPasswordRuleConf();
        config.setSpecial(1);
        config.getSpecialChars().add('$');
        config.getSpecialChars().add('%');
        config.getSpecialChars().add('£');
        config.getSpecialChars().add('@');
        config.getIllegalChars().add('x');
        config.getIllegalChars().add('y');
        config.getIllegalChars().add('z');
        config.getWordsNotPermitted().add("forbidden");
        MyImplementation rule = new MyImplementation();
        rule.setBody(POJOHelper.serialize(config));
        MyPasswordPolicy myPassPolicy = new MyPasswordPolicy();
        myPassPolicy.add(rule);


        DefaultPasswordRuleConf config2 = new DefaultPasswordRuleConf();
        config2.setSpecial(1);
        config2.getSpecialChars().add('$');
        config2.getSpecialChars().add('%');
        config2.getSpecialChars().add('£');
        config2.getSpecialChars().add('@');
        config2.getIllegalChars().add('x');
        config2.getIllegalChars().add('y');
        config2.getIllegalChars().add('z');
        config2.getWordsNotPermitted().add("forbidden");
        config2.setUppercase(15);
        MyImplementation rule2 = new MyImplementation();
        rule.setBody(POJOHelper.serialize(config));
        MyPasswordPolicy myPassPolicy2 = new MyPasswordPolicy();
        myPassPolicy2.add(rule2);

        List<PasswordPolicy> policiesLambda = List.of(myPassPolicy,myPassPolicy2);
        return policiesLambda;

    }

    public static List<PasswordPolicy> createPolicies(Types policiesType){
        DefaultPasswordRuleConf config = new DefaultPasswordRuleConf();
        List<PasswordPolicy> policy;
        switch(policiesType){
            case ALPHA:
                config.setAlphabetical(1);
                policy = createPolicy(config);
                break;

            case VALID:
                config.setAlphabetical(2);
                policy = createPolicy(config);
                break;

            case INVALID:
                config.setRepeatSame(1); //lista vuota va in errore
                policy = createPolicy(config);
                break;

            case EMPTY:
                policy = new ArrayList<PasswordPolicy>();
                break;

            case EXTERNAL:
            case MIN_LENGTH:
                config.setMinLength(minLength+1);
                policy = createPolicy(config);
                break;

            case MAX_LENGTH:
                config.setMaxLength(maxLength-1);
                policy = createPolicy(config);
                break;

            case MAX_BRANCH:
                config.setMaxLength(maxLength+1);
                policy = createPolicy(config);
                break;

            case CONFLICT_MAX:
                config.setMaxLength(12);
                policy = createPolicy(config);
                break;

            case CONFLICT_UPPER:
                config.setUppercase(15);
                policy = createPolicy(config);
                break;

            case UPPERCASE:
                config.setUppercase(1);
                policy = createPolicy(config);
                break;

            case NOT_UPPERCASE:
                config.setUppercase(0);
                policy = createPolicy(config);
                break;

            case LOWERCASE:
                config.setLowercase(1);
                policy = createPolicy(config);
                break;

            case DIGIT:
                config.setDigit(1);
                policy = createPolicy(config);
                break;

            case SPECIAL:
                config.setSpecial(1);
                policy = createPolicy(config);
                break;

            case SPECIAL_CHARS:
                config.setSpecial(1);
                config.getSpecialChars().add('$');
                config.getSpecialChars().add('%');
                config.getSpecialChars().add('£');
                config.getSpecialChars().add('@');
                policy = createPolicy(config);
                break;

            case ILLEGAL_CHARS:
                config.getIllegalChars().add('x');
                config.getIllegalChars().add('y');
                config.getIllegalChars().add('z');
                policy = createPolicy(config);
                break;

            case REPEAT_SAME:
                config.setAlphabetical(1);
                config.setRepeatSame(1);
                policy = createPolicy(config);
                break;

            case WORD_NOT_PERMITTED:
                config.getWordsNotPermitted().add("forbidden");
                policy = createPolicy(config);
                break;

            case MIN_MAX:
                config.setMinLength(5);
                config.setMaxLength(4);
                policy = createPolicy(config);
                break;

            case MAX_MIN_IF_ZERO:
                config.setMaxLength(minLength-1);
                config.setUsernameAllowed(true);
                policy = createPolicy(config);
                break;

            case EXTERNAL_NULL:
            case NULL:
            default:
                policy = null;
        }

        return policy;

    }

    private boolean check(String password, Types type){
        boolean result;
        switch(type){
            case ALPHA:
            case REPEAT_SAME:
            case VALID:
                result = password.matches(".*[a-zA-Z].*");
                break;

            case MAX_MIN_IF_ZERO:
            case EXTERNAL_NULL:
            case EMPTY:
                result = true;
                break;

            case EXTERNAL:
            case MIN_LENGTH:
                if(password.length() <= minLength){
                    result = false;
                }else{
                    result = true;
                }
                break;

            case MAX_LENGTH:
                if(password.length() >= maxLength){
                    result = false;
                }else{
                    result = true;
                }
                break;

            case MAX_BRANCH:
                if(password.length() > maxLength){
                    result = false;
                }else{
                    result = true;
                }
                break;

            case UPPERCASE:
                result = password.matches(".*[A-Z]+.*");
                break;

            case NOT_UPPERCASE:
                result = password.matches(".*[^A-Z].*");
                break;

            case LOWERCASE:
                result = password.matches(".*[a-z]+.*");
                break;

            case DIGIT:
                result = password.matches(".*\\d+.*");
                break;

            case SPECIAL_CHARS:
                result = password.matches(".*[$%£@].*");
                break;

            case ILLEGAL_CHARS:
                result = password.matches(".*[^x-z].*");
                break;

            case WORD_NOT_PERMITTED:
                result = !password.contains("forbidden");
                break;

            case LAMBDA:
                result = (!password.contains("forbidden") && password.matches(".*[$%£@].*") && password.matches(".*[^x-z].*"));
                break;

            case MIN_MAX:
            case INVALID:
            default:
                result = true;
        }

        return result;
    }

    @Test
    public void testGenerate(){
        String generatedPassword;
        try {
            DefaultPasswordGenerator defaultPasswordGenerator = new DefaultPasswordGenerator();
            if(this.type != Types.EXTERNAL && this.type != Types.EXTERNAL_NULL){
                generatedPassword = defaultPasswordGenerator.generate(this.policies);
            }else{
                MyExternalResource myExternalResource = new MyExternalResource();
                if(type == Types.EXTERNAL){
                    myExternalResource.setPasswordPolicy(policies.get(0));
                }else{
                    myExternalResource.setPasswordPolicy(null);
                }

                generatedPassword = defaultPasswordGenerator.generate(myExternalResource);
            }

            //System.out.println("password "+generatedPassword+"\n");
            Assert.assertTrue(check(generatedPassword, type));

        } catch (Exception e) {
            Assert.assertEquals(expected,e.getClass());
        }

    }

    public enum Types{
        VALID, INVALID, ALPHA, NULL, EMPTY, MIN_LENGTH, MAX_LENGTH, UPPERCASE, LOWERCASE, DIGIT, SPECIAL, SPECIAL_CHARS,
        ILLEGAL_CHARS, REPEAT_SAME, WORD_NOT_PERMITTED, CONFLICT_MAX, CONFLICT_UPPER, MIN_MAX, NOT_UPPERCASE, EXTERNAL,
        EXTERNAL_NULL, MAX_MIN_IF_ZERO, MAX_BRANCH, LAMBDA
    }
}
