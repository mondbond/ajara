package controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class ErrorController {
    public void throwError(){
        throw new RuntimeException("throwing new error");
    }
}
