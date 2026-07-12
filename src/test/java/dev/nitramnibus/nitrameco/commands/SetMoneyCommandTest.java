package dev.nitramnibus.nitrameco.commands;

import dev.nitramnibus.nitrameco.NitramEco;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import static org.junit.jupiter.api.Assertions.fail;

public class SetMoneyCommandTest {


    private ServerMock server;
    private NitramEco plugin;

    @BeforeEach
    public void setUp() {
        // start the mock server
        server = MockBukkit.getMock();
        plugin = MockBukkit.load(NitramEco.class);
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void failTest() {
        fail();
    }


}
