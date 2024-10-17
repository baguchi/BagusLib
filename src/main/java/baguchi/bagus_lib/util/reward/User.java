package baguchi.bagus_lib.util.reward;

import java.util.UUID;

public class User {
    private final Tier tier;
    private final UUID uuid;

    public User(Tier tier, UUID uuid) {
        this.tier = tier;
        this.uuid = uuid;
    }

    public Tier getTier() {
        return tier;
    }

    public UUID getUuid() {
        return uuid;
    }

    public static Tier getTier(String nameIn) {
        for (Tier tier : Tier.values()) {
            if (tier.name().equals(nameIn))
                return tier;
        }
        return Tier.UNKNOWN;
    }

    public enum Tier {
        STAFF(4),
        CONTRIBUTOR(3),
        CRYSTAL_FOX(2),
        FUNKER(1),
        UNKNOWN(-1);

        private final int level;

        Tier(int level) {
            this.level = level;
        }

        public int getLevel() {
            return this.level;
        }
    }
}
