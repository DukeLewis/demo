package pro.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 * @TableName template_script
 */
@TableName(value ="template_script")
@Data
public class TemplateScript implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 配置 key
     */
    @TableField(value = "config_key")
    private String configKey;

    /**
     * 配置对应的执行脚本
     */
    @TableField(value = "config_script")
    private String configScript;

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
    @TableField(value = "id_deleted")
    private Integer idDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
