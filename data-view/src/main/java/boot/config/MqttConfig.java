package boot.config;

import boot.domain.MqttData;
import boot.service.MqttDataService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import javax.annotation.Resource;
import java.util.Date;

/**
 * mqtt 配置类
 *
 * @author ASUS
 */
@Configuration
@IntegrationComponentScan
public class MqttConfig {

//    @Resource
//    private MqttDataService mqttDataService;
//
//    @Bean
//    public MqttPahoClientFactory mqttClientFactory() {
//        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
//        return factory;
//    }
//
//    @Bean
//    public MessageChannel mqttInputChannel() {
//        return new DirectChannel();
//    }
//
//    @Bean
//    public MqttPahoMessageDrivenChannelAdapter inbound(MessageChannel mqttInputChannel, MqttPahoClientFactory mqttClientFactory) {
//        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("springBootClient", mqttClientFactory, "your/topic");
//        adapter.setCompletionTimeout(5000);
//        adapter.setConverter(new DefaultPahoMessageConverter());
//        adapter.setQos(1);
//        adapter.setOutputChannel(mqttInputChannel);
//        return adapter;
//    }
//
//    @Bean
//    @ServiceActivator(inputChannel = "mqttInputChannel")
//    public MessageHandler handler() {
//        return message -> {
//            System.out.println("Received message: " + message.getPayload());
//            // 在此处处理数据，比如更新数据库、发送通知等
//            mqttDataService.save(MqttData.builder()
//                    .payload(Double.parseDouble(message.getPayload().toString()))
//                    .timeStamp(new Date())
//                    .build());
//        };
//    }
}
