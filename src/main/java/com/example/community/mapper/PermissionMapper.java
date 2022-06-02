package com.example.community.mapper;

import com.example.community.model.Permission;
import com.example.community.model.PermissionExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface PermissionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table permission
     *
     * @mbg.generated Wed Apr 20 21:23:57 EDT 2022
     */
    long countByExample(PermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table permission
     *
     * @mbg.generated Wed Apr 20 21:23:57 EDT 2022
     */
    int deleteByExample(PermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table permission
     *
     * @mbg.generated Wed Apr 20 21:23:57 EDT 2022
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table permission
     *
     * @mbg.generated Wed Apr 20 21:23:57 EDT 2022
     */
    int insert(Permission row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table permission
     *
     * @mbg.generated Wed Apr 20 21:23:57 EDT 2022
     */
    int insertSelective(Permission row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table permission
     *
     * @mbg.generated Wed Apr 20 21:23:57 EDT 2022
     */
    List<Permission> selectByExampleWithRowbounds(PermissionExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table permission
     *
     * @mbg.generated Wed Apr 20 21:23:57 EDT 2022
     */
    List<Permission> selectByExample(PermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table permission
     *
     * @mbg.generated Wed Apr 20 21:23:57 EDT 2022
     */
    Permission selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table permission
     *
     * @mbg.generated Wed Apr 20 21:23:57 EDT 2022
     */
    int updateByExampleSelective(@Param("row") Permission row, @Param("example") PermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table permission
     *
     * @mbg.generated Wed Apr 20 21:23:57 EDT 2022
     */
    int updateByExample(@Param("row") Permission row, @Param("example") PermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table permission
     *
     * @mbg.generated Wed Apr 20 21:23:57 EDT 2022
     */
    int updateByPrimaryKeySelective(Permission row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table permission
     *
     * @mbg.generated Wed Apr 20 21:23:57 EDT 2022
     */
    int updateByPrimaryKey(Permission row);
}