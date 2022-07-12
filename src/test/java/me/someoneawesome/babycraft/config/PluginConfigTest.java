package me.someoneawesome.babycraft.config;

import me.someoneawesome.babycraft.PluginLogger;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.junit.jupiter.api.Test;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class PluginConfigTest {

    @Test
    public void setupConfigVariables_setup() {
        //assert
        int expectedNumberOfConfigTypes = 3;
        int expectedSpeakRadius = 5;

        PluginLogger logger = PluginLogger.getLogger(PluginConfig.class, "Unit test Logger Plugin Config");

        FileConfiguration config = mock(FileConfiguration.class);
        String expectedFilenameMain = "main.yml";
        String expectedFilenamePlayers = "players.yml";
        String expectedFilenameChildren = "children.yml";

        int expectedVersionMain = 1;
        int expectedVersionPlayers = 1;
        int expectedVersionChildren = 1;

        List<String> expectedStringListChestplates = Arrays.asList("CHAINMAIL_CHESTPLATE", "DIAMOND_CHESTPLATE");
        List<String> expectedStringListLegs = Arrays.asList("CHAINMAIL_LEGGINGS", "DIAMOND_LEGGINGS");
        List<String> expectedStringListBoots = Arrays.asList("CHAINMAIL_BOOTS", "DIAMOND_BOOTS");
        List<String> expectedStringListBoyNames = Arrays.asList("boy 1", "boy 2");
        List<String> expectedStringListGirlNames = Arrays.asList("girl 1", "girl 2");

        when(config.getString("config.main.filename")).thenReturn(expectedFilenameMain);
        when(config.getString("config.players.filename")).thenReturn(expectedFilenamePlayers);
        when(config.getString("config.children.filename")).thenReturn(expectedFilenameChildren);

        when(config.getString("config.main.version")).thenReturn(String.valueOf(expectedVersionMain));
        when(config.getString("config.players.version")).thenReturn(String.valueOf(expectedVersionPlayers));
        when(config.getString("config.children.version")).thenReturn(String.valueOf(expectedVersionChildren));

        when(config.getStringList("utils.chestplates")).thenReturn(expectedStringListChestplates);
        when(config.getStringList("utils.leggings")).thenReturn(expectedStringListLegs);
        when(config.getStringList("utils.boots")).thenReturn(expectedStringListBoots);

        when(config.getInt("children.speakingRadius")).thenReturn(expectedSpeakRadius);
        when(config.getStringList("children.boyNames")).thenReturn(expectedStringListBoyNames);
        when(config.getStringList("children.girlNames")).thenReturn(expectedStringListGirlNames);

        PluginConfig pluginConfig = new PluginConfig(logger);

        //actual
        pluginConfig.setupConfigVariables(config);

        //assert
        boolean hasMain = false;
        boolean hasPlayer = false;
        boolean hasChildren = false;
        for(PluginConfig.ConfigType type : pluginConfig.configTypes()) {
            if(type.label.equalsIgnoreCase("main")) {
                assertEquals(expectedFilenameMain, type.filename);
                assertEquals(expectedVersionMain, type.version);
                hasMain = true;
            } else if(type.label.equalsIgnoreCase("players")) {
                assertEquals(expectedFilenamePlayers, type.filename);
                assertEquals(expectedVersionPlayers, type.version);
                hasPlayer = true;
            } else if(type.label.equalsIgnoreCase("children")) {
                assertEquals(expectedFilenameChildren, type.filename);
                assertEquals(expectedVersionChildren, type.version);
                hasChildren = true;
            }
        }
        assertTrue(hasMain);
        assertTrue(hasPlayer);
        assertTrue(hasChildren);
        assertEquals(expectedNumberOfConfigTypes, pluginConfig.configTypes().size());

        assertMaterialListIsStringList(expectedStringListChestplates, pluginConfig.getChestplates());
        assertMaterialListIsStringList(expectedStringListLegs, pluginConfig.getLeggings());
        assertMaterialListIsStringList(expectedStringListBoots, pluginConfig.getBoots());

        assertEquals(expectedSpeakRadius, pluginConfig.getSpeakingRadius());
        assertEquals(expectedStringListBoyNames, pluginConfig.getBoyNames());
        assertEquals(expectedStringListGirlNames, pluginConfig.getGirlNames());
    }

    private void assertMaterialListIsStringList(List<String> expected, HashSet<Material> actual) {
        for(String item : expected) {
            Material mat = Material.getMaterial(item);
            assertTrue(actual.contains(mat));
        }
    }
}
