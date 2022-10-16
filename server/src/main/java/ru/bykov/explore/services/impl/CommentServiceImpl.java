package ru.bykov.explore.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.bykov.explore.exceptions.EntityNotFoundException;
import ru.bykov.explore.exceptions.ValidationException;
import ru.bykov.explore.model.Comment;
import ru.bykov.explore.model.Event;
import ru.bykov.explore.model.User;
import ru.bykov.explore.model.dto.comment.CommentDto;
import ru.bykov.explore.model.dto.comment.NewCommentDto;
import ru.bykov.explore.model.dto.comment.UpdateCommentByAdmin;
import ru.bykov.explore.model.dto.comment.UpdateCommentDto;
import ru.bykov.explore.repositories.CommentRepository;
import ru.bykov.explore.repositories.EventRepository;
import ru.bykov.explore.repositories.UserRepository;
import ru.bykov.explore.services.CommentService;
import ru.bykov.explore.utils.CommentState;
import ru.bykov.explore.utils.EventState;
import ru.bykov.explore.utils.FromSizeSortPageable;
import ru.bykov.explore.utils.mapperForDto.CommentMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    @Override
    public CommentDto addCommentToEventFromUser(Long ownerId, Long eventId, NewCommentDto newCommentDto) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", ownerId.toString()));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        if (!event.getState().equals(EventState.PUBLISHED) || !event.getState().equals(EventState.CANCELED)) throw new ValidationException(Event.class, "state", event.getState() + "! Событие не опубликовано!");
        Comment comment = CommentMapper.toComment(newCommentDto, owner, event, LocalDateTime.now());
        if (!event.getCommentModeration()) {
            comment.setStatus(CommentState.PUBLISHED);
            return CommentMapper.toCommentDto(commentRepository.save(comment));
        }
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto updateCommentToEventFromUser(Long ownerId, Long eventId, Long comId, UpdateCommentDto updateCommentDto) {
        userRepository.findById(ownerId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", ownerId.toString()));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        if (!event.getState().equals(EventState.PUBLISHED) || !event.getState().equals(EventState.CANCELED)) throw new ValidationException(Event.class, "state", event.getState() + "! Событие не опубликовано!");
        Comment comment = commentRepository.findByIdAndOwnerIdAndEventId(comId, ownerId, eventId)
                .orElseThrow(() -> new EntityNotFoundException(Comment.class, "id", comId.toString()));
        if (updateCommentDto.getText() != null) comment.setText(updateCommentDto.getText());
        if (updateCommentDto.getStatus() != null && updateCommentDto.getStatus().equals(CommentState.CANCELED))
            comment.setStatus(CommentState.CANCELED);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto findCommentByIdFromUser(Long ownerId, Long eventId, Long comId) {
        userRepository.findById(ownerId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", ownerId.toString()));
        eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        Comment comment = commentRepository.findByIdAndOwnerIdAndEventId(comId, ownerId, eventId)
                .orElseThrow(() -> new EntityNotFoundException(Comment.class, "id", comId.toString()));
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public void deleteCommentByIdFromUser(Long ownerId, Long eventId, Long comId) {
        commentRepository.findByIdAndOwnerIdAndEventId(comId, ownerId, eventId)
                .orElseThrow(() -> new EntityNotFoundException(Comment.class, "id", comId.toString()));
        commentRepository.deleteByIdAndOwnerIdAndEventId(comId, ownerId, eventId);
    }

    @Override
    public List<CommentDto> searchCommentsByParamFromUser(Long ownerId, Long eventId, String text, Integer from, Integer size) {
        userRepository.findById(ownerId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", ownerId.toString()));
        eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        return commentRepository.findByParamFromUser(ownerId, eventId, text, FromSizeSortPageable.of(from, size, Sort.by(Sort.Direction.ASC, "id")))
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto findCommentByIdFromAdmin(Long comId) {
        return CommentMapper.toCommentDto(commentRepository.findById(comId).orElseThrow(() -> new EntityNotFoundException(Comment.class, "id", comId.toString())));
    }

    @Override
    public CommentDto updateCommentByIdFromAdmin(Long comId, UpdateCommentByAdmin updateCommentByAdmin) {
        Comment comment = commentRepository.findById(comId).orElseThrow(() -> new EntityNotFoundException(Comment.class, "id", comId.toString()));
        if (updateCommentByAdmin.getEvent() != null) {
            Event event = eventRepository.findById(updateCommentByAdmin.getEvent())
                    .orElseThrow(() -> new EntityNotFoundException(Event.class, "id", updateCommentByAdmin.getEvent().toString()));
            comment.setEvent(event);
        }
        if (updateCommentByAdmin.getOwner() != null) {
            User user = userRepository.findById(updateCommentByAdmin.getOwner())
                    .orElseThrow(() -> new EntityNotFoundException(User.class, "id", updateCommentByAdmin.getOwner().toString()));
            comment.setOwner(user);
        }
        if (updateCommentByAdmin.getText() != null) comment.setText(updateCommentByAdmin.getText());
        if (updateCommentByAdmin.getStatus() != null)
            comment.setStatus(CommentState.valueOf(updateCommentByAdmin.getStatus()));
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public void deleteCommentByIdFromAdmin(Long comId) {
        commentRepository.findById(comId).orElseThrow(() -> new EntityNotFoundException(Comment.class, "id", comId.toString()));
        commentRepository.deleteById(comId);
    }

    @Override
    public CommentDto publishCommentByIdFromAdmin(Long comId) {
        Comment comment = commentRepository.findById(comId).orElseThrow(() -> new EntityNotFoundException(Comment.class, "id", comId.toString()));
        if (comment.getStatus().equals(CommentState.CANCELED)) {
            throw new ValidationException(Event.class, "Status=", comment.getStatus() + "! Комментарий уже отменен");
        }
        comment.setStatus(CommentState.PUBLISHED);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto rejectCommentByIdFromAdmin(Long comId) {
        Comment comment = commentRepository.findById(comId).orElseThrow(() -> new EntityNotFoundException(Comment.class, "id", comId.toString()));
        comment.setStatus(CommentState.CANCELED);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }
}
