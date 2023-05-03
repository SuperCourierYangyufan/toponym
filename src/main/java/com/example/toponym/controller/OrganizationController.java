package com.example.toponym.controller;


import com.example.toponym.entity.dto.OrganizationDTO;
import com.example.toponym.service.OrganizationService;
import com.example.toponym.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 杨宇帆
 * @since 2023-04-14
 */
@Api(tags = "组织信息")
@RestController
@Slf4j
@RequestMapping("/organization")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    /**
     * 获取组织树
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取组织树", notes = "组织树信息")
    @GetMapping("/getOrganizationTree")
    public R getOrganizationTree() throws Exception {
        return R.data(organizationService.getOrganizationTree());
    }

    /**
     * 新增组织
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "新增组织", notes = "新增组织信息")
    @PostMapping("/addOrganization")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "organizationName",value = "机构名字",required = true,paramType = "body"),
            @ApiImplicitParam(name = "parentId",value = "父机构id",required = true,paramType = "body"),
            @ApiImplicitParam(name = "administrativeDivisions",value = "行政区划",required = false,paramType = "body"),
    })
    public R addOrganization(@RequestBody OrganizationDTO organizationDTO) throws Exception {
        return R.data(organizationService.addOrganization(organizationDTO));
    }


    /**
     * 更新组织
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "更新组织", notes = "更新组织信息")
    @PostMapping("/updateOrganization")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id",value = "主键",required = true,paramType = "body"),
            @ApiImplicitParam(name = "organizationName",value = "机构名字",required = true),
            @ApiImplicitParam(name = "parentId",value = "父机构id",required = true,paramType = "body"),
            @ApiImplicitParam(name = "administrativeDivisions",value = "行政区划",required = false,paramType = "body"),
    })
    public R updateOrganization(@RequestBody OrganizationDTO organizationDTO) throws Exception {
        return R.data(organizationService.updateOrganization(organizationDTO));
    }

    /**
     * 删除组织
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "删除组织", notes = "删除组织信息")
    @GetMapping("/removeOrganization")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id",value = "主键",required = true,paramType = "param"),
    })
    public R removeOrganization(Long id) throws Exception {
        organizationService.removeOrganization(id);
        return R.success();
    }
}

