package com.ogul.wordfinder.repository.impl;

import com.ogul.wordfinder.dto.WordResponse;
import com.ogul.wordfinder.entity.Word;
import com.ogul.wordfinder.mapper.WordMapper;
import com.ogul.wordfinder.repository.CustomWordRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomWordRepositoryImpl implements CustomWordRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    private final WordMapper wordMapper;

    @Override
    public List<WordResponse> findMatchingWords(
        Map<Character, Integer> letterCounts, Integer minLength, Integer maxLength, Pageable pageable
    ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Word> query = cb.createQuery(Word.class);
        Root<Word> root = query.from(Word.class);
        List<Predicate> predicates = new ArrayList<>();

        final String alphabet = "abcdefghijklmnopqrstuvwxyz";

        for (char letter: alphabet.toCharArray()) {
            Expression<Integer> countLetter = cb.diff(
                cb.length(root.get("word")),
                cb.length(
                    cb.function("REPLACE", String.class, root.get("word"), cb.literal(String.valueOf(letter)), cb.literal(""))
                )
            );
            predicates.add(cb.lessThanOrEqualTo(countLetter, letterCounts.getOrDefault(letter, 0)));
        }
        predicates.add(cb.between(cb.function("LENGTH", Integer.class, root.get("word")), minLength, maxLength));

        Order ordering = pageable.getSort().isSorted() ?
            pageable.getSort().stream()
                .map(order -> {
                    if (order.isAscending()) {
                        if (order.getProperty().equals("length")) {
                            return cb.asc(cb.length(root.get("word")));
                        } else {
                            return cb.asc(root.get(order.getProperty()));
                        }
                    } else {
                        if (order.getProperty().equals("length")) {
                            return cb.desc(cb.length(root.get("word")));
                        } else {
                            return cb.desc(root.get(order.getProperty()));
                        }
                    }
                })
                .findFirst()
                .orElse(null)
            : null;

        Predicate predicate = cb.and(predicates.toArray(Predicate[]::new));

        if (ordering != null) {
            query.orderBy(ordering);
        }
        query.select(root).where(predicate);
        return entityManager.createQuery(query)
            .setFirstResult((int)pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .getResultList().stream().map(wordMapper::map).toList();
    }
}
