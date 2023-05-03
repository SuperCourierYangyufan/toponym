package com.example.toponym.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.PhoneUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.toponym.entity.bean.Menu;
import com.example.toponym.entity.bean.Organization;
import com.example.toponym.entity.bean.Role;
import com.example.toponym.entity.bean.RoleMenu;
import com.example.toponym.entity.bean.User;
import com.example.toponym.entity.bean.UserDetailsInfo;
import com.example.toponym.entity.bean.UserRole;
import com.example.toponym.entity.constant.OtherInfoConstant;
import com.example.toponym.entity.constant.RedisConstant;
import com.example.toponym.entity.constant.YesOrNoConstant;
import com.example.toponym.entity.dto.MenuDTO;
import com.example.toponym.entity.dto.RoleDTO;
import com.example.toponym.entity.dto.UserDTO;
import com.example.toponym.entity.query.UserQuery;
import com.example.toponym.exception.ServiceException;
import com.example.toponym.mapper.UserMapper;
import com.example.toponym.service.MenuService;
import com.example.toponym.service.OperateLogService;
import com.example.toponym.service.OrganizationService;
import com.example.toponym.service.RoleMenuService;
import com.example.toponym.service.RoleService;
import com.example.toponym.service.UserRoleService;
import com.example.toponym.service.UserService;
import com.example.toponym.utils.JwtUtil;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 杨宇帆
 * @since 2023-04-13
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OrganizationService organizationService;

    @Value("${user.default.password:12345!@#$%}")
    private String defaultPassword;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OperateLogService operateLogService;

    @Override
    public String login(UserDTO userDTO, HttpServletRequest request) throws Exception {
        if (userDTO == null || StringUtils.isEmpty(userDTO.getAccount()) || StringUtils.isEmpty(
                userDTO.getPassword())) {
            throw new ServiceException("登录必须携带用户名和密码");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDTO.getAccount(), userDTO.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (authenticate == null) {
            throw new ServiceException("登录失败");
        }
        // 存储redis
        UserDetailsInfo userDetailsInfo = (UserDetailsInfo) authenticate.getPrincipal();
        UserDTO user = userDetailsInfo.getUser();
        //填充角色信息
        fillUserRole(user);
        //菜单信息
        fillRoleMenu(user);
        //存储redis
        redisTemplate.opsForValue().set(RedisConstant.LOGIN_USER + userDetailsInfo.getUser().getId(), user,
                                        OtherInfoConstant.CACHE_USER_TOKEN_TIME, TimeUnit.MINUTES);
        // 生成jwt
        String jwtToken = JwtUtil.getJwtToken(userDetailsInfo.getUser().getId().toString(),
                                              userDetailsInfo.getUsername(), new HashMap<>());
        //记录日志
        operateLogService.addLoginLog(user, request);
        log.info("用户:{}登录系统成功,token:{}", userDTO.getUserName(), jwtToken);
        return jwtToken;
    }

    private void fillRoleMenu(UserDTO user) throws Exception {
        if (!CollectionUtil.isEmpty(user.getRoleList())) {
            List<RoleMenu> roleMenuList = roleMenuService.list(
                    new LambdaQueryWrapper<RoleMenu>().in(RoleMenu::getRoleId,
                                                          user.getRoleList().stream()
                                                                  .map(RoleDTO::getId)
                                                                  .collect(
                                                                          Collectors.toList())));
            if (!CollectionUtil.isEmpty(roleMenuList)) {
                List<Menu> menuList = menuService.listByIds(
                        roleMenuList.stream().map(RoleMenu::getMenuId).collect(Collectors.toSet()));
                if (!CollectionUtil.isEmpty(menuList)) {
                    List<MenuDTO> menuDTOList = BeanUtil.copyToList(menuList, MenuDTO.class);
                    user.setMenuList(menuService.changeTree(menuDTOList));
                }
            }
        }
    }

    private void fillUserRole(UserDTO user) throws Exception {
        List<UserRole> userRoleList = userRoleService.list(
                new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, user.getId()));
        if (!CollectionUtil.isEmpty(userRoleList)) {
            List<Role> roleList = roleService.listByIds(
                    userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toList()));
            if (!CollectionUtil.isEmpty(roleList)) {
                List<RoleDTO> roleDTOList = BeanUtil.copyToList(roleList, RoleDTO.class);
                user.setRoleList(roleDTOList);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = {ServiceException.class, Exception.class})
    public Long addUser(UserDTO userDTO) throws Exception {
        checkUser(userDTO);

        Organization organization = organizationService.getById(userDTO.getOrganizationId());
        if (organization == null) {
            throw new ServiceException("所属机构不存在");
        }

        int count = super.count(new LambdaQueryWrapper<User>().eq(User::getAccount, userDTO.getAccount()));
        if (count > 0) {
            throw new ServiceException("该账号已存在");
        }

        //用户信息
        userDTO.setOrganizationName(organization.getOrganizationName());
        userDTO.setCreateTime(new Date());
        userDTO.setUpdateTime(new Date());
        UserDTO currentUser = ((UserDetailsInfo) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).getUser();
        userDTO.setCreateUser(currentUser.getId());
        userDTO.setUpdateUser(currentUser.getId());
        userDTO.setPassword(passwordEncoder.encode(defaultPassword));
        userDTO.setUserType(YesOrNoConstant.YES);
        User user = BeanUtil.copyProperties(userDTO, User.class);
        super.save(user);

        //角色信息
        if (!CollectionUtil.isEmpty(userDTO.getRoleIdList())) {
            List<UserRole> userRoleList = new ArrayList<>();
            for (Long roleId : userDTO.getRoleIdList()) {
                UserRole userRole = new UserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(roleId);
                userRole.setCreateTime(new Date());
                userRole.setUpdateTime(new Date());
                userRole.setCreateUser(currentUser.getId());
                userRole.setUpdateUser(currentUser.getId());
                userRoleList.add(userRole);
            }
            userRoleService.saveBatch(userRoleList);
        }

        return user.getId();
    }

    @Override
    public void logout() throws Exception {
        UserDetailsInfo userDetailsInfo = (UserDetailsInfo) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        redisTemplate.delete(RedisConstant.LOGIN_USER + userDetailsInfo.getUser().getId());
    }

    @Override
    public IPage<UserDTO> selectPage(UserQuery userQuery) throws Exception {
        Page page = super.page(new Page<>(userQuery.getPageCurrent(), userQuery.getPageSize()),
                               new LambdaQueryWrapper<User>().eq(userQuery.getOrganizationId() != null,
                                                                 User::getOrganizationId, userQuery.getOrganizationId())
                                       .like(!StringUtils.isEmpty(userQuery.getAccount()), User::getAccount,
                                             userQuery.getAccount())
                                       .like(!StringUtils.isEmpty(userQuery.getUserName()), User::getUserName,
                                             userQuery.getUserName()));
        if (page.getTotal() <= 0) {
            return page;
        }
        page.setRecords(BeanUtil.copyToList(page.getRecords(), UserDTO.class));
        //角色信息
        fillUserRole(page.getRecords());
        return page;
    }

    private void fillUserRole(List<UserDTO> userDTOList) throws Exception {
        if (CollectionUtil.isEmpty(userDTOList)) {
            return;
        }
        List<UserRole> userRoleList = userRoleService.list(new LambdaQueryWrapper<UserRole>().in(UserRole::getUserId,
                                                                                                 userDTOList.stream()
                                                                                                         .map(UserDTO::getId)
                                                                                                         .collect(
                                                                                                                 Collectors.toList())));
        if (CollectionUtil.isEmpty(userRoleList)) {
            return;
        }
        List<Role> roleList = roleService.listByIds(
                userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toList()));
        if (CollectionUtil.isEmpty(roleList)) {
            return;
        }
        List<RoleDTO> roleDTOList = BeanUtil.copyToList(roleList, RoleDTO.class);
        Map<Long, RoleDTO> roleDTOMap = roleDTOList.stream()
                .collect(Collectors.toMap(RoleDTO::getId, Function.identity()));
        Map<Long, List<UserRole>> userRoleMap = userRoleList.stream()
                .collect(Collectors.groupingBy(UserRole::getUserId));
        for (UserDTO userDTO : userDTOList) {
            userDTO.setRoleList(new ArrayList<RoleDTO>());
            List<UserRole> list = userRoleMap.get(userDTO.getId());
            if (CollectionUtil.isEmpty(list)) {
                continue;
            }
            for (UserRole userRole : list) {
                RoleDTO roleDTO = roleDTOMap.get(userRole.getRoleId());
                if (roleDTO != null) {
                    userDTO.getRoleList().add(roleDTO);
                }
            }
        }
    }

    @Override
    public void batchChangeUserStatus(UserDTO userDTO) throws Exception {
        if (userDTO == null || CollectionUtil.isEmpty(userDTO.getIdList()) || userDTO.getUserStatus() == null) {
            throw new ServiceException("入参不能为空");
        }
        List<User> list = userDTO.getIdList().stream().map(id -> {
            User user = new User();
            user.setId(id);
            user.setUserStatus(userDTO.getUserStatus());
            return user;
        }).collect(Collectors.toList());
        super.updateBatchById(list);
    }

    @Override
    public List<UserDTO> getUserListByOrganizationIdAndRoleId(Long roleId, Long organizationId) throws Exception {
        List<User> userList = super.list(new LambdaQueryWrapper<User>().eq(User::getOrganizationId, organizationId));
        if (CollectionUtil.isEmpty(userList)) {
            return Lists.newArrayList();
        }
        List<UserRole> userRoleList = userRoleService.list(
                new LambdaQueryWrapper<UserRole>().eq(UserRole::getRoleId, roleId)
                        .in(UserRole::getUserId, userList.stream().map(User::getId).collect(Collectors.toList())));
        List<UserDTO> userDTOList = BeanUtil.copyToList(userList, UserDTO.class);
        userDTOList.forEach(user -> user.setHasRole(YesOrNoConstant.NO));
        //判断是否含有角色
        if (!CollectionUtil.isEmpty(userRoleList)) {
            Map<Long, UserDTO> map = userDTOList.stream()
                    .collect(Collectors.toMap(UserDTO::getId, Function.identity()));
            for (UserRole userRole : userRoleList) {
                UserDTO userDTO = map.get(userRole.getUserId());
                if (userDTO != null) {
                    userDTO.setHasRole(YesOrNoConstant.YES);
                }
            }
        }
        return userDTOList;
    }

    @Override
    public List<UserDTO> getUserListByRoleId(Long roleId) throws Exception {
        List<UserRole> userRoleList = userRoleService.list(
                new LambdaQueryWrapper<UserRole>().eq(UserRole::getRoleId, roleId));
        if (CollectionUtil.isEmpty(userRoleList)) {
            return Lists.newArrayList();
        }
        List<User> userList = super.listByIds(
                userRoleList.stream().map(UserRole::getUserId).collect(Collectors.toList()));
        return BeanUtil.copyToList(userList, UserDTO.class);
    }

    @Override
    public void getSmsCode(String mobile) throws Exception {
        if (!PhoneUtil.isMobile(mobile)) {
            throw new ServiceException("手机号码格式不正确");
        }
        Object smsCode = redisTemplate.opsForValue().get(mobile);
        if (smsCode != null) {
            throw new ServiceException("请勿频繁请求验证码");
        }
        //TODO 实际随机生成校验码并发送验证码
        String mobileRandom = "1234";
        redisTemplate.opsForValue().set(mobile, mobileRandom, 120L, TimeUnit.SECONDS);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, ServiceException.class})
    public String register(UserDTO userDTO, HttpServletRequest request) throws Exception {
        checkParam(userDTO);

        Object smsCode = redisTemplate.opsForValue().get(userDTO.getMobile());
        if (smsCode == null) {
            throw new ServiceException("验证码已过期,请重新发送");
        }
        if (!smsCode.toString().equals(userDTO.getSmsCode())) {
            throw new ServiceException("验证码不正确");
        }
        User user = BeanUtil.copyProperties(userDTO, User.class);
        user.setUserStatus(YesOrNoConstant.YES);
        user.setOrganizationId(OtherInfoConstant.EXTERNAL_ORGANIZATION);
        user.setUserType(YesOrNoConstant.NO);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        super.save(user);

        //默认角色
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(OtherInfoConstant.EXTERNAL_ROLE);
        userRole.setCreateUser(user.getId());
        userRole.setUpdateUser(user.getId());
        userRole.setCreateTime(new Date());
        userRole.setUpdateTime(new Date());
        userRoleService.save(userRole);

        return this.login(userDTO, request);
    }

    private void checkParam(UserDTO userDTO) throws Exception {
        if (userDTO == null) {
            throw new ServiceException("入参不能为空");
        }
        if (StringUtils.isEmpty(userDTO.getAccount())) {
            throw new ServiceException("账号不能为空");
        }
        if (StringUtils.isEmpty(userDTO.getPassword())) {
            throw new ServiceException("密码不能为空");
        }
        if (StringUtils.isEmpty(userDTO.getMobile())) {
            throw new ServiceException("手机号不能为空");
        }
        if (StringUtils.isEmpty(userDTO.getSmsCode())) {
            throw new ServiceException("手机号验证码不能为空");
        }
    }

    private void checkUser(UserDTO userDTO) throws Exception {
        if (StringUtils.isEmpty(userDTO.getAccount())) {
            throw new ServiceException("账号不能为空");
        }
        if (StringUtils.isEmpty(userDTO.getUserName())) {
            throw new ServiceException("姓名不能为空");
        }
        if (StringUtils.isEmpty(userDTO.getIdentityCard())) {
            throw new ServiceException("身份证不能为空");
        }
        if (userDTO.getUserStatus() == null) {
            throw new ServiceException("用户状态不能为空");
        }
        if (userDTO.getOrganizationId() == null) {
            throw new ServiceException("所属组织不能为空");
        }
    }
}
