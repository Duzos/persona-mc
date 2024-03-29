package mc.duzo.persona.util;

import net.minecraft.util.Identifier;
import net.minecraft.world.World;

/**
 * @author MDTeam
 */
public class SerialDimension {
    private final World dimension;
    private final String value;

    public SerialDimension(World dimension) {
        this.dimension = dimension;
        this.value = this.dimension.getRegistryKey().getValue().toString();
    }

    /**
     * This method is strictly for serialization purposes to the client. - Loqor
     **/
    private SerialDimension(Identifier value) {
        this.dimension = null;
        this.value = value.getPath();
    }

    public SerialDimension(String value) {
        this(WorldUtil.findWorld(value));
    }

    public String getValue() {
        return value;
    }

    public World get() {
        return dimension;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SerialDimension other)
            return this.value.equals(other.getValue());

        return false;
    }
}