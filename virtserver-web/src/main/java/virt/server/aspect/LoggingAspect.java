package virt.server.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import virt.server.dto.Identifier;

import java.util.Arrays;
import java.util.List;

/**
 * Aspect to log after method returns all necessary data
 *
 * @author Lukas Oscipovsky
 */
@Aspect
@Slf4j
@Component
public class LoggingAspect {

    @AfterReturning(value = "@annotation(virt.server.aspect.Logging)", returning = "retVal")
    public void logAfterReturning(final JoinPoint joinPoint, final Object retVal) {
        if (retVal instanceof Identifier identifier) {
            log.debug("Method called successfully with name: [{}.{}] and returning id [{}]", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), identifier.getId());
        } else if (retVal instanceof List list) {
            log.debug("Method called successfully with name: [{}.{}] and returning number of elements [{}]", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), list.size());
        } else {
            this.handleGetId(joinPoint);
        }
    }

    private void handleGetId(final JoinPoint joinPoint) {
        final Long l = (Long) Arrays.stream(joinPoint.getArgs()).filter(o -> o instanceof Long).findFirst().orElseThrow(() -> new IllegalArgumentException("Not Existing Long id argument"));

        log.debug("Method called successfully with name: [{}.{}] and id : [{}]", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), l);
    }

}
