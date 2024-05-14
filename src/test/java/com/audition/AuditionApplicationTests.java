package com.audition;


import com.audition.integration.AuditionIntegrationClient;
import com.audition.service.AuditionService;
import com.audition.web.AuditionController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuditionApplicationTests {

    @Autowired
    private AuditionController auditionController;

    @Autowired
    private AuditionService auditionService;

    @Autowired
    private AuditionIntegrationClient auditionIntegrationClient;


    @Test
    void contextLoads() {
        assertThat(auditionController).isNotNull();
        assertThat(auditionService).isNotNull();
        assertThat(auditionIntegrationClient).isNotNull();
    }

}
