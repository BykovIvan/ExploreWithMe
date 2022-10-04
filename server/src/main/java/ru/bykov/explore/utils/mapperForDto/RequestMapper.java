package ru.bykov.explore.utils.mapperForDto;

import ru.bykov.explore.model.Request;
import ru.bykov.explore.model.dto.ParticipationRequestDto;

public class RequestMapper {

    public static ParticipationRequestDto toParticipationRequestDto(Request request){
        return ParticipationRequestDto.builder()
                .created(request.getCreated())
                .event(request.getEvent())
                .id(request.getId())
                .requester(request.getRequester())
                .state(request.getState())
                .build();
    }

    public static Request toRequest(ParticipationRequestDto participationRequestDto){
        return Request.builder()
                .created(participationRequestDto.getCreated())
                .event(participationRequestDto.getEvent())
                .id(participationRequestDto.getId())
                .requester(participationRequestDto.getRequester())
                .state(participationRequestDto.getState())
                .build();
    }

}
