package exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthorException extends Exception {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorException.class);

    public AuthorException(){
    }

    public AuthorException(String message){
        super(message);
        LOGGER.info("message = [{}] ", message);
    }

    public AuthorException(String message, Exception exception){
        super(message, exception);
        LOGGER.info("message = [{}] ", message);
    }
}

