package net.robyf.ms.autoconfigure.security;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;
import org.springframework.security.core.context.SecurityContext;

public class SecurityContextHystrixRequestVariable {

    private static final HystrixRequestVariableDefault<SecurityContext> securityContextVariable = new HystrixRequestVariableDefault<>();

    private SecurityContextHystrixRequestVariable() {}

    public static HystrixRequestVariableDefault<SecurityContext> getInstance() {
        return securityContextVariable;
    }

}
