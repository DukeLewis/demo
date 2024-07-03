package boot.controller;

import boot.config.MqttConsumer;
import boot.config.PropertiesUtil;
import boot.domain.MqttData;
import boot.domain.MqttData2;
import boot.service.MqttData2Service;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Collections;
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

    @Resource
    private MqttData2Service mqttData2Service;

    @GetMapping("/dashboard")
    public String sensorDashboard(Model model) {
        // 从数据库获取数据
        List<MqttData> data = mqttDataService.lambdaQuery().orderByAsc(MqttData::getTimeStamp).list();
        List<MqttData2> data2 = mqttData2Service.lambdaQuery().orderByAsc(MqttData2::getTimeStamp).list();
        if (data == null || data.isEmpty()) {
            model.addAttribute("maxId", 0);
            model.addAttribute("timeStamps", null);
            model.addAttribute("humidities", null);
            model.addAttribute("temperatures", null);
        } else {
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
        }
        if (data2 == null || data2.isEmpty()) {
            model.addAttribute("maxId2", 0);
            model.addAttribute("humidities2", null);
            model.addAttribute("timeStamps2", null);
        } else {
            // 获取湿度
            List<Double> humidities2 = data2.stream()
                    .map(MqttData2::getHumidity)
                    .collect(Collectors.toList());
            // 提取时间戳和值
            List<String> timestamps2 = data2.stream()
                    .map(d -> "\"" + d.getTimeStamp().toString() + "\"")
                    .collect(Collectors.toList());
            MqttData2 mqttData2 = data2.stream()
                    .max(Comparator.comparing(MqttData2::getId))
                    .orElse(null);
            assert mqttData2 != null;
            model.addAttribute("maxId2", mqttData2.getId());
            model.addAttribute("timeStamps2", timestamps2);
            model.addAttribute("humidities2", humidities2);
        }
        return "DataView";
    }

    @GetMapping("/api/data")
    @ResponseBody
    public List<MqttData> getCurrentSensorData(@RequestParam("maxId") Integer maxId) {
        if (maxId == null) {
            return null;
        }
        return mqttDataService.lambdaQuery()
                .gt(MqttData::getId, maxId)
                .orderByAsc(MqttData::getTimeStamp)
                .list();
    }

    @GetMapping("/api/data2")
    @ResponseBody
    public List<MqttData2> getCurrentSensorData2(@RequestParam("maxId2") Integer maxId2) {
        if (maxId2 == null) {
            return null;
        }
        return mqttData2Service.lambdaQuery()
                .gt(MqttData2::getId, maxId2)
                .orderByAsc(MqttData2::getTimeStamp)
                .list();
    }

    @GetMapping("/api/{operation}")
    public void sensorOperation(@PathVariable("operation") String operation) {
        int cnt = 1;
        if (OperationType.GET_AIRMSG.toString().equalsIgnoreCase(operation)) {
            cnt = 5;
        }
        for (int i = 0; i < cnt; i++) {
            MqttConsumer.publish("wateringljy/windows", JSON.toJSONString(new Msg(operation)));
        }
    }

    public static enum OperationType {
        OPEN,
        CLOSE,
        WATER_PRO,
        GET_AIRMSG;
    }


    /**
     * 五秒一次
     */
//    @Scheduled(cron = "*/5 * * * * *")
//    public void publish() {
//        MqttConsumer.publish("wateringljy/windows", JSON.toJSONString(new Msg("get_airmsg")));
//    }

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
