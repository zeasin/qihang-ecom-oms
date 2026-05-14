package cn.qihangerp.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 *
 */
@Data
public class WarehouseCloudQuery implements Serializable {

    /**
     * 类型（LOCAL本地仓CLOUD系统云仓JDYC京东云仓Other其他）
     */
    @Schema(description = "仓库类型（LOCAL本地仓CLOUD系统云仓JDYC京东云仓）", example = "LOCAL")
    private String warehouseType;

    /**
     * status
     */
    @Schema(description = "状态（1启用0禁用）", example = "1")
    private String status;

    @Schema(description = "商户ID（0总部）", example = "0")
    private Long merchantId;

}