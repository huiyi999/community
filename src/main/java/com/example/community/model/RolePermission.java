package com.example.community.model;

public class RolePermission {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column role_permission.id
     *
     * @mbg.generated Wed Apr 20 21:23:57 EDT 2022
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column role_permission.role_id
     *
     * @mbg.generated Wed Apr 20 21:23:57 EDT 2022
     */
    private Long roleId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column role_permission.permission_id
     *
     * @mbg.generated Wed Apr 20 21:23:57 EDT 2022
     */
    private Long permissionId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column role_permission.id
     *
     * @return the value of role_permission.id
     *
     * @mbg.generated Wed Apr 20 21:23:57 EDT 2022
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column role_permission.id
     *
     * @param id the value for role_permission.id
     *
     * @mbg.generated Wed Apr 20 21:23:57 EDT 2022
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column role_permission.role_id
     *
     * @return the value of role_permission.role_id
     *
     * @mbg.generated Wed Apr 20 21:23:57 EDT 2022
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column role_permission.role_id
     *
     * @param roleId the value for role_permission.role_id
     *
     * @mbg.generated Wed Apr 20 21:23:57 EDT 2022
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column role_permission.permission_id
     *
     * @return the value of role_permission.permission_id
     *
     * @mbg.generated Wed Apr 20 21:23:57 EDT 2022
     */
    public Long getPermissionId() {
        return permissionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column role_permission.permission_id
     *
     * @param permissionId the value for role_permission.permission_id
     *
     * @mbg.generated Wed Apr 20 21:23:57 EDT 2022
     */
    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }
}