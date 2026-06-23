package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.ItemBookingProjection;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingStorage extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByCreatedAtAsc(Long userId);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByCreatedAtAsc(Long userId, Instant before);

    @Query("SELECT b FROM Booking b "
            + "WHERE b.booker.id = :userId "
            + "  AND b.start <= :now "
            + "  AND b.end > :now "
            + "ORDER BY b.createdAt ASC")
    List<Booking> findCurrentBookingsByOwnerId(
            @Param("userId") Long userId,
            @Param("now") Instant now);

    @Query("SELECT b FROM Booking b "
            + "WHERE b.booker.id = :userId "
            + " AND b.start > :now "
            + "ORDER BY b.createdAt ASC")
    List<Booking> findFutureBookingsByOwnerId(
            @Param("userId") Long userId,
            @Param("now") Instant now);

    List<Booking> findAllByBookerIdAndStatusOrderByCreatedAtAsc(Long userId, Status status);

    // для владельца вещи
    List<Booking> findByItemOwnerIdEquals(Long itemOwnerId);

    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByCreatedAtAsc(Long itemOwnerId, Instant before);

    @Query("SELECT b FROM Booking b "
            + "JOIN Item it "
            + "WHERE it.owner.id = :itemOwnerId "
            + "  AND b.start <= :now "
            + "  AND b.end > :now "
            + "ORDER BY b.createdAt ASC")
    List<Booking> findCurrentBookingsByItemOwner(
            @Param("itemOwnerId") Long itemOwnerId,
            @Param("now") Instant now);

    @Query("SELECT b FROM Booking b "
            + "JOIN Item it "
            + "WHERE it.owner.id = :itemOwnerId "
            + " AND b.start > :now "
            + "ORDER BY b.createdAt ASC")
    List<Booking> findFutureBookingsByItemOwner(
            @Param("itemOwnerId") Long itemOwnerId,
            @Param("now") Instant now);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByCreatedAtAsc(Long itemOwnerId, Status status);

    @Query(nativeQuery = true, value = """
    SELECT
        it.item_id AS itemId,
        MIN(CASE WHEN b.start_date >= NOW() THEN b.start_date END) AS nextBookingStart,
        MAX(CASE WHEN b.end_date < (NOW() + INTERVAL '10' SECOND) THEN b.end_date END) AS lastBookingEnd
    FROM items it
    LEFT JOIN bookings b ON it.item_id = b.item_id
    WHERE it.owner.id = ?1
    GROUP BY it.item_id;
    """)
    List<ItemBookingProjection> findAllByItemsWithBookingDates(Long userId);

    @Query(nativeQuery = true, value = """
            SELECT
                it.item_id AS itemId,
                MIN(CASE WHEN b.start_date >= NOW() THEN b.start_date END) AS nextBookingStart,
                MAX(CASE WHEN b.end_date < (NOW() + INTERVAL '10' SECOND) THEN b.end_date END) AS lastBookingEnd
            FROM items it
            LEFT JOIN bookings b ON it.item_id = b.item_id
            WHERE it.item_id = ?1
            GROUP BY it.item_id;
            """)
    Optional<ItemBookingProjection> findItemWithBookingDates(Long itemId);

    Optional<Booking> findByBookerIdAndItemId(Long userId, Long itemId);

    boolean existsByBookerIdAndItemIdAndStatus(Long userId,
                                             Long itemId,
                                             Status status);

    Optional<Booking> findByItemIdAndBookerId(Long itemId, Long bookerId);

    boolean existsByBookerIdAndItemIdAndStatusAndEndBefore(Long userId,
                                                           Long itemId,
                                                           Status status,
                                                           Instant instant);

    Optional<Booking> findFirstByBookerIdAndItemIdAndStatusOrderByEndDesc(Long userId,
                                                                          Long itemId,
                                                                          Status status);
}
