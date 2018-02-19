package beans;

import entity.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.BookFacade;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

@ManagedBean(name = "validatorBean")
@ViewScoped
public class ValidationBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationBean.class);

    @EJB
    private BookFacade bookFacade;

    public void validateISBN(FacesContext context, UIComponent component,
                             Object value) throws ValidatorException {
        LOGGER.info("validateISBN [{}]", value.toString());
        if (!value.toString().matches("[0-9]{10,18}")) {
            FacesMessage msg =
                    new FacesMessage("ISBN must contain minimum 10, maximum 18 numbers",
                            "ISBN must contain minimum 10, maximum 18 numbers");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        } else if (!bookFacade.isUnique(Book.ISBN_COLUMN, value.toString())) {
            FacesMessage msg =
                    new FacesMessage("ISBN is not unique",
                            "ISBN is not unique");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }
}
