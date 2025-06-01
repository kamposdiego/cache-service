package bhsg.com.cache.exceptions;

import io.grpc.Status;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;

import static bhsg.com.cache.logging.LogContextUtils.trace;
import static bhsg.com.cache.logging.LoggingConstants.Symbols.OUT;
import static bhsg.com.cache.logging.LoggingConstants.Tags.SERVICE;

@Slf4j
@GrpcAdvice
public class GrpcExceptionAdvice {

    @GrpcExceptionHandler
    public Status handleInvalidArgument(final IllegalArgumentException e) {
        log.error("{} {} - {}", OUT, SERVICE, trace());

        return Status.INVALID_ARGUMENT.withDescription("Invalid Argument").withCause(e);
    }

    @GrpcExceptionHandler(RedisConnectionFailureException.class)
    public Status handleRedisConnectionFailureException(final RedisConnectionFailureException ex) {
        log.error("{} {} - {}", OUT, SERVICE, trace());

        return Status.INTERNAL
                .withDescription("Internal Server Error")
                .augmentDescription("Cannot connect to Redis. Please try again later or contact support with the trace ID.")
                .withCause(ex);
    }

    @GrpcExceptionHandler({RedisSerializationException.class, RedisSystemException.class})
    public Status handleRedisInternalExceptions(final RuntimeException ex) {
        log.error("{} {} - {}", OUT, SERVICE, trace());

        return Status.UNAVAILABLE
                .withDescription("Service Unavailable")
                .augmentDescription("Redis failed to process the request. Please try again later or contact support with the trace ID.")
                .withCause(ex);
    }

}
