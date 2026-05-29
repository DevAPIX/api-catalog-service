package com.devapix.spec;

import com.devapix.model.ApiModel;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ApiSpecification {

    public static Specification<ApiModel> filterApis(Integer categoryId, String visibility, String queryText, String wildcard
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
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
