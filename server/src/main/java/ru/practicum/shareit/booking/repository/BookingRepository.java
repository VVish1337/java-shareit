package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b from Booking b where b.booker.id = ?1 and b.status = ?2 order by b.start asc")
    Page<Booking> findRejectedBookings(Long userId, BookingStatus status, Pageable pageable);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 " +
            "and b.start < ?2 " +
            "and b.end < ?2 " +
            "order by b.start asc ")
    Page<Booking> findPastBookings(Long userId, LocalDateTime now,Pageable pageable);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "and b.start < ?2 " +
            "and b.end > ?2 " +
            "order by b.start asc")
    Page<Booking> findBookingsByItemOwnerCurrent(Long userId, LocalDateTime now,Pageable pageable);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 " +
            "and b.start < ?2 " +
            "and b.end > ?2 " +
            "order by b.start asc ")
    Page<Booking> findByBookerIdCurrent(Long userId, LocalDateTime now,Pageable pageable);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.status = ?2 order by b.start asc")
    Page<Booking> findRejectedBookingsByOwner(Long userId, BookingStatus rejected,Pageable pageable);

    Page<Booking> findByBookerIdAndStartIsAfter(Long userId, LocalDateTime now, Pageable pageable);

    Page<Booking> findByBookerId(Long userId, Pageable pageable);

    Page<Booking> findByItemOwnerId(Long userId, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndStartIsAfter(Long userId, LocalDateTime now, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndEndIsBefore(Long userId, LocalDateTime now, Pageable pageable);

    List<Booking> findByItemIdAndEndBefore(Long itemId, LocalDateTime now, Sort sort);

    List<Booking> findByItemIdAndStartAfter(Long itemId, LocalDateTime now, Sort sort);

    @Query("select b from Booking b " +
            " where b.item.id = ?1 " +
            " and b.booker.id = ?2" +
            " and b.end < ?3")
    List<Booking> findSuitableBookingsForComments(Long itemId, Long userId, LocalDateTime now);
}