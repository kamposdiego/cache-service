package bhsg.com.cache.exceptions;

public final class RedisSerializationException extends RuntimeException {

    public RedisSerializationException(final String message, final Throwable throwable){
        super(message, throwable);
    }
}
