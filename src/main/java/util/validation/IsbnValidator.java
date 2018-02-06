package util.validation;

import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;

@RequestScoped
@FacesValidator("util.validation.IsbnValidator")
public class IsbnValidator implements Validator, Serializable {

    public IsbnValidator() {
    }

    @PersistenceContext
    protected EntityManager em;

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {


        o.toString();
        if(isUnique("ISBN",o.toString())){
            FacesMessage msg =
                    new FacesMessage("E-mail validation failed.",
                            "Invalid E-mail format.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);

        }
    }
    public boolean isUnique(String columnName, String value){
        String sqlString = "select count (*) from Book where " + columnName + " = " + value;
        Query query = em.createQuery(sqlString);
        Long result = (Long) query.getSingleResult();
        return result == 0;
    }
}
