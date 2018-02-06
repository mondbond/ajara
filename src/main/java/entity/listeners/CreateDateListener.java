package entity.listeners;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

public class CreateDateListener {

    @PrePersist
    public void setCreateDate(HasDate entity){
        entity.setDate(new Date());
    }
}
