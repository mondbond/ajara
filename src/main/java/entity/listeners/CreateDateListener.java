package entity.listeners;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

public class CreateDateListener {

    @PrePersist
    @PreUpdate
    public void setCreateDate(HasDate entity){
        entity.setDate(new Date());
    }
}
