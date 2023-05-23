package org.jeepay.core.service;

import org.jeepay.core.entity.*;

import java.util.List;

/**
 * @author: aragom
 * @date: 18/1/2
 * @description: 系统管理接口
 */
public interface ISysService {

    int add(SysUser sysUser);

    int update(SysUser sysUser);

    SysUser findByUserId(Long userId);

    SysUser findByUserName(String userName);

    List<SysUser> select(int offset, int limit, SysUser sysUser);

    Integer count(SysUser sysUser);

    Integer batchDelete(List<Long> userIds);

    List<SysResource> selectAllResource(Byte system);

    List<SysResource> selectResourceByUserId(Long userId);

    List<SysResource> selectResource(int offset, int limit, SysResource sysResource);

    Integer countResource(SysResource sysResource);

    SysResource findResourceById(Long resourceId);

    int addResource(SysResource sysResource);

    int updateResource(SysResource sysResource);

    int batchDeleteResource(List<Long> resourceIds);

    int addRole(SysRole sysRole);

    int updateRole(SysRole sysRole);

    int batchDeleteRole(List<Long> roleIds);

    SysRole findRoleById(Long roleId);

    List<SysRole> selectRole(int offset, int limit, SysRole sysRole);

    List<SysRole> selectAllRole();

    Integer countRole(SysRole sysRole);

    List<SysPermission> selectPermissionByRoleId(Long roleId);

    List<SysUserRole> selectUserRoleByUserId(Long userId);

    /**
     * 保存角色拥有的资源
     * @param roleId
     * @param resourceIdList
     */
    void savePermission(Long roleId, List<Long> resourceIdList);

    /**
     * 保存用户拥有的角色
     * @param userId
     * @param roleIds
     */
    void saveUserRole(Long userId, List<Long> roleIds);


}
