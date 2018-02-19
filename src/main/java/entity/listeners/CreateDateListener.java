package entity.listeners;

import entity.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PrePersist;
import java.time.LocalDateTime;

public class CreateDateListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateDateListener.class);

    @PrePersist
    public void setCreateDate(BaseEntity entity){
        entity.setCreateDate(LocalDateTime.now());
    }
}
