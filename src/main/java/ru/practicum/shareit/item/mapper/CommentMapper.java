package ru.practicum.shareit.item.mapper;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CommentMapper {
    public static CommentDtoOut toCommentDto(Comment comment) {
        CommentDtoOut commentDtoOut = CommentDtoOut.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();

        log.info("🔀 comment: " + comment + " сконвертирован в commentDtoOut: " + commentDtoOut);
        return commentDtoOut;
    }

    public static List<CommentDtoOut> toCommentsDto(List<Comment> comments) {
        List<CommentDtoOut> commentsDtoOut = comments
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());

        log.info("🔀 список comments: " + comments + " сконвертирован в commentsDtoOut: " + commentsDtoOut);
        return commentsDtoOut;
    }

    public static Comment toComment(CommentDtoIn commentDtoIn) {
        Comment comment = Comment.builder()
                .text(commentDtoIn.getText())
                .build();

        log.info("🔀 toComment: " + commentDtoIn + " сконвертирован в comment: " + comment);
        return comment;
    }

    public static List<Comment> toComments(List<CommentDtoIn> commentsDto) {
        List<Comment> comments = commentsDto
                .stream()
                .map(CommentMapper::toComment)
                .collect(Collectors.toList());

        log.info("🔀 список commentsDto: " + commentsDto + " сконвертирован в comments: " + comments);
        return comments;
    }
}
