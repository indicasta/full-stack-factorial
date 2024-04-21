package com.indicasta.fullstackfactorial;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ContainerTests extends AbstractTestContainer{

    @Test
    void canStartPostgresDB() {
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();
    }

}
