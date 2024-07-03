package boot.service;

import boot.config.PropertiesUtil;
import boot.domain.MqttData;
import boot.domain.MqttData2;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;

/**
 * mqtt回调处理类
 *
 * @author ASUS
 */
@Slf4j
public class MqttConsumerCallback implements MqttCallbackExtended {

    private MqttClient client;
    private MqttConnectOptions options;
    private String[] topic;
    private int[] qos;

    private MqttDataService mqttDataService;

    private MqttData2Service mqttData2Service;

    public MqttConsumerCallback(MqttClient client, MqttConnectOptions options, String[] topic, int[] qos, MqttDataService mqttDataService, MqttData2Service mqttData2Service) {
        this.client = client;
        this.options = options;
        this.topic = topic;
        this.qos = qos;
        this.mqttDataService = mqttDataService;
        this.mqttData2Service = mqttData2Service;
    }

    /**
     * 断开重连
     */
    @Override
    public void connectionLost(Throwable cause) {
        log.info("MQTT连接断开，发起重连......");
        try {
            if (null != client && !client.isConnected()) {
                client.reconnect();
                log.info("尝试重新连接");
            } else {
                client.connect(options);
                log.info("尝试建立新连接");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收到消息调用令牌中调用
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        log.info("deliveryComplete---------{}", Arrays.toString(topic));
    }

    /**
     * 消息处理
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        try {
            String msg = new String(message.getPayload());
            log.info("收到topic:{}, 消息：{}", topic, msg);
//            收到消息后执行具体的业务逻辑操作，比如将消息存储进数据库
            if (msg.lastIndexOf("Humidity") == -1) {
                return;
            }
            if (msg.indexOf("Temp") != -1) {
                String[] split = msg.split(" ");
                log.info("temp:{}", Double.parseDouble(split[0].split("=")[1]));
                mqttDataService.save(MqttData.builder()
                        .timeStamp(new Date())
                        .temperature(Double.parseDouble(split[0].split("=")[1]))
                        .humidity(Double.parseDouble(split[1].split("=")[1]))
                        .build());
            } else {
                mqttData2Service.save(MqttData2.builder()
                        .timeStamp(new Date())
                        .humidity(Double.parseDouble(msg.split("=")[1]))
                        .build());
            }
        } catch (Exception e) {
            log.info("处理mqtt消息异常:" + e);
        }
    }

    /**
     * mqtt连接后订阅主题
     */
    @Override
    public void connectComplete(boolean b, String s) {
        try {
            if (null != topic && null != qos) {
                if (client.isConnected()) {
                    client.subscribe(topic, qos);
                    log.info("mqtt连接成功，客户端ID：{}", PropertiesUtil.MQTT_CLIENT_ID);
                    log.info("--订阅主题:{}", Arrays.toString(topic));
                } else {
                    log.info("mqtt连接失败，客户端ID：{}", PropertiesUtil.MQTT_CLIENT_ID);
                }
            }
        } catch (Exception e) {
            log.info("mqtt订阅主题异常:{}" + e);
        }
    }
}
