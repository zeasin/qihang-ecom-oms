package cn.qihangerp.model.bo;

import lombok.Data;

@Data
public class JdTokenCreateBo {
    private Long shopId;
    private Integer shopType;
    private String code;
}
