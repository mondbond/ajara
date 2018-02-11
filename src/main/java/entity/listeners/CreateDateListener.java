package entity.listeners;

import javax.persistence.PrePersist;
import java.time.LocalDate;

public class CreateDateListener {

    @PrePersist
    public void setCreateDate(HasDate entity){
        entity.setDate(LocalDate.now());
    }
}
