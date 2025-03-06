package ru.darujo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties( prefix = "integration.task-service")
@Data
public class TaskServiceProperty implements PropertyConnectionInterface {
  private String url;
  private Integer connectionTimeOut;
  private Integer readTimeOut;
  private Integer writeTimeOut;
}
