package backend.service;

import backend.dto.ClientDto;
import backend.dto.UserDto;

import java.util.List;

public interface UserService {
    public UserDto save(UserDto userDto);
    public ClientDto saveClient(ClientDto clientDto, String clientUsername);
    public List<UserDto> findAll();
    public List<UserDto> findAllByRole(String roleName);
    public UserDto findById(Integer id);
    public UserDto addRoleToUser(Integer id, String role);
    public UserDto delete(Integer id);
}
