package util;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@ManagedBean(name="commonUtils")
@ApplicationScoped
public class CommonUtil {
    private static final long serialVersionUID = 1L;

    public void redirectWithGet() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletRequest request = (HttpServletRequest)externalContext.getRequest();

        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();

        if (queryString != null) {
            requestURL.append('?').append(queryString).toString();
        }

        String url = requestURL.toString();
        try {
            externalContext.redirect(requestURL.toString());
        } catch (IOException e) {
            throw new RuntimeException("Unable to rerirect to " + url);
        }

        facesContext.responseComplete();
    }
}
