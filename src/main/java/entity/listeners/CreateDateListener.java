package entity.listeners;

import entity.BaseEntity;

import javax.persistence.PrePersist;
import java.time.LocalDateTime;

public class CreateDateListener {

    @PrePersist
    public void setCreateDate(BaseEntity entity){
        entity.setCreateDate(LocalDateTime.now());
    }
}
