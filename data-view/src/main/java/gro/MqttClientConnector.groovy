package gro

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage

/**
 * @description:
 * @author ：dukelewis
 * @date: 2024/7/1
 * @Copyright ： https://github.com/DukeLewis
 */
class MqttClientConnector implements MqttCallback {
    MqttClient client
    String brokerUrl = 'tcp://broker.hivemq.com:1883'
    String clientId = 'GroovySampleClient'
    String topic = 'your/topic'

    void start() {
        client = new MqttClient(brokerUrl, MqttClient.generateClientId())
        client.setCallback(this)
        MqttConnectOptions options = new MqttConnectOptions()
        options.setCleanSession(true)
        client.connect(options)
        client.subscribe(topic)
    }

    @Override
    void connectionLost(Throwable cause) {
        println "Connection lost!"
    }

    @Override
    void messageArrived(String topic, MqttMessage message) {
        println "Message received: ${new String(message.payload)}"
        // 更新数据和图表的逻辑
        ChartData.updateData(Double.parseDouble(new String(message.payload)))
    }

    @Override
    void deliveryComplete(IMqttDeliveryToken token) {
        // 传输完成
    }
}
