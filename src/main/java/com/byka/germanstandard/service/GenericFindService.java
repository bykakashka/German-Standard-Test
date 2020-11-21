package com.byka.germanstandard.service;

import com.byka.germanstandard.enums.AgregatorsEnum;
import com.byka.germanstandard.filter.GenericFilter;
import com.byka.germanstandard.model.AdsMetric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class GenericFindService {
    @Autowired
    private EntityManager entityManager;

    @Value("${com.byka.defaultScale}")
    private Integer defaultScale;

    /* in my opinion, API should not have a generic endpoint to handle all possible cases like this.
    Because in future with a lot of logic it will be hard to read/debug/refactor/mantain.
    But there is an example how we can handle different filters and agregations
    */

    public List<Map<String, Object>> find(GenericFilter filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = cb.createQuery(Object[].class);
        Root<AdsMetric> root = criteriaQuery.from(AdsMetric.class);

        List<Predicate> predicates = buildFilter(filter, cb, root);
        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        List<Expression<AdsMetric>> grouping = buildGroupBy(filter, root);
        criteriaQuery.groupBy(grouping.toArray(new Expression[0]));

        List<Selection> selectors = buildSelectors(filter, cb, root);
        criteriaQuery.multiselect(selectors.toArray(new Selection[0]));

        List<Object[]> result = entityManager.createQuery(criteriaQuery).getResultList();
        return convertResult(selectors, result, filter.getAggregators().contains(AgregatorsEnum.CTR));
    }

    private double calculateCTR(Object clicks, Object impressions) {
        if ((Long) impressions == 0L) {
            return 0;
        }

        return BigDecimal.valueOf((Long) clicks)
                .divide(BigDecimal.valueOf((Long) impressions), defaultScale, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private List<Selection> buildSelectors(GenericFilter filter, CriteriaBuilder cb, Root<AdsMetric> root) {
        Set<Selection> selectors = new HashSet<>();
        if (filter.getAggregators().contains(AgregatorsEnum.CTR)) {
            filter.getAggregators().add(AgregatorsEnum.CLICKS);
            filter.getAggregators().add(AgregatorsEnum.IMPRESSIONS);
        }

        filter.getGroupBy().forEach(group ->
                selectors.add(root.get(group.name().toLowerCase()).alias(group.name().toLowerCase()))
        );

        filter.getAggregators().forEach(agregator -> {
            if (!AgregatorsEnum.CTR.equals(agregator)) {
                selectors.add(cb.sum(root.get(agregator.name().toLowerCase())).alias(agregator.name().toLowerCase()));
            }
        });
        return new ArrayList<>(selectors);
    }

    private List<Expression<AdsMetric>> buildGroupBy(GenericFilter filter, Root<AdsMetric> root) {
        List<Expression<AdsMetric>> expressionList = new ArrayList<>();
        filter.getGroupBy().forEach(group -> expressionList.add(root.get(group.name().toLowerCase())));

        return expressionList;
    }

    private List<Predicate> buildFilter(GenericFilter filter, CriteriaBuilder cb, Root<AdsMetric> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getStartDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("date"), filter.getStartDate()));
        }

        if (filter.getEndDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("date"), filter.getEndDate()));
        }

        if (filter.getCampaigns() != null && !filter.getCampaigns().isEmpty()) {
            CriteriaBuilder.In<String> inPredicate = cb.in(root.get("campaign"));
            filter.getCampaigns().forEach(inPredicate::value);
            predicates.add(inPredicate);
        }

        if (filter.getDatasources() != null && !filter.getDatasources().isEmpty()) {
            CriteriaBuilder.In<String> inPredicate = cb.in(root.get("datasource"));
            filter.getDatasources().forEach(inPredicate::value);
            predicates.add(inPredicate);
        }

        return predicates;
    }

    private List<Map<String, Object>> convertResult(List<Selection> selectors, List<Object[]> result, boolean calculateCTR) {
        List<Map<String, Object>> convertedResult = new ArrayList<>();

        result.forEach(rawEntry -> {
            Map<String, Object> convertedEntry = new HashMap<>();
            int i = 0;
            for (Object value : rawEntry) {
                convertedEntry.put(selectors.get(i).getAlias(), value);
                i++;
            }

            if (calculateCTR) {
                convertedEntry.put(AgregatorsEnum.CTR.name().toLowerCase(),
                        calculateCTR(convertedEntry.get(AgregatorsEnum.CLICKS.name().toLowerCase()),
                                convertedEntry.get(AgregatorsEnum.IMPRESSIONS.name().toLowerCase())));
            }
            convertedResult.add(convertedEntry);
        });
        return convertedResult;
    }
}
