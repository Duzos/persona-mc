package mc.duzo.persona.client.sound;

import mc.duzo.persona.PersonaMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

/**
 * A manager for playing sounds on the client
 * Makes things easier
 *
 * @author duzo
 */
public class SoundsManager {
    protected List<SoundInstance> sounds;

    protected SoundsManager() {}

    public static SoundsManager create(SoundInstance... list) {
        SoundsManager handler = new SoundsManager();

        handler.sounds = new ArrayList<>();
        handler.sounds.addAll(List.of(list));

        return handler;
    }

    public void startIfNotPlaying(SoundEvent event) {
        if (!isPlaying(event)) startSound(event);
    }
    public void startIfNotPlaying(SoundInstance sound) {
        if (!isPlaying(sound)) startSound(sound);
    }

    public boolean isPlaying(SoundEvent event) {
        return MinecraftClient.getInstance().getSoundManager().isPlaying(findSoundByEvent(event));
    }
    public boolean isPlaying(SoundInstance sound) {
        return MinecraftClient.getInstance().getSoundManager().isPlaying(sound);
    }

    /**
     * Searches through the sounds in this handler and starts playing whichever one matches the SoundEvent given
     * @param event the event to search for
     */
    public void startSound(SoundEvent event) {
        SoundInstance sound = findSoundByEvent(event);

        MinecraftClient.getInstance().getSoundManager().play(sound);
    }
    public void startSound(SoundInstance sound) {
        if (sound == null) return;
        MinecraftClient.getInstance().getSoundManager().play(sound);
        this.sounds.add(sound);
    }
    public void stopSound(SoundEvent event) {
        MinecraftClient.getInstance().getSoundManager().stop(findSoundByEvent(event));
    }
    public void stopSound(SoundInstance sound) {
        MinecraftClient.getInstance().getSoundManager().stop(sound);
    }
    public void stopSounds() {
        if (this.sounds == null) return;

        for (SoundInstance sound : this.sounds) {
            this.stopSound(sound);
        }
    }

    /**
     * Finds the first sound instance that matches the event given
     */
    public SoundInstance findSoundByEvent(SoundEvent event) {
        return findSoundById(event.getId());
    }
    public SoundInstance findSoundById(Identifier id) {
        Identifier temp;

        for (SoundInstance sound : this.sounds) {
            temp = sound.getId();

            if (temp.equals(id)) return sound;
        }

        PersonaMod.LOGGER.error("Could not find sound " + id + " in list, returning empty sound!");
        return new PlayerFollowingLoopingSound(SoundEvents.INTENTIONALLY_EMPTY, SoundCategory.NEUTRAL);
    }
}
