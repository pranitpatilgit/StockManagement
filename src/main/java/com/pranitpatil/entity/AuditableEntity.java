package com.pranitpatil.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@MappedSuperclass
public class AuditableEntity {

    @Column(updatable = false, name = "CREATED_AT")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(updatable = true, name = "LAST_MODIFIED_AT")
    @UpdateTimestamp
    private LocalDateTime lastModifiedAt;

    @Version
    private int version;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }
}
