package boot.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.*;

/**
 *
 * @author ASUS
 * @TableName mqtt_data
 */
@TableName(value ="mqtt_data")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MqttData implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     *
     */
    @TableField(value = "payload")
    private Double payload;

    /**
     *
     */
    @TableField(value = "time_stamp")
    private Date timeStamp;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
