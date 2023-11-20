package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass

@Slf4j
public class CommentMapper {
    public CommentDtoOut toCommentDtoOut(Comment comment) {
        CommentDtoOut commentDtoOut = CommentDtoOut.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();

        log.info("🔀 comment: " + comment + " сконвертирован в commentDtoOut: " + commentDtoOut);
        return commentDtoOut;
    }

    public List<CommentDtoOut> toCommentsDtoOut(List<Comment> comments) {
        List<CommentDtoOut> commentsDtoOut = comments
                .stream()
                .map(CommentMapper::toCommentDtoOut)
                .collect(Collectors.toList());

        log.info("🔀 список comments: " + comments + " сконвертирован в commentsDtoOut: " + commentsDtoOut);
        return commentsDtoOut;
    }

    public Comment toComment(CommentDtoIn commentDtoIn) {
        Comment comment = Comment.builder()
                .text(commentDtoIn.getText())
                .build();

        log.info("🔀 toComment: " + commentDtoIn + " сконвертирован в comment: " + comment);
        return comment;
    }

    public List<Comment> toComments(List<CommentDtoIn> commentsDto) {
        List<Comment> comments = commentsDto
                .stream()
                .map(CommentMapper::toComment)
                .collect(Collectors.toList());

        log.info("🔀 список commentsDto: " + commentsDto + " сконвертирован в comments: " + comments);
        return comments;
    }

    public CommentDtoIn toCommentDtoIn(Comment comment) {
        CommentDtoIn commentDtoIn = CommentDtoIn.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .build();

        log.info("🔀 comment: " + comment + " сконвертирован в commentDtoIn: " + commentDtoIn);
        return commentDtoIn;
    }
}
