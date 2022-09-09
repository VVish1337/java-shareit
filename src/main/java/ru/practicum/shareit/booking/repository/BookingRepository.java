package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {


    @Query("select b from Booking b where b.booker.id = ?1 and b.status = ?2 order by b.start asc")
    List<Booking> findRejectedBookings(Long userId, BookingStatus status);

//    @Query("select b from Booking b " +
//            "where b.booker.id = ?1 " +
//            "and b.start > ?2 " +
////            "and b.end > ?2 " +
//            "order by b.start asc ")
//    List<Booking> findFutureBookings(Long userId, LocalDateTime now);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 " +
            "and b.start < ?2 " +
            "and b.end < ?2 " +
            "order by b.start asc ")
    List<Booking> findPastBookings(Long userId, LocalDateTime now);
//
//    @Query("select b from Booking b where b.booker.id = ?1 order by b.start asc")
//    List<Booking> findAllBookings(Long userId);
//
//    @Query("select b from Booking b " +
//            "where b.item.owner.id = ?1 " +
//            "and b.start > ?2 " +
//            "and b.end > ?2 " +
//            "order by b.start asc")
//    List<Booking> findPastByItemOwner(Long bookerId, LocalDateTime now);
//
//    @Query("select b from Booking b " +
//            "where b.item.owner.id = ?1 " +
//            "and b.start > ?2 " +
////            "and b.end > ?2 " +
//            "order by b.start asc")
//    List<Booking> findFutureBookingByItemOwner(Long bookerId, LocalDateTime now);
//
//    @Query("select b from Booking b where b.item.owner.id = ?1 order by b.start asc")
//    List<Booking> findAllBookingByItemOwner(Long bookerId);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "and b.start < ?2 " +
            "and b.end > ?2 " +
            "order by b.start asc")
    List<Booking> findBookingsByItemOwnerCurrent(Long userId, LocalDateTime now);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 " +
            "and b.start < ?2 " +
            "and b.end > ?2 " +
            "order by b.start asc ")
    List<Booking> findByBookerIdCurrent(Long userId, LocalDateTime now);


    @Query("select b from Booking b where b.item.owner.id = ?1 and b.status = ?2 order by b.start asc")
    List<Booking> findRejectedBookingsByOwner(Long userId, BookingStatus rejected);

    List<Booking> findByBookerIdAndStartIsAfter(Long userId, LocalDateTime now, Sort sort);

    List<Booking> findByBookerId(Long userId, Sort sort);

    List<Booking> findByItemOwnerId(Long userId, Sort sort);

    List<Booking> findByItemOwnerIdAndStartIsAfter(Long userId, LocalDateTime now, Sort sort);

    List<Booking> findByItemOwnerIdAndEndIsBefore(Long userId, LocalDateTime now, Sort sort);

    List<Booking> findByItemIdAndEndBefore(Long itemId, LocalDateTime now, Sort sort);

    List<Booking> findByItemIdAndStartAfter(Long itemId, LocalDateTime now, Sort sort);


    @Query("select b from Booking b " +
            " where b.item.id = ?1 " +
            " and b.booker.id = ?2" +
            " and b.end < ?3")
    List<Booking> findSuitableBookingsForComments(Long itemId, Long userId, LocalDateTime now);
}