package com.bekrenovr.ecommerce.catalog.repository;

import com.bekrenovr.ecommerce.catalog.exception.ItemApplicationException;
import com.bekrenovr.ecommerce.catalog.model.Item;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.bekrenovr.ecommerce.catalog.exception.ItemApplicationExceptionReason.CANNOT_ADD_ITEMS_TO_LANDING_PAGE;

@Repository
@RequiredArgsConstructor
public class LandingPageRepository {
    private final ItemRepository itemRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public List<Item> getLandingPageItems(){
        Query query = entityManager.createNativeQuery("select item_id from landing_page_item");
        List<Long> itemsIdsLong = ((List<Integer>) query.getResultList())
                .stream()
                .map(Integer::longValue)
                .toList();
        return itemRepository.findAllById(itemsIdsLong);
    }

    @Transactional
    public void addLandingPageItems(List<Long> itemsIds) {
        List<Item> itemsToAdd = itemRepository.findAllById(itemsIds)
                .stream()
                .filter(item -> !getLandingPageItems().contains(item))
                .toList();
        List<Integer> idsToAdd = itemsToAdd.stream()
                .map(Item::getId)
                .map(Long::intValue)
                .toList();

        if(idsToAdd.isEmpty()) throw new ItemApplicationException(CANNOT_ADD_ITEMS_TO_LANDING_PAGE);

        var sql = composeSqlForAddingItems(idsToAdd);
        System.out.println(sql);
        Query query = entityManager.createNativeQuery(sql);
        query.executeUpdate();
    }

    private String composeSqlForAddingItems(List<Integer> idsToAdd){
        StringBuilder sqlBuilder = new StringBuilder("insert into landing_page_item values ");
        for(int i = 0; i < idsToAdd.size(); i++){
            boolean isLast = i == idsToAdd.size() - 1;
            sqlBuilder
                    .append('(')
                    .append(idsToAdd.get(i))
                    .append(")")
                    .append(isLast ? "" : ", ");
        }
        return sqlBuilder.toString();
    }
}
