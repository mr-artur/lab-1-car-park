package ua.kpi.fict.carpark.mapper.assignment;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import ua.kpi.fict.carpark.dto.response.AssignmentDto;
import ua.kpi.fict.carpark.entity.Assignment;
import ua.kpi.fict.carpark.mapper.LocalizedMapper;

@RequiredArgsConstructor
@Component
public class LocalizedAssignmentMapper extends LocalizedMapper<Assignment, AssignmentDto> {

    private final AssignmentMapper mapper;

    @Override
    protected AssignmentDto toDtoWithDefaultData(Assignment assignment) {
        return mapper.toAssignmentDto(assignment);
    }

    @Override
    protected AssignmentDto toDtoWithNativeData(Assignment assignment) {
        return mapper.toAssignmentDtoWithNativeData(assignment);
    }
}
