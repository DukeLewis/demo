package pro.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ASUS
 * @TableName config_engine
 */
@TableName(value ="rule_engine")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RuleEngineEntity implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * groovy 执行脚本
     */
    @TableField(value = "groovy_script")
    private String groovyScript;

    /**
     * 规则 key
     */
    @TableField(value = "rule_key")
    private String ruleKey;

    /**
     * 创建时间
     */
    @TableField(value = "created_time")
    private Date createdTime;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time")
    private Date updatedTime;

    /**
     * 是否删除
     */
    @TableField(value = "is_deleted")
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
