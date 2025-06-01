package bhsg.com.cache.exceptions;

public final class RedisServiceUnavailableException extends RuntimeException {

    public RedisServiceUnavailableException(final String message, final Throwable throwable){
        super(message, throwable);
    }

}
