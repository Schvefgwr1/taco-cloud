package tacos.integration.filesystem;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.file.FileHeaders;
import org.springframework.messaging.handler.annotation.Header;

//шлюз отправки сообщений
//аннотация говорит о создании реализации интерфейса,
// в котором все сообщения будут направляться в канал textInChannel
@MessagingGateway(defaultRequestChannel="textInChannel")
public interface FileWriterGateway {

    //принимает на вход имя файла и текст сообщения
    //аннотация Header говорит о том, что имя файла должно быть записано в заголовок сообщения
    //с именем FileHeaders.FILENAME
    void writeToFile(@Header(FileHeaders.FILENAME) String filename, String data);
}