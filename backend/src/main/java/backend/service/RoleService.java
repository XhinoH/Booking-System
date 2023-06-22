package backend.service;

import backend.module.dto.RoleDto;

import java.util.List;

public interface RoleService {

    public RoleDto save(RoleDto roleDto);

    public List<RoleDto> findAll();

    public RoleDto findById(Integer id);

}
