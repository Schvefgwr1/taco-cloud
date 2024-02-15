package tacos.integration.filesystem;

import java.io.File;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.support.FileExistsMode;

//конфигурация потока интеграции записи в файл
@Configuration
public class FileWriterIntegrationConfig {
    @Bean
    public IntegrationFlow fileWriterFlow() {
        return IntegrationFlow
                //входной канал
                .from(MessageChannels.direct("textInChannel"))
                //преобразователь
                .<String, String>transform(t -> t.toUpperCase())
                //обработка операции записи в файл
                .handle(Files
                        .outboundAdapter(new File("./files"))
                        .fileExistsMode(FileExistsMode.APPEND)
                        .appendNewLine(true))
                .get();
    }
}