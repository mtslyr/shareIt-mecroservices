package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemStorage extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerId(Long userId);

    Optional<Item> findById(Long itemId);

    void deleteById(Long itemId);

    @Query(nativeQuery = true, value = """
    select *
    from items as it
    where (LOWER(it.name) LIKE CONCAT('%', LOWER(:query), '%')
    OR LOWER(it.description) LIKE CONCAT('%', LOWER(:query), '%'))
    AND it.available IS TRUE
    """)
    List<Item> search(@Param("query") String search);

    List<Item> findAllByRequestId(Long requestId);

    List<Item> findAllByRequestIdIn(List<Long> requestIds);
}
