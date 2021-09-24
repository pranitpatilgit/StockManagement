package com.pranitpatil.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@MappedSuperclass
public class AuditableEntity {

    //@Temporal(value = TemporalType.TIMESTAMP)
    @Column(updatable = false, name = "CREATED_AT")
    @CreationTimestamp
    private LocalDateTime createdAt;

    //@Temporal(value = TemporalType.TIMESTAMP)
    @Column(updatable = true, name = "LAST_MODIFIED_AT")
    @UpdateTimestamp
    private LocalDateTime lastModifiedAt;

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
