package util.exception;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NoArgsConstructor
public class AuthorException extends Exception {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorException.class);

    public AuthorException(String message){
        super(message);
        LOGGER.info("message = [{}] ", message);
    }

    public AuthorException(String message, Exception exception){
        super(message, exception);
        LOGGER.info("message = [{}] ", message);
    }
}
