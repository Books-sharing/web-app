package com.ninjabooks.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public abstract class BaseEntityDto implements Serializable
{
    private static final long serialVersionUID = -8303931784541468748L;

    private Long id;

    @JsonIgnore
    private boolean active;

    public BaseEntityDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
