package com.bekrenovr.ecommerce.catalog.repository;

import com.bekrenovr.ecommerce.catalog.model.entity.Item;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class LandingPageRepository {
    private final ItemRepository itemRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public List<Item> getLandingPageItems(){
        Query query = entityManager.createNativeQuery("select item_id from landing_page_item");
        List<UUID> itemsIds = ((List<UUID>) query.getResultList())
                .stream()
                .toList();
        return itemRepository.findAllById(itemsIds);
    }

    @Transactional
    public void addLandingPageItems(List<Item> itemsToAdd) {
        List<UUID> idsToAdd = itemsToAdd.stream()
                .map(Item::getId)
                .toList();
        String sql = composeSqlForAddingItems(idsToAdd);
        Query query = entityManager.createNativeQuery(sql);
        query.executeUpdate();
    }

    @Transactional
    public void removeLandingPageItems(List<Item> itemsToRemove){
        List<UUID> idsToRemove = itemsToRemove.stream()
                .map(Item::getId)
                .toList();
        String sql = composeSqlForRemovingItems(idsToRemove);
        Query query = entityManager.createNativeQuery(sql);
        query.executeUpdate();
    }

    private String composeSqlForAddingItems(List<UUID> idsToAdd){
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

    private String composeSqlForRemovingItems(List<UUID> idsToRemove){
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
