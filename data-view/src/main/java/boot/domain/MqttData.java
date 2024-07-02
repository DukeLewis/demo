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
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 湿度
     */
    @TableField(value = "humidity")
    private Double humidity;

    /**
     * 温度
     */
    @TableField(value = "temperature")
    private Double temperature;

    /**
     * 时间戳
     */
    @TableField(value = "time_stamp")
    private Date timeStamp;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
