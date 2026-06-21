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

    List<Booking> findAllByBookerIdAndEndDateBeforeOrderByCreatedAtAsc(Long userId, Instant before);

    @Query("SELECT b FROM Booking b "
            + "WHERE b.booker.id = :userId "
            + "  AND b.startDate <= :now "
            + "  AND b.endDate > :now "
            + "ORDER BY b.createdAt ASC")
    List<Booking> findCurrentBookingsByUserId(
            @Param("userId") Long userId,
            @Param("now") Instant now);

    @Query("SELECT b FROM Booking b "
            + "WHERE b.booker.id = :userId "
            + " AND b.startDate > :now "
            + "ORDER BY b.createdAt ASC")
    List<Booking> findFutureBookingsByUserId(
            @Param("userId") Long userId,
            @Param("now") Instant now);

    List<Booking> findAllByBookerIdAndStatusOrderByCreatedAtAsc(Long userId, Status status);

    // для владельца вещи
    List<Booking> findByItemUserIdEquals(Long itemOwnerId);

    List<Booking> findAllByItemUserIdAndEndDateBeforeOrderByCreatedAtAsc(Long itemOwnerId, Instant before);

    @Query("SELECT b FROM Booking b "
            + "JOIN Item it "
            + "WHERE it.userId = :itemOwnerId "
            + "  AND b.startDate <= :now "
            + "  AND b.endDate > :now "
            + "ORDER BY b.createdAt ASC")
    List<Booking> findCurrentBookingsByItemOwner(
            @Param("itemOwnerId") Long itemOwnerId,
            @Param("now") Instant now);

    @Query("SELECT b FROM Booking b "
            + "JOIN Item it "
            + "WHERE it.userId = :itemOwnerId "
            + " AND b.startDate > :now "
            + "ORDER BY b.createdAt ASC")
    List<Booking> findFutureBookingsByItemOwner(
            @Param("itemOwnerId") Long itemOwnerId,
            @Param("now") Instant now);

    List<Booking> findAllByItemUserIdAndStatusOrderByCreatedAtAsc(Long itemOwnerId, Status status);

    @Query(nativeQuery = true, value = """
    SELECT
        it.item_id AS itemId,
        MIN(CASE WHEN b.start_date >= NOW() THEN b.start_date END) AS nextBookingStart,
        MAX(CASE WHEN b.end_date < (NOW() - INTERVAL '10' SECOND) THEN b.end_date END) AS lastBookingEnd
    FROM items it
    LEFT JOIN bookings b ON it.item_id = b.item_id
    WHERE it.user_id = ?1
    GROUP BY it.item_id;
    """)
    List<ItemBookingProjection> findAllByItemsWithBookingDates(Long userId);

    @Query(nativeQuery = true, value = """
            SELECT
                it.item_id AS itemId,
                MIN(CASE WHEN b.start_date >= NOW() THEN b.start_date END) AS nextBookingStart,
                MAX(CASE WHEN b.end_date < (NOW() - INTERVAL '10' SECOND) THEN b.end_date END) AS lastBookingEnd
            FROM items it
            LEFT JOIN bookings b ON it.item_id = b.item_id
            WHERE it.item_id = ?1
            GROUP BY it.item_id;
            """)
    Optional<ItemBookingProjection> findItemWithBookingDates(Long itemId);

    Optional<Booking> findByBookerIdAndItemId(Long userId, Long itemId);
}
