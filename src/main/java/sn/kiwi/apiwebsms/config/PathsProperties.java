package sn.kiwi.apiwebsms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class PathsProperties {

    @Autowired
    private Environment env;

    public String getPathValue(String configKey){
        return env.getProperty(configKey);
    }
}