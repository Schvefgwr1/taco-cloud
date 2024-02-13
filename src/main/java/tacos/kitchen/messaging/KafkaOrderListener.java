package tacos.kitchen.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tacos.message.CustomMessageConverterTacoOrders;
import tacos.model.TacoOrder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import tacos.integration.filesystem.FileWriterGateway;

@Component
public class KafkaOrderListener {
    private CustomMessageConverterTacoOrders converter = new CustomMessageConverterTacoOrders(
            new ObjectMapper()
    );

    @KafkaListener(topics = "tacocloud.orders.topic", groupId = "kitchen")
    public void handle(String order_str) throws IOException {
        TacoOrder order = converter.fromMessage(order_str);

        //пример записи в файл для просмотра результата
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("C:\\Users\\Сева Счетов\\IdeaProjects\\taco-cloud\\taco-cloud\\src\\main\\java\\tacos\\kitchen\\messaging\\logging.log"));
        oos.writeObject(order);


    }
}