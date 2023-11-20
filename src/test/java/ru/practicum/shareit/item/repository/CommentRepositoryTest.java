package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("CommentRepositoryTest должен ")
@DataJpaTest
public class CommentRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    User user = User.builder().name("test_name_1").email("test_email_1").build();
    User author = User.builder().name("test_name_2").email("test_email_2").build();
    Item item = Item.builder().name("test_name_1").description("test_description_1").available(true).owner(user).build();

    Comment comment = Comment.builder().text("test_text_1").item(item).author(author).created(LocalDateTime.now()).build();

    @BeforeEach
    void saveUsersAndItem() {
        userRepository.save(user);
        userRepository.save(author);
        itemRepository.save(item);
    }

    @DisplayName("сохранять комментарий")
    @Test
    void save_whenSuccessInvoked_thenSavedCommentsIsReturned() {
        Comment returnedComment = commentRepository.save(comment);
        Assertions.assertEquals(comment, returnedComment);
    }

    @DisplayName("НЕ сохранять комментарий, если поле \"text\" == null")
    @Test
    void save_whenTextIsNull_thenSavedCommentsIsNotReturned() {
        comment.setText(null);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> commentRepository.save(comment));
    }

    @DisplayName("НЕ сохранять комментарий, если длина поля \"text\" > 512")
    @Test
    void save_whenTextLengthIsMoreThen512_thenSavedCommentsIsNotReturned() {
        String text = "test_text_1";
        comment.setText(text.repeat(50));
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> commentRepository.save(comment));
    }

    @DisplayName("НЕ сохранять комментарий, если поле \"created\" == null")
    @Test
    void save_whenCreatedIsNull_thenSavedCommentsIsNotReturned() {
        comment.setCreated(null);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> commentRepository.save(comment));
    }

    @DisplayName("выдавать все комментарии по полю \"item.id\"")
    @Test
    void findAllByItemId_whenSuccessInvoked_thenFoundCommentsIsReturned() {
        commentRepository.save(comment);

        List<Comment> returnedComment = commentRepository.findAllByItemId(item.getId());
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);

        Assertions.assertEquals(comments, returnedComment);
    }
}
