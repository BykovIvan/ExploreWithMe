package ru.bykov.explore.utils.mapperForDto;

import ru.bykov.explore.model.Request;
import ru.bykov.explore.model.dto.RequestDto;

public class RequestMapper {

    public static RequestDto toRequestDto(Request request){
        return RequestDto.builder()
                .created(request.getCreated())
                .event(request.getEvent())
                .id(request.getId())
                .requester(request.getRequester())
                .state(request.getState())
                .build();
    }

    public static Request toRequest(RequestDto requestDto){
        return Request.builder()
                .created(requestDto.getCreated())
                .event(requestDto.getEvent())
                .id(requestDto.getId())
                .requester(requestDto.getRequester())
                .state(requestDto.getState())
                .build();
    }

}
