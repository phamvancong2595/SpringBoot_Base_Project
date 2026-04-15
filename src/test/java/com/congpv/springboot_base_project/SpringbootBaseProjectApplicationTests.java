package com.congpv.springboot_base_project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "sentry.dsn=",
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "JWT_SECRET=daf66e01593f61a15b857cf433aae03a005812b31234e149036bcc8dee755dbb"
})
class SpringbootBaseProjectApplicationTests {

    @Test
    void contextLoads() {
    }
}
