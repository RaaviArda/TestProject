package org.raavi.demo.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.raavi.demo.dao.NaceDAO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NaceData {
    private Integer order;

    private Integer level;

    private String code;

    private String parent;

    private String description;

    private String includes;

    private String alsoIncludes;

    private String rulings;

    private String excludes;

    private String reference;

    public static NaceData fromEntity(NaceDAO entity) {
        return new NaceData(entity.getOrder(),
                entity.getLevel(),
                entity.getCode(),
                entity.getParent(),
                entity.getDescription(),
                entity.getIncludes(),
                entity.getAlsoIncludes(),
                entity.getRulings(),
                entity.getExcludes(),
                entity.getReference());
    }

    public NaceDAO toEntity() {
        NaceDAO entity = new NaceDAO();
        entity.setOrder(this.order);
        entity.setLevel(this.level);
        entity.setCode(this.code);
        entity.setParent(this.parent);
        entity.setDescription(this.description);
        entity.setIncludes(this.includes);
        entity.setAlsoIncludes(this.alsoIncludes);
        entity.setRulings(this.rulings);
        entity.setExcludes(this.excludes);
        entity.setReference(this.reference);
        return entity;
    }
}
