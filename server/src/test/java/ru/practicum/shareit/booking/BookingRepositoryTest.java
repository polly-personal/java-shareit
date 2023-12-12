package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.constant.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("UserRepositoryTest должен ")
@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User itemOwner = User.builder().name("test_owner-name_1").email("test_owner-email_1").build();
    private User firstBooker = User.builder().name("test_booker-name_1").email("test_booker-email_1").build();

    private Item item = Item.builder().name("test_name_1").description("test_description_1").available(true).owner(itemOwner).build();

    /*
    pastBooking: 1-9
    currentBooking: 10-19
    futureBooking: 20-29
    */
    private LocalDateTime past = LocalDateTime.of(2023, 11, 1, 01, 01, 01);
    private LocalDateTime current = LocalDateTime.of(2023, 11, 10, 01, 01, 01);
    private LocalDateTime future = LocalDateTime.of(2023, 11, 20, 01, 01, 01);
    private Booking pastBooking;
    private Booking currentBooking;
    private Booking futureBooking;

    private int from = 0;
    private int size = 10;
    private PageRequest pageRequest = PageRequest.of(from, size);

    @BeforeEach
    public void saveUsersAndItemsAndInitBookings() {
        userRepository.save(itemOwner);
        userRepository.save(firstBooker);
        itemRepository.save(item);

        pastBooking = Booking.builder().start(past).end(past.plusDays(8)).item(item).booker(firstBooker).status(Status.WAITING).build();
        currentBooking = Booking.builder().start(current).end(current.plusDays(9)).item(item).booker(firstBooker).status(Status.WAITING).build();
        futureBooking = Booking.builder().start(future).end(future.plusDays(9)).item(item).booker(firstBooker).status(Status.WAITING).build();
    }

    @DisplayName("сохранять бронь")
    @Test
    public void save_whenSuccessInvoked_thenSavedBookingIsReturned() {
        Assertions.assertNull(currentBooking.getId());
        Booking returnedBooking = bookingRepository.save(currentBooking);
        Assertions.assertNotNull(currentBooking.getId());
        Assertions.assertEquals(currentBooking, returnedBooking);
    }

    @DisplayName("НЕ сохранять бронь, если поле \"start\" == null")
    @Test
    public void save_whenStartIsNull_thenSavedBookingIsNotReturned() {
        currentBooking.setStart(null);
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> bookingRepository.save(currentBooking));
    }

    @DisplayName("НЕ сохранять бронь, если поле \"end\" == null")
    @Test
    public void save_whenEndIsNull_thenSavedBookingIsNotReturned() {
        currentBooking.setStatus(null);
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> bookingRepository.save(currentBooking));
    }

    @DisplayName("НЕ сохранять бронь, если поле \"status\" == null")
    @Test
    public void save_whenStatusIsNull_thenSavedBookingIsNotReturned() {
        currentBooking.setEnd(null);
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> bookingRepository.save(currentBooking));
    }

    @DisplayName("выдавать все брони бронирующего по полю \"booker.id\"")
    @Test
    public void findAllByBookerId_whenSuccessInvoked_thenFoundBookingsIsReturned() {
        User secondBooker = User.builder().name("test_booker-name_2").email("test_booker-email_2").build();
        userRepository.save(secondBooker);
        Booking firstBookingSecondBooker = Booking.builder().start(current).end(current.plusDays(9)).item(item).booker(secondBooker).status(Status.WAITING).build();

        bookingRepository.save(pastBooking);
        bookingRepository.save(currentBooking);
        bookingRepository.save(firstBookingSecondBooker);

        Page<Booking> returnedBookings = bookingRepository.findAllByBookerId(firstBooker.getId(), pageRequest);

        List<Booking> expected = new ArrayList<>();
        expected.add(pastBooking);
        expected.add(currentBooking);

        Assertions.assertEquals(expected, returnedBookings.toList());
    }

    @DisplayName("выдавать все CURRENT брони бронирующего по полю \"booker.id\"")
    @Test
    public void findAllByBookerIdAndStartBeforeAndEndAfter_whenSuccessInvoked_thenFoundBookingsIsReturned() {
        bookingRepository.save(pastBooking);
        bookingRepository.save(currentBooking);
        bookingRepository.save(futureBooking);

        Page<Booking> returnedBookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(firstBooker.getId(), future, current.plusDays(8), pageRequest);

        List<Booking> expected = new ArrayList<>();
        expected.add(currentBooking);

        Assertions.assertEquals(expected, returnedBookings.toList());
    }

    @DisplayName("выдавать все PAST брони бронирующего по полю \"booker.id\"")
    @Test
    public void findAllByBookerIdAndEndBefore_whenSuccessInvoked_thenFoundBookingsIsReturned() {
        bookingRepository.save(pastBooking);
        bookingRepository.save(currentBooking);
        bookingRepository.save(futureBooking);

        Page<Booking> returnedBookings = bookingRepository.findAllByBookerIdAndEndBefore(firstBooker.getId(), current, pageRequest);

        List<Booking> expected = new ArrayList<>();
        expected.add(pastBooking);

        Assertions.assertEquals(expected, returnedBookings.toList());
    }

    @DisplayName("выдавать все FUTURE брони бронирующего по полю \"booker.id\"")
    @Test
    public void findAllByBookerIdAndStartAfter_whenSuccessInvoked_thenFoundBookingsIsReturned() {
        bookingRepository.save(pastBooking);
        bookingRepository.save(currentBooking);
        bookingRepository.save(futureBooking);

        Page<Booking> returnedBookings = bookingRepository.findAllByBookerIdAndStartAfter(firstBooker.getId(), current.plusDays(9), pageRequest);

        List<Booking> expected = new ArrayList<>();
        expected.add(futureBooking);

        Assertions.assertEquals(expected, returnedBookings.toList());
    }

    @DisplayName("выдавать все WAITING брони бронирующего по полю \"booker.id\"")
    @Test
    public void findAllByBookerIdAndStatusEqualsWaiting_whenSuccessInvoked_thenFoundBookingsIsReturned() {
        currentBooking.setStatus(Status.APPROVED);
        futureBooking.setStatus(Status.REJECTED);

        bookingRepository.save(pastBooking);
        bookingRepository.save(currentBooking);
        bookingRepository.save(futureBooking);

        Page<Booking> returnedBookings = bookingRepository.findAllByBookerIdAndStatusEquals(firstBooker.getId(), Status.WAITING, pageRequest);

        List<Booking> expected = new ArrayList<>();
        expected.add(pastBooking);

        Assertions.assertEquals(expected, returnedBookings.toList());
    }

    @DisplayName("выдавать все REJECTED брони бронирующего по полю \"booker.id\"")
    @Test
    public void findAllByBookerIdAndStatusEqualsRejected_whenSuccessInvoked_thenFoundBookingsIsReturned() {
        currentBooking.setStatus(Status.APPROVED);
        futureBooking.setStatus(Status.REJECTED);

        bookingRepository.save(pastBooking);
        bookingRepository.save(currentBooking);
        bookingRepository.save(futureBooking);

        Page<Booking> returnedBookings = bookingRepository.findAllByBookerIdAndStatusEquals(firstBooker.getId(), Status.REJECTED, pageRequest);

        List<Booking> expected = new ArrayList<>();
        expected.add(futureBooking);

        Assertions.assertEquals(expected, returnedBookings.toList());
    }
}
