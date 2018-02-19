package util.exception;

import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import java.util.Iterator;
import java.util.Map;

public class CustomExceptionHandler extends ExceptionHandlerWrapper  {

    private ExceptionHandler wrapped;

    CustomExceptionHandler(ExceptionHandler exception) {
        this.wrapped = exception;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }


    @Override
    public void handle() throws FacesException {

        @val Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator();
        while (i.hasNext()) {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context =
                    (ExceptionQueuedEventContext) event.getSource();
            Throwable t = context.getException();
            @val FacesContext fc = FacesContext.getCurrentInstance();
            @val  Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
            @val NavigationHandler nav = fc.getApplication().getNavigationHandler();
            try {
                requestMap.put("exceptionMessage", ExceptionUtils.getRootCauseMessage(t));
                nav.handleNavigation(fc, null, "/view/error_page.xhtml");
                fc.renderResponse();
            } finally {
                i.remove();
            }
        }
        wrapped.handle();
    }
}

