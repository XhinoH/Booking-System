package backend.util;

import backend.model.dto.RoleDto;
import backend.model.dto.UserDetailDto;
import backend.model.dto.UserDto;
import backend.model.entity.Role;
import backend.model.entity.User;
import backend.model.entity.UserDetail;


public class DtoConversion {

    public UserDto convertUser(User user) {
        UserDto userDto = new UserDto();

        if (user.getId() != null) {
            userDto.setId(user.getId());
        }

        if (user.getUsername() != null){
            userDto.setUsername(user.getUsername());
        }

        return userDto;
    }



    public UserDetailDto convertUserDetail(UserDetail userDetail) {
        UserDetailDto userDetailDto = new UserDetailDto();
        if (userDetail.getId() != null){
            userDetailDto.setId(userDetail.getId());
        }

        if (userDetail.getFirstName() != null){
            userDetailDto.setFirstName(userDetail.getFirstName());
        }

        if (userDetail.getLastName() != null){
            userDetailDto.setLastName(userDetail.getLastName());
        }

        if (userDetail.getEmail() != null){
            userDetailDto.setEmail(userDetail.getEmail());
        }

        if (userDetail.getPhoneNumber() != null){
            userDetailDto.setPhoneNumber(userDetail.getPhoneNumber());
        }

        if (userDetail.getAddress() != null){
            userDetailDto.setAddress(userDetail.getAddress());
        }

        return userDetailDto;
    }

    public RoleDto convertRole(Role role) {
        RoleDto roleDto = new RoleDto();

        if (role.getId() != null){
            roleDto.setId(role.getId());
        }

        return roleDto;
    }

}
