package com.bekrenovr.ecommerce.catalog.repository;

import com.bekrenovr.ecommerce.catalog.model.Item;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public void addLandingPageItems(List<Item> itemsToAdd) {
        List<Integer> idsToAdd = itemsToAdd.stream()
                .map(Item::getId)
                .map(Long::intValue)
                .toList();
        String sql = composeSqlForAddingItems(idsToAdd);
        Query query = entityManager.createNativeQuery(sql);
        query.executeUpdate();
    }

    @Transactional
    public void removeLandingPageItems(List<Item> itemsToRemove){
        List<Integer> idsToRemove = itemsToRemove.stream()
                .map(Item::getId)
                .map(Long::intValue)
                .toList();
        String sql = composeSqlForRemovingItems(idsToRemove);
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

    private String composeSqlForRemovingItems(List<Integer> idsToRemove){
        StringBuilder sqlBuilder = new StringBuilder("delete from landing_page_item where landing_page_item.item_id in (");
        for(int i = 0; i < idsToRemove.size(); i++){
            boolean isLast = i == idsToRemove.size() - 1;
            sqlBuilder
                    .append(idsToRemove.get(i))
                    .append(isLast ? ")" : ", ");
        }
        return sqlBuilder.toString();
    }
}
