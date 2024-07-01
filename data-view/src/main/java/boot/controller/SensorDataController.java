package boot.controller;

import boot.domain.MqttData;
import boot.service.MqttDataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ASUS
 */
@Controller
public class SensorDataController {

    @Resource
    private MqttDataService mqttDataService;

    @GetMapping("/dashboard")
    public String sensorDashboard(Model model) {
        // 从数据库获取数据
        List<MqttData> data = mqttDataService.lambdaQuery().list();
        // 提取时间戳和值
        List<String> timestamps = data.stream()
            .map(d -> "\"" + d.getTimeStamp().toString() + "\"")
            .collect(Collectors.toList());
        // 获取值
        List<Double> payloads = data.stream()
            .map(MqttData::getPayload)
            .collect(Collectors.toList());
        model.addAttribute("timeStamps", timestamps);
        model.addAttribute("payloads", payloads);
        return "DataView";
    }

    @GetMapping("/api/data")
    @ResponseBody
    public List<MqttData> getCurrentSensorData() {
        // 实际应用中可能只返回最近的数据或者按需求筛选
        return mqttDataService.lambdaQuery().list();
    }

    // 定时插入数据，模拟数据
//    @Scheduled(cron = "* * * * * ?")
//    public void insertData() {
//        mqttDataService.save(MqttData.builder()
//                    .payload(Math.random()*10)
//                    .timeStamp(new Date())
//                    .build());
//    }
}
