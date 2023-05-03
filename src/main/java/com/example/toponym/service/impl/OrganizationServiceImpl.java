package com.example.toponym.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.toponym.entity.bean.Organization;
import com.example.toponym.entity.bean.User;
import com.example.toponym.entity.bean.UserDetailsInfo;
import com.example.toponym.entity.constant.YesOrNoConstant;
import com.example.toponym.entity.dto.OrganizationDTO;
import com.example.toponym.exception.ServiceException;
import com.example.toponym.mapper.OrganizationMapper;
import com.example.toponym.service.OrganizationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 杨宇帆
 * @since 2023-04-14
 */
@Service
@Slf4j
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements
                                                                                           OrganizationService {

    @Override
    public OrganizationDTO addOrganization(OrganizationDTO organizationDTO) throws Exception {
        checkParam(organizationDTO);

        Organization parentOrganization = super.getById(organizationDTO.getParentId());
        if (parentOrganization == null) {
            throw new ServiceException("父节点不存在");
        }
        copyParentInfo(parentOrganization, organizationDTO);
        organizationDTO.setCreateUser(
                (((UserDetailsInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser()
                        .getId()));
        organizationDTO.setCreateTime(new Date());

        Organization organization = BeanUtil.copyProperties(organizationDTO, Organization.class);
        super.save(organization);
        organizationDTO.setId(organization.getId());
        return organizationDTO;
    }

    private void copyParentInfo(Organization parentOrganization, OrganizationDTO organizationDTO) throws Exception {
        organizationDTO.setOrganizationLevel(parentOrganization.getOrganizationLevel() + 1);
        organizationDTO.setTreeList(parentOrganization.getTreeList() + "," + parentOrganization.getId());
        organizationDTO.setUpdateTime(new Date());
        organizationDTO.setUpdateUser(
                ((UserDetailsInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser()
                        .getId());
        organizationDTO.setOrganizationType(parentOrganization.getOrganizationType());
    }

    private void checkParam(OrganizationDTO organizationDTO) throws Exception {
        if (organizationDTO == null) {
            throw new ServiceException("入参不能为空");
        }
        if (StringUtils.isEmpty(organizationDTO.getOrganizationName())) {
            throw new ServiceException("机构名字不能为空");
        }
        if (organizationDTO.getParentId() == null) {
            throw new ServiceException("父节点不能为空");
        }
    }

    @Override
    public OrganizationDTO updateOrganization(OrganizationDTO organizationDTO) throws Exception {
        checkParam(organizationDTO);
        if (organizationDTO.getId() == null) {
            throw new ServiceException("更新必须携带id");
        }

        Organization parentOrganization = super.getById(organizationDTO.getParentId());
        if (parentOrganization == null) {
            throw new ServiceException("父节点不存在");
        }

        if (!parentOrganization.getParentId().equals(organizationDTO.getParentId())) {
            throw new ServiceException("禁止更新节点层级");
        }

        copyParentInfo(parentOrganization, organizationDTO);

        super.updateById(BeanUtil.copyProperties(organizationDTO, Organization.class));

        return organizationDTO;
    }

    @Override
    public void removeOrganization(Long id) throws Exception {
        Organization organization = super.getById(id);
        if (organization == null) {
            throw new ServiceException("该节点不存在");
        }
        List<Organization> list = super.list(new LambdaQueryWrapper<Organization>().eq(Organization::getParentId, id));
        if (!CollectionUtil.isEmpty(list)) {
            throw new ServiceException("该节点下含有子节点,无法删除");
        }
        super.removeById(id);
    }

    @Override
    public List<OrganizationDTO> getOrganizationTree() throws Exception {
        List<Organization> list = super.list(
                new LambdaQueryWrapper<Organization>().orderByAsc(Organization::getCreateTime));
        //分组根据父级
        Map<Long, List<OrganizationDTO>> map = list.stream()
                .map(org -> BeanUtil.copyProperties(org, OrganizationDTO.class))
                .filter(org -> !YesOrNoConstant.YES.equals(org.getOrganizationLevel()))
                .collect(Collectors.groupingBy(OrganizationDTO::getParentId));
        //获取最顶级
        List<OrganizationDTO> parentOrganization = list.stream()
                .filter(org -> YesOrNoConstant.YES.equals(org.getOrganizationLevel()))
                .map(org -> BeanUtil.copyProperties(org, OrganizationDTO.class))
                .collect(Collectors.toList());
        List<OrganizationDTO> tree = parentOrganization;
        while (!CollectionUtil.isEmpty(parentOrganization)) {
            List<OrganizationDTO> childOrganizationDTO = new ArrayList<>();
            for (OrganizationDTO parent : parentOrganization) {
                List<OrganizationDTO> childList = map.get(parent.getId());
                if(CollectionUtil.isEmpty(childList)){
                    continue;
                }
                parent.setChild(childList);
                childOrganizationDTO.addAll(childList);
            }
            parentOrganization = childOrganizationDTO;
        }
        return tree;
    }
}
