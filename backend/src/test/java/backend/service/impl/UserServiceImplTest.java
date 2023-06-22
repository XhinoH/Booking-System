package backend.service.impl;

import backend.dto.UserDetailDto;
import backend.dto.UserDto;
import backend.model.Role;
import backend.model.User;
import backend.model.UserDetail;
import backend.repository.RestaurantRepository;
import backend.repository.RoleRepository;
import backend.repository.UserDetailRepository;
import backend.repository.UserRepository;
import backend.service.RestaurantService;
import backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;

class UserServiceImplTest {

    private UserService userService;
    private UserRepository userRepository;
    private UserDetailRepository userDetailRepository;
    private RoleRepository roleRepository;
    private RestaurantRepository restaurantRepository;
    private RestaurantService restaurantService;

    @BeforeEach
    void setUp() {

        userRepository = Mockito.mock(UserRepository.class);
        userDetailRepository = Mockito.mock(UserDetailRepository.class);
        roleRepository = Mockito.mock(RoleRepository.class);
        restaurantRepository = Mockito.mock(RestaurantRepository.class);
        restaurantService = Mockito.mock(RestaurantService.class);

        userService = new UserServiceImpl(userRepository, userDetailRepository, roleRepository, restaurantRepository,restaurantService);

    }

    @Test
    void update_client_returns_dto_when_user_upadted() {

        UserDetailDto userDetailDto = new UserDetailDto();
        userDetailDto.setFirstName("test");
        userDetailDto.setAddress("test");
        userDetailDto.setLastName("test");
        userDetailDto.setEmail("test");
        userDetailDto.setPhoneNumber("test");

        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("test");
        userDto.setPassword("test");
        userDto.setUserDetail(userDetailDto);
        userDto.setRoles(null);

        User userFromDb = new User();
        userFromDb.setId(1);
        userFromDb.setUsername("username");
        userFromDb.setPassword("pass");
        userFromDb.setUserDetail(new UserDetail());

        Role roleClient = new Role();
        roleClient.setName("ROLE_CLIENT");

        Mockito.when(userRepository.findByIdAndIsDeleted(any(), any())).thenReturn(Optional.of(userFromDb));
        Mockito.when(userDetailRepository.findByUserId(any())).thenReturn(Optional.of(userFromDb.getUserDetail()));
        Mockito.when(userRepository.findByUsernameAndIsDeleted(any(), any())).thenReturn(Optional.empty());
        Mockito.when(roleRepository.findByName("ROLE_CLIENT")).thenReturn(Optional.of(roleClient));
        Mockito.when(userRepository.save(any())).thenReturn(userFromDb);

        UserDto updatedUser = userService.save(userDto);

        assertEquals(userDto.getUsername(), updatedUser.getUsername());

    }

    @Test
    void update_throws_error_when_id_invalid(){
        Boolean throwed = null;

        UserDto userDto = new UserDto();
        userDto.setId(1);

        Mockito.when(userRepository.findByIdAndIsDeleted(any(), any())).thenReturn(Optional.empty());

        try {
            UserDto updatedUser = userService.save(userDto);
        } catch (RuntimeException e){
            throwed = true;
        }

        assertTrue(throwed);

    }

    @Test
    void update_throws_error_when_username_exists(){
        Boolean throwed = null;

        UserDto userDto = new UserDto();
        userDto.setId(1);

        User user = new User();
        user.setId(1);

        Mockito.when(userRepository.findByIdAndIsDeleted(any(), any())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByUsernameAndIsDeleted(any(), any())).thenReturn(Optional.of(new User()));

        try {
            UserDto updatedUser = userService.save(userDto);
        } catch (RuntimeException e){
            throwed = true;
        }

        assertTrue(throwed);
    }
}