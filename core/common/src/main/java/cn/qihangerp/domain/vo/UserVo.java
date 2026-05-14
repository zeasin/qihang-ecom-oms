package cn.qihangerp.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * 用户对象 sys_user
 * 
 * @author qihang
 */
@Data
public class UserVo
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long userId;
    // 用户身份0管理员10云仓库20商户30供应商40店铺
    private Integer userIdentity;
    private Long merchantId;//商户ID

    /** 部门ID */
    private Long deptId;

    /** 用户账号 */

    private String userName;

    /** 用户昵称 */
    private String nickName;


    /** 手机号码 */
    private String mobile;

    /** 用户性别 */
    private String sex;

    /** 用户头像 */
    private String avatar;

    /** 密码 */
    private String password;

    /** 帐号状态（0正常 1停用） */
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 最后登录IP */
    private String loginIp;

    /** 最后登录时间 */
    private Date loginDate;

    /** 角色ID */
    private Long roleId;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    private String remark;

}
