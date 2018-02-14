package entity.listeners;

import javax.persistence.PrePersist;
import java.time.LocalDateTime;

public class CreateDateListener {

    @PrePersist
    public void setCreateDate(HasDate entity){
        entity.setDate(LocalDateTime.now());
    }
}
