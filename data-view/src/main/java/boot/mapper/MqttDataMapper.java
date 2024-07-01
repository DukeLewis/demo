package boot.mapper;

import boot.domain.MqttData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author ASUS
* @description 针对表【mqtt_data】的数据库操作Mapper
* @createDate 2024-07-01 22:26:55
* @Entity generator.domain.MqttData
*/
@Mapper
public interface MqttDataMapper extends BaseMapper<MqttData> {

}




