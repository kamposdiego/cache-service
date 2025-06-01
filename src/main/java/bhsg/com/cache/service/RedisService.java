package bhsg.com.cache.service;

import bhsg.com.cache.IdempotentReply;
import bhsg.com.cache.entity.PostRequestRedisHash;
import bhsg.com.cache.exceptions.RedisErrorDuringSavingException;
import bhsg.com.cache.exceptions.RedisSerializationException;
import bhsg.com.cache.exceptions.RedisServiceUnavailableException;
import bhsg.com.cache.logging.LoggingConstants;
import bhsg.com.cache.repository.PostRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Service;

import static bhsg.com.cache.logging.LogContextUtils.trace;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisService {

    private static final String FIND_OPERATION = "find";
    private static final String PERSIST_OPERATION = "persist";
    private static final String LOG_MESSAGE_FOR_REDIS = "{} {}{} Failed to {} X-Idempotency-ID {} - {}";

    private final PostRequestRepository postRequestRepository;

    public IdempotentReply getByXIdempotencyId(final String xIdempotencyId){
        try {
            final var idempotency = postRequestRepository.findById(xIdempotencyId);

            if(idempotency.isPresent()){
                final var request = idempotency.get();

                return IdempotentReply.newBuilder().setId(request.getId()).setXIdempotencyKey(request.getXIdempotencyId()).build();
            } else {
                return IdempotentReply.newBuilder().build();
            }
        } catch (RedisConnectionFailureException redisConnectionFailureException) {
            log.error(LOG_MESSAGE_FOR_REDIS, LoggingConstants.Symbols.ERROR, LoggingConstants.Tags.REDIS, LoggingConstants.Tags.IDEMPOTENT, FIND_OPERATION, xIdempotencyId, trace(), redisConnectionFailureException);
            throw new RedisServiceUnavailableException("Redis server is down or unreachable", redisConnectionFailureException);
        } catch (final SerializationException serializationException){
            log.error(LOG_MESSAGE_FOR_REDIS, LoggingConstants.Symbols.ERROR, LoggingConstants.Tags.REDIS, LoggingConstants.Tags.IDEMPOTENT, FIND_OPERATION, xIdempotencyId, trace(), serializationException);
            throw new RedisSerializationException("Can’t (de)serialize an object", serializationException);
        } catch (final RedisSystemException redisSystemException){
            log.error(LOG_MESSAGE_FOR_REDIS, LoggingConstants.Symbols.ERROR, LoggingConstants.Tags.REDIS, LoggingConstants.Tags.IDEMPOTENT, FIND_OPERATION, xIdempotencyId, trace(), redisSystemException);
            throw new RedisErrorDuringSavingException("General Redis failures", redisSystemException);
        }
    }

    public IdempotentReply createPostRequest(final String requestId){
        try {
            final var postRequest = postRequestRepository.save(new PostRequestRedisHash(requestId));

            return IdempotentReply.newBuilder().setId(postRequest.getId()).build();
        } catch (RedisConnectionFailureException redisConnectionFailureException) {
            log.error(LOG_MESSAGE_FOR_REDIS, LoggingConstants.Symbols.ERROR, LoggingConstants.Tags.REDIS, LoggingConstants.Tags.IDEMPOTENT, PERSIST_OPERATION, requestId, trace(), redisConnectionFailureException);
            throw new RedisServiceUnavailableException("Redis server is down or unreachable", redisConnectionFailureException);
        } catch (final SerializationException serializationException){
            log.error(LOG_MESSAGE_FOR_REDIS, LoggingConstants.Symbols.ERROR, LoggingConstants.Tags.REDIS, LoggingConstants.Tags.IDEMPOTENT, PERSIST_OPERATION, requestId, trace(), serializationException);
            throw new RedisSerializationException("Can’t (de)serialize an object", serializationException);
        } catch (final RedisSystemException redisSystemException){
            log.error(LOG_MESSAGE_FOR_REDIS, LoggingConstants.Symbols.ERROR, LoggingConstants.Tags.REDIS, LoggingConstants.Tags.IDEMPOTENT, PERSIST_OPERATION, requestId, trace(), redisSystemException);
            throw new RedisErrorDuringSavingException("General Redis failures", redisSystemException);
        }
    }

}
