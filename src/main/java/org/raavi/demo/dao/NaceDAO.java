package org.raavi.demo.dao;

import jdk.jfr.Name;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="nace")
@Data
public class NaceDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer order;

    private Integer level;

    private String code;

    private String parent;

    private String description;

    private String includes;

    @Name("also_includes")
    private String alsoIncludes;

    private String rulings;

    private String excludes;

    private String reference;
}
