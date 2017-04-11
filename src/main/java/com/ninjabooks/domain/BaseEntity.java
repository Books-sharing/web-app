package com.ninjabooks.domain;

import javax.persistence.*;

/**
 * Base entity contains id, which is needed in every table.
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@MappedSuperclass
public abstract class BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public BaseEntity() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}