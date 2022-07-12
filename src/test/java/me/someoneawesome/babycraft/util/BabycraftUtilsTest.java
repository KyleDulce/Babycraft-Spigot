package me.someoneawesome.babycraft.util;

import org.bukkit.Material;
import org.junit.jupiter.api.Test;
import me.someoneawesome.babycraft.Babycraft;
import me.someoneawesome.babycraft.config.PluginConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static me.someoneawesome.babycraft.util.BabycraftUtils.*;

public class BabycraftUtilsTest {

    @Test
    public void getResourceAsOptional_getsResourceAndContents() {
        Babycraft plugin = mock(Babycraft.class);
        InputStream stream = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        when(plugin.getResource(anyString())).thenReturn(stream);
        Babycraft.instance = plugin;

        Optional<InputStream> actual = getResourceAsOptional("src/test/resources/test-resource.txt");

        assertTrue(actual.isPresent());
    }

    @Test
    public void getResourceAsOptional_emptyOptionalOnResourceDoesNotExist() {
        Babycraft plugin = mock(Babycraft.class);
        when(plugin.getResource(anyString())).thenReturn(null);
        Babycraft.instance = plugin;

        Optional<InputStream> actual = getResourceAsOptional("this is really bad");

        assertTrue(actual.isEmpty());
    }

    @Test
    public void isStringNullOrEmpty_nullBlankEmpty_true_others_false() {
        assertTrue(BabycraftUtils.isStringNullOrEmpty(null));
        assertTrue(BabycraftUtils.isStringNullOrEmpty(""));
        assertTrue(BabycraftUtils.isStringNullOrEmpty("   "));
        assertFalse(BabycraftUtils.isStringNullOrEmpty(" Hello all\t"));
    }

    @Test
    public void removeFirstItemFromArray_success() {
        Integer[] elements = new Integer[] {1,2,3};

        Integer[] actual = removeFirstItemFromArray(elements);

        assertEquals(2, actual.length);
        assertEquals(2, actual[0]);
        assertEquals(3, actual[1]);
    }

    @Test
    public void removeFirstItemFromArray_emptyArray_null() {
        Integer[] elements = new Integer[0];

        Integer[] actual = removeFirstItemFromArray(elements);

        assertNull(actual);
    }

    @Test
    public void removeFirstItemFromArray_nullArray_null() {
        Integer[] elements = null;

        Integer[] actual = removeFirstItemFromArray(elements);

        assertNull(actual);
    }

    @Test
    public void materialIsChestplate_ItemInList_true() {
        HashSet<Material> getSet = new HashSet<>();
        getSet.add(Material.APPLE);

        PluginConfig pluginConfig = mock(PluginConfig.class);
        when(pluginConfig.getChestplates()).thenReturn(getSet);
        PluginConfig.instance = pluginConfig;

        boolean actual = materialIsChestplate(Material.APPLE);

        assertTrue(actual);
    }

    @Test
    public void materialIsChestplate_ItemNotInList_false() {
        HashSet<Material> getSet = new HashSet<>();
        getSet.add(Material.APPLE);

        PluginConfig pluginConfig = mock(PluginConfig.class);
        when(pluginConfig.getChestplates()).thenReturn(getSet);
        PluginConfig.instance = pluginConfig;

        boolean actual = materialIsChestplate(Material.DIRT);

        assertFalse(actual);
    }

    @Test
    public void materialIsLeggings_ItemInList_true() {
        HashSet<Material> getSet = new HashSet<>();
        getSet.add(Material.APPLE);

        PluginConfig pluginConfig = mock(PluginConfig.class);
        when(pluginConfig.getLeggings()).thenReturn(getSet);
        PluginConfig.instance = pluginConfig;

        boolean actual = materialIsLeggings(Material.APPLE);

        assertTrue(actual);
    }

    @Test
    public void materialIsLeggings_ItemNotInList_false() {
        HashSet<Material> getSet = new HashSet<>();
        getSet.add(Material.APPLE);

        PluginConfig pluginConfig = mock(PluginConfig.class);
        when(pluginConfig.getLeggings()).thenReturn(getSet);
        PluginConfig.instance = pluginConfig;

        boolean actual = materialIsLeggings(Material.DIRT);

        assertFalse(actual);
    }

    @Test
    public void materialIsBoots_ItemInList_true() {
        HashSet<Material> getSet = new HashSet<>();
        getSet.add(Material.APPLE);

        PluginConfig pluginConfig = mock(PluginConfig.class);
        when(pluginConfig.getBoots()).thenReturn(getSet);
        PluginConfig.instance = pluginConfig;

        boolean actual = materialIsBoots(Material.APPLE);

        assertTrue(actual);
    }

    @Test
    public void materialIsBoots_ItemNotInList_false() {
        HashSet<Material> getSet = new HashSet<>();
        getSet.add(Material.APPLE);

        PluginConfig pluginConfig = mock(PluginConfig.class);
        when(pluginConfig.getBoots()).thenReturn(getSet);
        PluginConfig.instance = pluginConfig;

        boolean actual = materialIsBoots(Material.DIRT);

        assertFalse(actual);
    }
}
