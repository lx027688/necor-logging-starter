# Necor Logging Starter

Necor Logging Starter 是一个基于 Spring Boot 的自定义日志启动器，旨在简化和标准化日志配置。通过简单的配置，您可以快速集成并定制日志功能。

## 功能特性

- **统一日志配置**：通过标准化的配置格式，简化项目中的日志设置。
- **日志文件路径和前缀配置**：可自定义日志文件的存储路径和前缀，支持项目名自动注入。
- **日志级别配置**：支持全局日志级别的配置。
- **自定义日志输出**：支持自定义 appender 和 marker，便于不同业务场景下的日志输出。

## 快速开始

### 引入依赖

在您的 `pom.xml` 文件中添加以下依赖：

```xml
<dependency>
	<groupId>com.necor.log</groupId>
	<artifactId>necor-logging-starter</artifactId>
	<version>1.0.0</version>
</dependency>
```

### 配置文件

在 `application.yml` 或 `application.properties` 中添加以下配置：

```yml
spring:
  application:
    name: my-app

necor:
  log:
    file:
      prefix: ${spring.application.name} # 使用 my-app 作为日志文件前缀
      path: logs # 将日志文件存储在logs
      appenders:
        test: # 自定义日志附加器，日志将会${prefix}_test.log 命名
          markers: TEST # 日志过滤器使用的 marker，用于日志分类。与filters配置二选其一
          filters: xxx.xx.Filter # 自定义过滤器，配置后markers不会生效
          maxFileSize: 100 # 单天单个日志大小 单位M (默认100，最大1024*10)
          maxHistory: 30 # 日志文件保留天数 (默认30，最大365*10)
          pattern: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n" # 默认日志输出格式，可自定义
    level: info # 设置全局日志级别为 info


```

### 自定义 Appender

您可以根据业务需求，添加自定义的 appender 。在上面的示例中，`test` appender 会输出包含 `TEST` marker 的日志。您可以根据需要，可以自定义filter, `filters`与 `markers`二选其一配置。如果都配置了，`markers`不会生效。

系统提供了名为API和BUSI的marker，可以通过NecorMarkerFactory获取。使用示例：

```java
Marker apiMarker = NecorMarkerFactory.getApiMarker();
log.info(apiMarker, "api marker");

Marker busiMarker = NecorMarkerFactory.getBusiMarker();
log.info(busiMarker, "busi marker");
```

自定义Filter示例：

```java
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class LogFilter  extends Filter<ILoggingEvent> {

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (event.getMarker() != null) {
            if (event.getMarker().contains("TEST")) {
                return FilterReply.ACCEPT;
            }else {
                return FilterReply.DENY;
            }
        }
        return FilterReply.DENY;
    }
}

```

## 贡献

欢迎提交问题和贡献代码！请通过 [GitHub issues](https://github.com/yourcompany/necor-logging-starter/issues) 报告问题，或者提交 Pull Request 来贡献代码。

## 许可证

Necor Logging Starter 使用 MIT 许可证 开源。

---

感谢使用 Necor Logging Starter。如果您有任何问题或建议，请随时联系我们。
