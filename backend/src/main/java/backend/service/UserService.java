package backend.service;

import backend.module.dto.UserDto;

import java.util.List;

public interface UserService {
    public UserDto save(UserDto userDto);
    public List<UserDto> findAll();
    public List<UserDto> findAllByRole(String roleName);
    public UserDto findById(Integer id);
    public UserDto addRoleToUser(Integer id, String role);
    public UserDto delete(Integer id);
}
