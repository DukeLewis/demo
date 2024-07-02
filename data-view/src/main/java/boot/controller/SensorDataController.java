package boot.controller;

import boot.config.MqttConsumer;
import boot.config.PropertiesUtil;
import boot.domain.MqttData;
import boot.service.MqttDataService;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;

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
        List<MqttData> data = mqttDataService.lambdaQuery().orderByAsc(MqttData::getTimeStamp).list();
        if (data == null) {
            model.addAttribute("maxId", 0);
            model.addAttribute("timeStamps", null);
            model.addAttribute("humidities", null);
            model.addAttribute("temperatures", null);
            return "DataView";
        }
        // 提取时间戳和值
        List<String> timestamps = data.stream()
                .map(d -> "\"" + d.getTimeStamp().toString() + "\"")
                .collect(Collectors.toList());
        // 获取湿度
        List<Double> humidities = data.stream()
                .map(MqttData::getHumidity)
                .collect(Collectors.toList());
        // 获取温度
        List<Double> temperatures = data.stream()
                .map(MqttData::getTemperature)
                .collect(Collectors.toList());
        MqttData mqttData = data.stream()
                .max(Comparator.comparing(MqttData::getId))
                .orElse(null);
        assert mqttData != null;
        model.addAttribute("maxId", mqttData.getId());
        model.addAttribute("timeStamps", timestamps);
        model.addAttribute("humidities", humidities);
        model.addAttribute("temperatures", temperatures);
        return "DataView";
    }

    @GetMapping("/api/data")
    @ResponseBody
    public List<MqttData> getCurrentSensorData(@RequestParam("maxId") Integer maxId) {
        return mqttDataService.lambdaQuery()
                .gt(MqttData::getId, maxId)
                .orderByAsc(MqttData::getTimeStamp)
                .list();
    }

    /**
     * 五秒一次
     */
    @Scheduled(cron = "*/5 * * * * *")
    public void publish() {
        MqttConsumer.publish("wateringljy/windows", JSON.toJSONString(new Msg("get_airmsg")));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Msg {
        private String msg;
    }

    // 定时插入数据，模拟数据
//    @Scheduled(cron = "* * * * * ?")
//    public void insertData() {
//        mqttDataService.save(MqttData.builder()
//                .temperature(Math.random() * 30)
//                .humidity(Math.random() * 100)
//                .timeStamp(new Date())
//                .build());
//    }
}
