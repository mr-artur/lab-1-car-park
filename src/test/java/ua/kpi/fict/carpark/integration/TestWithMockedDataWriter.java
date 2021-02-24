package ua.kpi.fict.carpark.integration;

import org.springframework.boot.test.mock.mockito.MockBean;

import ua.kpi.fict.carpark.config.DataWriter;

public abstract class TestWithMockedDataWriter {

    @MockBean
    private DataWriter dataWriter;
}
