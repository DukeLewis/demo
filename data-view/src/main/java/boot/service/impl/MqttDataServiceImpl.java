package boot.service.impl;

import boot.domain.MqttData;
import boot.mapper.MqttDataMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import boot.service.MqttDataService;
import org.springframework.stereotype.Service;

/**
* @author ASUS
* @description 针对表【mqtt_data】的数据库操作Service实现
* @createDate 2024-07-01 22:26:55
*/
@Service
public class MqttDataServiceImpl extends ServiceImpl<MqttDataMapper, MqttData>
    implements MqttDataService{

}




