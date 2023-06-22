package backend.service.impl;

import backend.dto.ClientDto;
import backend.dto.RoleDto;
import backend.dto.UserDto;
import backend.exception.CustomRequestException;
import backend.exception.InvalidRequestException;
import backend.model.Restaurant;
import backend.model.Role;
import backend.model.User;
import backend.model.UserDetail;
import backend.repository.RestaurantRepository;
import backend.repository.RoleRepository;
import backend.repository.UserDetailRepository;
import backend.repository.UserRepository;
import backend.service.RestaurantService;
import backend.service.UserService;
import backend.util.DtoConversion;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final RoleRepository roleRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantService restaurantService;
    private DtoConversion dtoConversion = new DtoConversion();

    public UserServiceImpl(UserRepository userRepository, UserDetailRepository userDetailRepository, RoleRepository roleRepository, RestaurantRepository restaurantRepository, RestaurantService restaurantService) {
        this.userRepository = userRepository;
        this.userDetailRepository = userDetailRepository;
        this.roleRepository = roleRepository;
        this.restaurantRepository = restaurantRepository;
        this.restaurantService = restaurantService;
    }


    // ADMIN: Saving or updating a user
    @Override
    public UserDto save(UserDto userDto) {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user;
        UserDetail userDetail;
        Set<Role> roles = new HashSet<>();
        Boolean isUpdating;

        // Updating the user if the userDto has an id
        if (userDto.getId() != null) {
            Optional<User> userOptional = userRepository.findByIdAndIsDeleted(userDto.getId(), false);
            if (userOptional.isPresent()) {
                user = userOptional.get();
                isUpdating = true;
            } else {
                logger.error("User not found");
                throw new NullPointerException("User not found");
            }
            Optional<UserDetail> userDetailOptional = userDetailRepository.findByUserId(user.getId());
            if (userDetailOptional.isPresent()){
                userDetail = userDetailOptional.get();
            }else {
                userDetail = new UserDetail();
            }

        } else {
            user = new User();
            userDetail = new UserDetail();
            isUpdating = false;
        }

        // Validating the username
        if ((!usernameExists(userDto.getUsername())
                || userDto.getUsername() == user.getUsername())
                && userDto.getUsername() != null) {
            user.setUsername(userDto.getUsername());
        } else {
            throw new CustomRequestException("Username exists or is null");
        }

        if (userDto.getPassword() != null){
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        } else {
            throw new InvalidRequestException("Password is invalid");
        }

        if (userDto.getUserDetail() != null) {
            if (userDto.getUserDetail().getFirstName() == null){
                throw new InvalidRequestException("First name is invalid");
            }
            if (userDto.getUserDetail().getLastName() == null){
                throw new InvalidRequestException("Last name is invalid");
            }
            if (userDto.getUserDetail().getEmail() == null){
                throw new InvalidRequestException("Email is invalid");
            }
            if (userDto.getUserDetail().getPhoneNumber() == null){
                throw new InvalidRequestException("Phone number is invalid");
            }
            if (userDto.getUserDetail().getAddress() == null){
                throw new InvalidRequestException("Address is invalid");
            }

            userDetail.setFirstName(userDto.getUserDetail().getFirstName());
            userDetail.setLastName(userDto.getUserDetail().getLastName());
            userDetail.setEmail(userDto.getUserDetail().getEmail());
            userDetail.setPhoneNumber(userDto.getUserDetail().getPhoneNumber());
            userDetail.setAddress(userDto.getUserDetail().getAddress());
            userDetail.setUser(user);
        } else {
            throw new InvalidRequestException("User details are invalid");
        }

        user.setUserDetail(userDetail);

        if (userDto.getRoles() != null) {

            addRoles(roles, userDto.getRoles());
            user.setRoles(roles);

            // Checking if the userDto has the role MANAGER and assigning the manager to a restaurant
            if (isManager(userDto) == true && isUpdating == false) {
                if (userDto.getRestaurantId() != null) {
                    Optional<Restaurant> restaurantOptional = restaurantRepository.findByIdAndIsDeleted(userDto.getRestaurantId(), false);
                    if (restaurantOptional.isPresent() && restaurantOptional.get().getManager() == null) {
                        Restaurant restaurant = restaurantOptional.get();
                        userRepository.save(user);
                        restaurant.setManager(user);
                        restaurantService.save(dtoConversion.convertRestaurant(restaurant));

                        user.setRestaurant(restaurant);
                        System.out.println(restaurant.getName());
                    } else {
                        throw new NullPointerException("Restaurant not found or restaurant already has a manager");
                    }
                } else {
                    throw new InvalidRequestException("Restaurant id is invalid");
                }
            }
        } else {

            // Adding role CLIENT to the user if the userDto doesn't have any roles
            Optional<Role> roleOptional = roleRepository.findByName("ROLE_CLIENT");
            if (roleOptional.isPresent()){
                Role client = roleOptional.get();
                roles.add(client);
                user.setRoles(roles);
            }else {
                throw new NullPointerException("Role not found");
            }
        }

        logger.info("Saved user with username: " + user.getUsername());
        return dtoConversion.convertUser(userRepository.save(user));
    }

    // CLIENT: Saving or updating a client
    @Override
    public ClientDto saveClient(ClientDto clientDto, String clientUsername) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user;
        UserDetail userDetail;
        Set<Role> roles = new HashSet<>();

        // Updating the client if the clientDto has an id
        if (clientDto.getId() != null && clientUsername != null) {
            Optional<User> userOptional = userRepository.findByIdAndIsDeleted(clientDto.getId(), false);
            if (userOptional.isPresent() &&
                    userRepository.findByUsernameAndIsDeleted(clientUsername, false).get().getId() == userOptional.get().getId()) {
                user = userOptional.get();
            } else {
                logger.error("User not found");
                throw new NullPointerException("User not found");
            }
            Optional<UserDetail> userDetailOptional = userDetailRepository.findByUserId(user.getId());
            if (userDetailOptional.isPresent()){
                userDetail = userDetailOptional.get();
            }else {
                userDetail = new UserDetail();
            }

        } else {
            user = new User();
            userDetail = new UserDetail();
        }

        // Validating the username
        if ((!usernameExists(clientDto.getUsername())
                || clientDto.getUsername() == user.getUsername())
                && clientDto.getUsername() != null) {
            user.setUsername(clientDto.getUsername());
        } else {
            throw new CustomRequestException("Username exists or is null");
        }

        if (clientDto.getPassword() != null){
            user.setPassword(passwordEncoder.encode(clientDto.getPassword()));
        } else {
            throw new InvalidRequestException("Password is invalid");
        }

        if (clientDto.getUserDetail() != null) {

            if (clientDto.getUserDetail().getFirstName() == null){
                throw new InvalidRequestException("First name is invalid");
            }
            if (clientDto.getUserDetail().getLastName() == null){
                throw new InvalidRequestException("Last name is invalid");
            }
            if (clientDto.getUserDetail().getEmail() == null){
                throw new InvalidRequestException("Email is invalid");
            }
            if (clientDto.getUserDetail().getPhoneNumber() == null){
                throw new InvalidRequestException("Phone number is invalid");
            }
            if (clientDto.getUserDetail().getAddress() == null){
                throw new InvalidRequestException("Address is invalid");
            }

            userDetail.setFirstName(clientDto.getUserDetail().getFirstName());
            userDetail.setLastName(clientDto.getUserDetail().getLastName());
            userDetail.setEmail(clientDto.getUserDetail().getEmail());
            userDetail.setPhoneNumber(clientDto.getUserDetail().getPhoneNumber());
            userDetail.setAddress(clientDto.getUserDetail().getAddress());
            userDetail.setUser(user);
        } else {
            throw new InvalidRequestException("User details are invalid");
        }

        user.setUserDetail(userDetail);

        Optional<Role> roleOptional = roleRepository.findByName("ROLE_CLIENT");
        if (roleOptional.isPresent()){
            Role client = roleOptional.get();
            roles.add(client);
            user.setRoles(roles);
        }else {
            throw new NullPointerException("Role not found");
        }

        logger.info("Saved user with username: " + user.getUsername());
        return dtoConversion.convertClient(userRepository.save(user));
    }


    // Finding all the users except other admins
    @Override
    public List<UserDto> findAll() {
        return userRepository.findAllWithoutAdmin().stream()
                .map(dtoConversion::convertUser).collect(Collectors.toList());
    }

    // Finding all the users by role except the ones with role admin
    @Override
    public List<UserDto> findAllByRole(String roleName) {
        if (!roleName.equalsIgnoreCase("role_admin")){
            return userRepository.findAllByRole(roleName).stream()
                    .map(dtoConversion::convertUser).collect(Collectors.toList());
        }else {
            throw new CustomRequestException("You can not view other admins");
        }

    }

    // Finding all the users by id except other admin users
    @Override
    public UserDto findById(Integer userId) {
        Optional<User> userOptional = userRepository.findByIdAndIsDeleted(userId, false);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            for (Role role : user.getRoles()){
                if (role.getName().equalsIgnoreCase("role_admin")){
                    throw new CustomRequestException("You can not view other admins info");
                }
            }
            return dtoConversion.convertUser(user);
        } else {
            throw new NullPointerException("User not found");
        }
    }

    // Adding a role to a user
    @Override
    public UserDto addRoleToUser(Integer id, String roleName) {
        Optional<User> userOptional = userRepository.findByIdAndIsDeleted(id, false);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            Optional<Role> roleOptional = roleRepository.findByName(roleName);

            if(roleOptional.isPresent()){
                Role role = roleOptional.get();
                Set<Role> roleSet = user.getRoles();
                roleSet.add(role);
                user.setRoles(roleSet);

                logger.info("Added role " + role.getName() + " to user " + user.getUsername());

                return dtoConversion.convertUser(userRepository.save(user));
            } else {
                throw new NullPointerException("Role not found");
            }
        } else {
            throw new NullPointerException("User not found");
        }
    }

    // Deleting a user
    @Override
    public UserDto delete(Integer userId) {
        Optional<User> userOptional = userRepository.findByIdAndIsDeleted(userId, false);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            user.setDeleted(true);
            logger.info("Deleted user with username: " + user.getUsername());
            return dtoConversion.convertUser(userRepository.save(user));
        } else {
            logger.error("User with id: " + userId + " not found");
            throw new NullPointerException("User not found");
        }
    }


    // Checking if a user with the given username exists
    public  Boolean usernameExists(String username) {
        if (userRepository.findByUsernameAndIsDeleted(username, false).isPresent()) {
            return true;
        }
        return false;
    }

    // Checking if a UserDto has ROLE_MANAGER
    public Boolean isManager(UserDto userDto) {
        if (userDto.getRoles() != null) {
            for (RoleDto role : userDto.getRoles()) {
                if (role.getName().contains("ROLE_MANAGER")) {
                    return true;
                }
            }
        }
        return false;
    }

    // Adding roles from a RoleDto set to a Role set
    public void addRoles(Set<Role> roleList, Set<RoleDto> roleDtoList) {
        for (RoleDto roleDto : roleDtoList) {
            Optional<Role> roleOptional = roleRepository.findByName(roleDto.getName());
            if(roleOptional.isPresent()){
                Role role = roleOptional.get();
                roleList.add(role);
            }
        }
    }

}
