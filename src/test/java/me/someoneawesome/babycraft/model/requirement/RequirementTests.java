package me.someoneawesome.babycraft.model.requirement;

import me.someoneawesome.babycraft.config.ConfigInterface;
import me.someoneawesome.babycraft.config.interfaces.PlayersConfigInterface;
import me.someoneawesome.babycraft.model.Gender;
import me.someoneawesome.babycraft.model.permissions.BcPermission;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RequirementTests {

    @Test
    public void playerHasPermission_success() {
        String permissionString = "fakePermission";
        String failMsg = "fail";
        Player mockPlayer = mock(Player.class);
        BcPermission mockPermission = mock(BcPermission.class);

        when(mockPermission.toString()).thenReturn(permissionString);
        when(mockPlayer.hasPermission(permissionString)).thenReturn(true);

        RequirementCheck actual = RequirementVerifierBuilder
                .builder()
                .playerHasPermission(mockPlayer, mockPermission, failMsg)
                .build()
                .doesMeetRequirements();

        assertTrue(actual.isSuccess());
        verify(mockPlayer).hasPermission(permissionString);
    }

    @Test
    public void playerHasPermission_fail_messageOut() {
        String permissionString = "fakePermission";
        String failMsg = "fail";
        Player mockPlayer = mock(Player.class);
        BcPermission mockPermission = mock(BcPermission.class);

        when(mockPermission.toString()).thenReturn(permissionString);
        when(mockPlayer.hasPermission(permissionString)).thenReturn(false);

        RequirementCheck actual = RequirementVerifierBuilder
                .builder()
                .playerHasPermission(mockPlayer, mockPermission, failMsg)
                .build()
                .doesMeetRequirements();

        assertFalse(actual.isSuccess());
        assertEquals(failMsg, actual.getMessage());
        verify(mockPlayer).hasPermission(permissionString);
    }

    @ParameterizedTest
    @ValueSource(doubles = {1.0, 1.5})
    public void locationsWithinRadius_success(double radiusBetweenLocations) {
        String failMsg = "fail";
        double radiusToCheck = 1.5D;
        Location mockLoc1 = mock(Location.class);
        Location mockLoc2 = mock(Location.class);

        when(mockLoc1.distance(mockLoc2)).thenReturn(radiusBetweenLocations);

        RequirementCheck actual = RequirementVerifierBuilder
                .builder()
                .locationsWithinRadius(mockLoc1, mockLoc2, radiusToCheck, failMsg)
                .build()
                .doesMeetRequirements();

        assertTrue(actual.isSuccess());
        verify(mockLoc1).distance(mockLoc2);
    }

    @Test
    public void locationsWithinRadius_fail_messageOut() {
        String failMsg = "fail";
        double radiusToCheck = 1.5D;
        Location mockLoc1 = mock(Location.class);
        Location mockLoc2 = mock(Location.class);

        when(mockLoc1.distance(mockLoc2)).thenReturn(2.0D);

        RequirementCheck actual = RequirementVerifierBuilder
                .builder()
                .locationsWithinRadius(mockLoc1, mockLoc2, radiusToCheck, failMsg)
                .build()
                .doesMeetRequirements();

        assertFalse(actual.isSuccess());
        assertEquals(failMsg, actual.getMessage());
        verify(mockLoc1).distance(mockLoc2);
    }

    @ParameterizedTest
    @ValueSource(doubles = {1.0, 1.5})
    public void playersWithinRadius_success(double radiusBetweenLocations) {
        String failMsg = "fail";
        double radiusToCheck = 1.5D;
        Player mockPlayer1 = mock(Player.class);
        Player mockPlayer2 = mock(Player.class);
        Location mockLoc1 = mock(Location.class);
        Location mockLoc2 = mock(Location.class);

        when(mockLoc1.distance(mockLoc2)).thenReturn(radiusBetweenLocations);
        when(mockPlayer1.getLocation()).thenReturn(mockLoc1);
        when(mockPlayer2.getLocation()).thenReturn(mockLoc2);

        RequirementVerifierBuilder builder = RequirementVerifierBuilder.builder();
        RequirementVerifierBuilder spyBuilder = spy(builder);

        RequirementCheck actual = spyBuilder
                .playersWithinRadius(mockPlayer1, mockPlayer2, radiusToCheck, failMsg)
                .build()
                .doesMeetRequirements();

        assertTrue(actual.isSuccess());
        verify(mockLoc1).distance(mockLoc2);
        verify(spyBuilder).locationsWithinRadius(eq(mockLoc1), eq(mockLoc2), anyDouble(), eq(failMsg));
    }

    @Test
    public void playersWithinRadius_fail_messageOut() {
        String failMsg = "fail";
        double radiusToCheck = 1.5D;
        Player mockPlayer1 = mock(Player.class);
        Player mockPlayer2 = mock(Player.class);
        Location mockLoc1 = mock(Location.class);
        Location mockLoc2 = mock(Location.class);

        when(mockLoc1.distance(mockLoc2)).thenReturn(2.0D);
        when(mockPlayer1.getLocation()).thenReturn(mockLoc1);
        when(mockPlayer2.getLocation()).thenReturn(mockLoc2);

        RequirementVerifierBuilder builder = RequirementVerifierBuilder.builder();
        RequirementVerifierBuilder spyBuilder = spy(builder);

        RequirementCheck actual = spyBuilder
                .playersWithinRadius(mockPlayer1, mockPlayer2, radiusToCheck, failMsg)
                .build()
                .doesMeetRequirements();

        assertFalse(actual.isSuccess());
        assertEquals(failMsg, actual.getMessage());
        verify(mockLoc1).distance(mockLoc2);
        verify(spyBuilder).locationsWithinRadius(eq(mockLoc1), eq(mockLoc2), anyDouble(), eq(failMsg));
    }

    @Test
    public void playerExistInConfig_success() {
        String failMsg = "fail";
        PlayersConfigInterface mockConfig = mock(PlayersConfigInterface.class);
        new ConfigInterface(null, mockConfig, null);
        Player mockPlayer = mock(Player.class);

        when(mockConfig.contains(any())).thenReturn(true);
        when(mockPlayer.getUniqueId()).thenReturn(UUID.randomUUID());

        RequirementCheck actual = RequirementVerifierBuilder
                .builder()
                .playerExistInConfig(mockPlayer, failMsg)
                .build()
                .doesMeetRequirements();

        assertTrue(actual.isSuccess());
        verify(mockConfig).contains(any());
    }

    @Test
    public void playerExistInConfig_fail_messageOut() {
        String failMsg = "fail";
        PlayersConfigInterface mockConfig = mock(PlayersConfigInterface.class);
        new ConfigInterface(null, mockConfig, null);
        Player mockPlayer = mock(Player.class);

        when(mockConfig.contains(any())).thenReturn(false);
        when(mockPlayer.getUniqueId()).thenReturn(UUID.randomUUID());

        RequirementCheck actual = RequirementVerifierBuilder
                .builder()
                .playerExistInConfig(mockPlayer, failMsg)
                .build()
                .doesMeetRequirements();

        assertFalse(actual.isSuccess());
        assertEquals(failMsg, actual.getMessage());
        verify(mockConfig).contains(any());
    }

    @Test
    public void playerGenderNotNull_success() {
        String failMsg = "fail";
        PlayersConfigInterface mockConfig = mock(PlayersConfigInterface.class);
        new ConfigInterface(null, mockConfig, null);
        Player mockPlayer = mock(Player.class);

        when(mockConfig.contains(any(UUID.class))).thenReturn(true);
        when(mockConfig.getPlayerGender(any(UUID.class))).thenReturn(Gender.OTHER);
        when(mockPlayer.getUniqueId()).thenReturn(UUID.randomUUID());

        RequirementCheck actual = RequirementVerifierBuilder
                .builder()
                .playerGenderNotNull(mockPlayer, failMsg)
                .build()
                .doesMeetRequirements();

        assertTrue(actual.isSuccess());
        verify(mockConfig).contains(any());
        verify(mockConfig).getPlayerGender(any(UUID.class));
    }

    @Test
    public void playerGenderNotNull_doesNotContain_fail() {
        String failMsg = "fail";
        PlayersConfigInterface mockConfig = mock(PlayersConfigInterface.class);
        new ConfigInterface(null, mockConfig, null);
        Player mockPlayer = mock(Player.class);

        when(mockConfig.contains(any(UUID.class))).thenReturn(false);
        when(mockConfig.getPlayerGender(any(UUID.class))).thenReturn(Gender.OTHER);
        when(mockPlayer.getUniqueId()).thenReturn(UUID.randomUUID());

        RequirementCheck actual = RequirementVerifierBuilder
                .builder()
                .playerGenderNotNull(mockPlayer, failMsg)
                .build()
                .doesMeetRequirements();

        assertFalse(actual.isSuccess());
        assertEquals(failMsg, actual.getMessage());
        verify(mockConfig).contains(any());
    }

    @Test
    public void playerGenderNotNull_nullGender_fail() {
        String failMsg = "fail";
        PlayersConfigInterface mockConfig = mock(PlayersConfigInterface.class);
        new ConfigInterface(null, mockConfig, null);
        Player mockPlayer = mock(Player.class);

        when(mockConfig.contains(any(UUID.class))).thenReturn(true);
        when(mockConfig.getPlayerGender(any(UUID.class))).thenReturn(Gender.NULL);
        when(mockPlayer.getUniqueId()).thenReturn(UUID.randomUUID());

        RequirementCheck actual = RequirementVerifierBuilder
                .builder()
                .playerGenderNotNull(mockPlayer, failMsg)
                .build()
                .doesMeetRequirements();

        assertFalse(actual.isSuccess());
        assertEquals(failMsg, actual.getMessage());
        verify(mockConfig).contains(any());
        verify(mockConfig).getPlayerGender(any(UUID.class));
    }

    @Test
    public void playerGenderNotNull_nullGenderNotContain_fail() {
        String failMsg = "fail";
        PlayersConfigInterface mockConfig = mock(PlayersConfigInterface.class);
        new ConfigInterface(null, mockConfig, null);
        Player mockPlayer = mock(Player.class);

        when(mockConfig.contains(any(UUID.class))).thenReturn(false);
        when(mockConfig.getPlayerGender(any(UUID.class))).thenReturn(Gender.NULL);
        when(mockPlayer.getUniqueId()).thenReturn(UUID.randomUUID());

        RequirementCheck actual = RequirementVerifierBuilder
                .builder()
                .playerGenderNotNull(mockPlayer, failMsg)
                .build()
                .doesMeetRequirements();

        assertFalse(actual.isSuccess());
        assertEquals(failMsg, actual.getMessage());
        verify(mockConfig).contains(any());
    }

    @Test
    public void haveChildSameGenderCheck_bothSameGenderPermission_success() {
        String failMsg = "fail";
        Player mockPlayer1 = mock(Player.class);
        Player mockPlayer2 = mock(Player.class);

        when(mockPlayer1.hasPermission(BcPermission.BABYCRAFT_SAME_GENDER.toString())).thenReturn(true);
        when(mockPlayer2.hasPermission(BcPermission.BABYCRAFT_SAME_GENDER.toString())).thenReturn(true);

        RequirementCheck actual = RequirementVerifierBuilder
                .builder()
                .haveChildSameGenderCheck(mockPlayer1, mockPlayer2, failMsg)
                .build()
                .doesMeetRequirements();

        assertTrue(actual.isSuccess());
        verify(mockPlayer1).hasPermission(BcPermission.BABYCRAFT_SAME_GENDER.toString());
        verify(mockPlayer2).hasPermission(BcPermission.BABYCRAFT_SAME_GENDER.toString());
    }

    @ParameterizedTest
    @CsvSource({"true,false", "false,true"})
    public void haveChildSameGenderCheck_oneNoGenderPermissionAndSameGender_success(boolean p1, boolean p2) {
        String failMsg = "fail";
        Player mockPlayer1 = mock(Player.class);
        Player mockPlayer2 = mock(Player.class);
        UUID mockUid1 = mock(UUID.class);
        UUID mockUid2 = mock(UUID.class);
        PlayersConfigInterface mockConfig = mock(PlayersConfigInterface.class);
        new ConfigInterface(null, mockConfig, null);

        when(mockPlayer1.hasPermission(BcPermission.BABYCRAFT_SAME_GENDER.toString())).thenReturn(p1);
        when(mockPlayer2.hasPermission(BcPermission.BABYCRAFT_SAME_GENDER.toString())).thenReturn(p2);
        when(mockPlayer1.getUniqueId()).thenReturn(mockUid1);
        when(mockPlayer2.getUniqueId()).thenReturn(mockUid2);
        when(mockConfig.getPlayerGender(mockUid1)).thenReturn(Gender.MALE);
        when(mockConfig.getPlayerGender(mockUid2)).thenReturn(Gender.FEMALE);

        RequirementCheck actual = RequirementVerifierBuilder
                .builder()
                .haveChildSameGenderCheck(mockPlayer1, mockPlayer2, failMsg)
                .build()
                .doesMeetRequirements();

        assertTrue(actual.isSuccess());
        verify(mockConfig, times(2)).getPlayerGender(any());
    }

    @ParameterizedTest
    @CsvSource({"true,false", "false,true"})
    public void haveChildSameGenderCheck_noGenderPermissionAndEqualGender_Fail(boolean p1, boolean p2) {
        String failMsg = "fail";
        Player mockPlayer1 = mock(Player.class);
        Player mockPlayer2 = mock(Player.class);
        UUID mockUid1 = mock(UUID.class);
        UUID mockUid2 = mock(UUID.class);
        PlayersConfigInterface mockConfig = mock(PlayersConfigInterface.class);
        new ConfigInterface(null, mockConfig, null);

        when(mockPlayer1.hasPermission(BcPermission.BABYCRAFT_SAME_GENDER.toString())).thenReturn(p1);
        when(mockPlayer2.hasPermission(BcPermission.BABYCRAFT_SAME_GENDER.toString())).thenReturn(p2);
        when(mockPlayer1.getUniqueId()).thenReturn(mockUid1);
        when(mockPlayer2.getUniqueId()).thenReturn(mockUid2);
        when(mockConfig.getPlayerGender(any())).thenReturn(Gender.MALE);

        RequirementCheck actual = RequirementVerifierBuilder
                .builder()
                .haveChildSameGenderCheck(mockPlayer1, mockPlayer2, failMsg)
                .build()
                .doesMeetRequirements();

        assertFalse(actual.isSuccess());
        assertEquals(failMsg, actual.getMessage());
        verify(mockConfig, times(2)).getPlayerGender(any());
    }
}
