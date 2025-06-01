package bhsg.com.cache.exceptions;

public final class RedisErrorDuringSavingException extends RuntimeException {

    public RedisErrorDuringSavingException(final String message, final Throwable throwable){
        super(message, throwable);
    }

}
