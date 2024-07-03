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
 * @TableName mqtt_data2
 */
@TableName(value ="mqtt_data2")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MqttData2 implements Serializable {
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     *
     */
    @TableField(value = "humidity")
    private Double humidity;

    /**
     *
     */
    @TableField(value = "time_stamp")
    private Date timeStamp;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
