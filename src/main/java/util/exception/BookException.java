package util.exception;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NoArgsConstructor
public class BookException extends Exception {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookException.class);

    public BookException(String message) {
        super(message);
        LOGGER.info("message = [{}] ", message);
    }

    public BookException(String message, Exception exception) {
        super(message, exception);
        LOGGER.info("message = [{}] ", message);
    }
}
