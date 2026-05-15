package cn.qihangerp.security;

import cn.qihangerp.common.ServiceException;
import cn.qihangerp.enums.EnumUserType;
import cn.qihangerp.enums.UserStatus;
import cn.qihangerp.common.utils.MessageUtils;
import cn.qihangerp.common.utils.StringUtils;
import cn.qihangerp.domain.SysUser;
import cn.qihangerp.domain.vo.UserVo;
import cn.qihangerp.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * 用户验证处理
 *
 * @author qihang
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;
    @Autowired
    private UserPasswordService passwordService;
//    @Autowired
//    private SysPasswordService passwordService;

//    @Autowired
//    private SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        SysUser user = userService.selectUserByUserName(username);
        if (StringUtils.isNull(user))
        {
            log.info("登录用户：{} 不存在.", username);
            throw new ServiceException(MessageUtils.message("user.not.exists"));
        }
        else if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            log.info("登录用户：{} 已被删除.", username);
            throw new ServiceException(MessageUtils.message("user.password.delete"));
        }
        else if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            log.info("登录用户：{} 已被停用.", username);
            throw new ServiceException(MessageUtils.message("user.blocked"));
        }
        Integer userIdentity = null;
        //用户类型（00系统用户10仓库20商户30供应商）
        if(user.getUserType().equals("00")){
            userIdentity= EnumUserType.SUPPER.getIndex();
        }else if(user.getUserType().equals("20")){
//            userIdentity = 20;
            userIdentity = EnumUserType.MERCHANT.getIndex();
        }else if(user.getUserType().equals("10")){
//            userIdentity = 10;
            userIdentity = EnumUserType.WAREHOUSE.getIndex();
        }else if(user.getUserType().equals("30")){
//            userIdentity = 30;
            userIdentity = EnumUserType.VENDOR.getIndex();
        }else if(user.getUserType().equals("40")){
//            userIdentity = 30;
            userIdentity = EnumUserType.STORE.getIndex();
        }

        UserVo userVo = new UserVo();
        userVo.setMerchantId(user.getDeptId());
        userVo.setNickName(user.getNickName());
        userVo.setUserIdentity(userIdentity);
        userVo.setUserId(user.getUserId());
        userVo.setDeptId(user.getDeptId());
        userVo.setCreateTime(user.getCreateTime());
        userVo.setUserName(user.getUserName());
        userVo.setPassword(user.getPassword());
        if(user.getUserType().equals("00")) {
            //如果是管理员，那么验证密码
            passwordService.validate(userVo);
        }
        return createLoginUser(userVo);
    }

    public UserDetails createLoginUser(UserVo userVo)
    {
        Set<String> perms = new HashSet<String>();
        perms.add("*:*:*");
        return new LoginUser(userVo.getUserId(), userVo.getDeptId(),userVo.getUserIdentity(), userVo, perms);
//        return new LoginUser(user.getUserId(), user.getDeptId(), user, permissionService.getMenuPermission(user));
//        return new LoginUser(user.getUserId(), user.getDeptId(), user, perms);
    }
}
