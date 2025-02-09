package org.example.studentsvoice.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.example.studentsvoice.dto.User.UserCreationAdminRequestDTO;
import org.example.studentsvoice.dto.User.UserRequestDTO;
import org.example.studentsvoice.dto.User.UserResponseAdminDTO;
import org.example.studentsvoice.dto.User.UserResponseDTO;
import org.example.studentsvoice.entity.User;
import org.example.studentsvoice.service.UserRoleService;

import java.util.List;

@ApplicationScoped
public class UserMapper {

    @Inject
    private UserRoleService userRoleService;

    private String getRoleCode(User user) {
        return userRoleService.findByUserId(user.getId()).stream()
                .map(userRole -> userRole.getRole().getCode())
                .findFirst()
                .orElse(null);
    }

    public UserResponseDTO toUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getName())
                .role(getRoleCode(user))
                .build();
    }

    public UserResponseAdminDTO toAdminDTO(User user) {
        return UserResponseAdminDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getName())
                .role(getRoleCode(user))
                .enabled(user.isEnabled())
                .build();
    }

    public User adminRequestDTOtoEntity(UserCreationAdminRequestDTO userCreationAdminRequestDTO) {
        return new User(
                null,
                userCreationAdminRequestDTO.getUsername(),
                userCreationAdminRequestDTO.getEmail(),
                userCreationAdminRequestDTO.getName(),
                userCreationAdminRequestDTO.getPassword(),
                userCreationAdminRequestDTO.isEnabled()
        );
    }

    public User userRequestDTOtoEntity(UserRequestDTO userDTO) {
        return new User(
                null,
                userDTO.getUsername(),
                userDTO.getEmail(),
                userDTO.getName(),
                null,
                true
        );
    }

    public List<UserResponseAdminDTO> toAdminDTOList(List<User> users) {
        return users.stream()
                .map(this::toAdminDTO)
                .toList();
    }
}
