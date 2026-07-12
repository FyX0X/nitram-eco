package dev.nitramnibus.nitrameco.commands;

import dev.nitramnibus.nitrameco.NitramEco;
import dev.nitramnibus.nitrameco.systems.EconomySystem;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.command.ConsoleCommandSenderMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class SetMoneyCommandTest {


    private ServerMock server;
    private NitramEco plugin;
    private EconomySystem economy;

    @BeforeEach
    public void setUp() {
        // start the mock server
        server = MockBukkit.mock();
        plugin = MockBukkit.load(NitramEco.class);
        economy = plugin.getEconomySystem();
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }
    /**
     * This test will currently modify the actual plugin's database
     * TODO: Create alternative DAO that simulates database, or use other database.
     */
    @Test
    public void testCommand() {
        ConsoleCommandSenderMock console = server.getConsoleSender();
        PlayerMock player = server.addPlayer("TestPlayer");
        UUID uuid = player.getUniqueId();

        player.performCommand("setmoney 10"); // should not work since no permission
        assertEquals(0, economy.getMoney(uuid));

        server.dispatchCommand(console, "setmoney TestPlayer 20");
        assertEquals(20, economy.getMoney(uuid));

        server.dispatchCommand(console, "op TestPlayer");
        player.performCommand("setmoney 10"); // should work since has permission
        assertEquals(10, economy.getMoney(uuid));


        player.performCommand("setmoney -10"); // should not work since negative
        assertEquals(10, economy.getMoney(uuid));
    }


}
