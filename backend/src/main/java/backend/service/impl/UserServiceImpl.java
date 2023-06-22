package backend.service.impl;

import backend.module.dto.RoleDto;
import backend.module.dto.UserDto;
import backend.exception.CustomRequestException;
import backend.exception.InvalidRequestException;
import backend.model.Role;
import backend.model.User;
import backend.model.UserDetail;
import backend.repository.RoleRepository;
import backend.repository.UserDetailRepository;
import backend.repository.UserRepository;
import backend.service.UserService;
import backend.util.DtoConversion;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final RoleRepository roleRepository;

    private DtoConversion dtoConversion = new DtoConversion();

    public UserServiceImpl(UserRepository userRepository, UserDetailRepository userDetailRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userDetailRepository = userDetailRepository;
        this.roleRepository = roleRepository;

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
