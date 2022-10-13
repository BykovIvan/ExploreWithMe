package ru.bykov.explore.utils.mapperForDto;

import ru.bykov.explore.model.Request;
import ru.bykov.explore.model.dto.ParticipationRequestDto;
import ru.bykov.explore.utils.StateOfEventAndReq;

public class RequestMapper {

    public static ParticipationRequestDto toParticipationRequestDto(Request request){
        return ParticipationRequestDto.builder()
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .status(String.valueOf(request.getStatus()))
                .build();
    }

//    public static Request toRequest(ParticipationRequestDto participationRequestDto){
//        return Request.builder()
//                .created(participationRequestDto.getCreated())
//                .event(participationRequestDto.getEvent())
//                .id(participationRequestDto.getId())
//                .requester(participationRequestDto.getRequester())
//                .status(StateOfEventAndReq.valueOf(participationRequestDto.getStatus()))
//                .build();
//    }

}
