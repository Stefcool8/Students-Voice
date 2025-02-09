package org.example.studentsvoice.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.studentsvoice.dto.Role.RoleDTO;
import org.example.studentsvoice.entity.Role;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RoleMapper {
    public RoleDTO toDTO(Role entity) {
        return RoleDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .build();
    }

    public Role toEntity(RoleDTO dto) {
        return new Role(
                dto.getId(),
                dto.getCode(),
                dto.getName()
        );
    }

    public List<RoleDTO> toDTOList(List<Role> entities) {
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
