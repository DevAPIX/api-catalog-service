package com.devapix.api_catalog_service.spec;

import com.devapix.api_catalog_service.model.ApiModel;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class ApiSpecification {

    public static Specification<ApiModel> filterApis(Integer categoryId, String visibility, String queryText, String wildcard, boolean includeDeleted, Integer ownerId
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!includeDeleted) {
                predicates.add(cb.notEqual(root.get("status"), "DELETED"));
            }
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("categoryId"), categoryId));
            }
            if (visibility != null && !visibility.isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("visibility")), visibility.toLowerCase()));
            }
            if (queryText != null && !queryText.isEmpty()) {
                String searchPattern = wildcard + queryText.toLowerCase() + wildcard;
                Predicate nameLike = cb.like(cb.lower(root.get("name")), searchPattern);
                Predicate descLike = cb.like(cb.lower(root.get("description")), searchPattern);
                predicates.add(cb.or(nameLike, descLike));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
