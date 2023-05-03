package com.example.toponym.service;

import com.example.toponym.entity.bean.Organization;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.toponym.entity.dto.OrganizationDTO;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 杨宇帆
 * @since 2023-04-14
 */
public interface OrganizationService extends IService<Organization> {

    /**
     * 新增组织
     * @param organizationDTO
     * @return
     * @throws Exception
     */
    OrganizationDTO addOrganization(OrganizationDTO organizationDTO) throws Exception;

    /**
     * 更新组织
     * @param organizationDTO
     * @return
     * @throws Exception
     */
    OrganizationDTO updateOrganization(OrganizationDTO organizationDTO) throws Exception;

    /**
     * 删除组织
     * @param id
     * @throws Exception
     */
    void removeOrganization(Long id) throws Exception;

    /**
     * 获取组织树
     * @return
     * @throws Exception
     */
    List<OrganizationDTO> getOrganizationTree() throws Exception;
}
