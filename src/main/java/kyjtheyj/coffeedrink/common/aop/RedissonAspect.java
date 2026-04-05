package kyjtheyj.coffeedrink.common.aop;

import kyjtheyj.coffeedrink.common.annotation.DistributedLock;
import kyjtheyj.coffeedrink.common.exception.ServiceErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import static kyjtheyj.coffeedrink.common.exception.domain.CommonExceptionEnum.ERR_GET_REDIS_LOCK_FAIL;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RedissonAspect {
    private final RedissonClient redissonClient;
    private final AopInTransaction aopInTransaction;

    private final ExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(distributedLock)")
    public Object lock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNameArr = signature.getParameterNames();
        Object[] argsArr = joinPoint.getArgs();

        StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext();
        for (int index = 0; index < paramNameArr.length; index++) {
            standardEvaluationContext.setVariable(paramNameArr[index], argsArr[index]);
        }

        String key = "dLock:" + parser.parseExpression(distributedLock.key()).getValue(standardEvaluationContext, String.class);
        RLock rLock = redissonClient.getLock(key);

        boolean isLock = rLock.tryLock(
                distributedLock.waitTime()
                , distributedLock.leaseTime()
                , distributedLock.timeUnit()
        );

        if (!isLock) {
            throw new ServiceErrorException(ERR_GET_REDIS_LOCK_FAIL);
        }

        try {
            return aopInTransaction.proceed(joinPoint);
        } finally {
            if(rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
    }
}
