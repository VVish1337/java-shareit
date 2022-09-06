package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.dto.BookingPostResponseDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    public BookingPostResponseDto addBooking(long userId, BookingPostDto bookingDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new NotFoundException("User not found"));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(()->new NotFoundException("Item not found"));
        Booking booking = BookingMapper.toBooking(user,item,bookingDto);
        bookingRepository.save(booking);
        return BookingMapper.toPostResponseDto(booking);
    }

    public BookingGetDto getBooking(long bookingId,long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new NotFoundException("User not found"));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(()->new NotFoundException("Booking not found"));
        if(booking.getBooker().getId()!=userId){
            throw new NotFoundException("Wrong UserID");
        }
        return BookingMapper.toBookingGetDto(booking);
    }
}