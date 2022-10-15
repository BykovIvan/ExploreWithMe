package ru.bykov.explore.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bykov.explore.exceptions.EntityNotFoundException;
import ru.bykov.explore.model.Event;
import ru.bykov.explore.model.User;
import ru.bykov.explore.model.dto.comment.CommentDto;
import ru.bykov.explore.model.dto.comment.NewCommentDto;
import ru.bykov.explore.repositories.CommentRepository;
import ru.bykov.explore.repositories.EventRepository;
import ru.bykov.explore.repositories.UserRepository;
import ru.bykov.explore.services.CommentService;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    @Override
    public CommentDto addCommentToEventFromUser(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        if (event.getCommentModeration()){

        }

        return null;
    }
}
