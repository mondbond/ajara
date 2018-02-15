package exception;


import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NoArgsConstructor
public class ReviewException extends Exception {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookException.class);

    public ReviewException(String message) {
        super(message);
        LOGGER.info("message = [{}] ", message);
    }

    public ReviewException(String message, Exception exception) {
        super(message, exception);
        LOGGER.info("message = [{}] ", message);
    }
}