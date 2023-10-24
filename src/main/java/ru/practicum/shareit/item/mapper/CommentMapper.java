package ru.practicum.shareit.item.mapper;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.dto.GetCommentDto;
import ru.practicum.shareit.item.dto.GiveCommentDto;
import ru.practicum.shareit.item.dto.GiveItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CommentMapper {
    public static GetCommentDto toCommentDto(Comment comment) {
        GetCommentDto getCommentDto = GetCommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
//                .created()
                .build();
        log.info("🔀 comment: " + comment + " сконвертирован в getCommentDto: " + getCommentDto);

        return getCommentDto;
    }

    public static List<GetCommentDto> toCommentsDto(List<Comment> comments) {
        List<GetCommentDto> commentsDto = comments
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());

        log.info("🔀 список comments: " + comments + " сконвертирован в commentsDto: " + commentsDto);

        return commentsDto;
    }

    public static Comment toComment(GiveCommentDto giveCommentDto) {
        Comment comment = Comment.builder()
                .id(giveCommentDto.getId())
                .text(giveCommentDto.getText())
//                .created()
                .build();
        log.info("🔀 toComment: " + giveCommentDto + " сконвертирован в comment: " + comment);

        return comment;
    }

    public static List<Comment> toComments(List<GiveCommentDto> commentsDto) {
        List<Comment> comments = commentsDto
                .stream()
                .map(CommentMapper::toComment)
                .collect(Collectors.toList());

        log.info("🔀 список commentsDto: " + commentsDto + " сконвертирован в comments: " + comments);

        return comments;
    }
}
