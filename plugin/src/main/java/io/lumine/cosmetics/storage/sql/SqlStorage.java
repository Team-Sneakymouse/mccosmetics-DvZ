package io.lumine.cosmetics.storage.sql;

import java.util.Optional;
import java.util.UUID;

import io.lumine.cosmetics.players.ProfileCosmeticData;
import io.lumine.cosmetics.players.ProfileManager;
import io.lumine.cosmetics.storage.sql.mappings.DefaultSchema;
import io.lumine.cosmetics.storage.sql.mappings.Tables;
import io.lumine.utils.lib.jooq.exception.DataAccessException;
import io.lumine.utils.logging.Log;
import io.lumine.utils.plugin.LuminePlugin;
import io.lumine.utils.promise.Promise;
import io.lumine.utils.storage.players.Profile;
import io.lumine.utils.storage.players.adapters.SqlPlayerStorageAdapter;
import io.lumine.utils.storage.sql.SqlConnector;

public class SqlStorage<MCCosmeticsPlugin,CosmeticsProfile> extends SqlPlayerStorageAdapter {

    private final ProfileManager manager;
    
    public SqlStorage(ProfileManager manager, SqlConnector connector) {
        super(manager.getPlugin(), connector);
        this.manager = manager;
        
        load(manager.getPlugin());
    }

    @Override
    public void load(LuminePlugin plugin) {
        var connection = getConnector().open();

        try {
            connection.fetch(Tables.MCCOSMETICS_PROFILE);
        } catch(DataAccessException ex) {
            Log.info("Generating database schema...");
            connection.ddl(DefaultSchema.DEFAULT_SCHEMA).executeBatch();
        }
    }

    @Override
    public void unload() {
    }

    @Override
    public Promise<Optional<io.lumine.cosmetics.players.Profile>> load(final UUID uuid) {
        return Promise.supplyingDelayedAsync(() -> {
            var connection = getConnector().open();

            var fetchProfile = connection.selectFrom(Tables.MCCOSMETICS_PROFILE)
                .where(Tables.MCCOSMETICS_PROFILE.UUID.eq(uuid.toString()))
                .fetchAny();
            
            if(fetchProfile == null) {
                return Optional.empty();
            }
            
            var profile = manager.createProfile(UUID.fromString(fetchProfile.getUuid()), fetchProfile.getName());

            profile.loadFromSql(fetchProfile);
            
            return Optional.ofNullable(profile);
        }, 20);
    }

    @Override
    public Promise<Optional<io.lumine.cosmetics.players.Profile>> loadByName(String name) {
        return Promise.supplyingAsync(() -> {
            var connection = getConnector().open();

            var fetchProfile = connection.selectFrom(Tables.MCCOSMETICS_PROFILE)
                .where(Tables.MCCOSMETICS_PROFILE.NAME.eq(name))
                .fetchAny();
            
            if(fetchProfile == null) {
                return Optional.empty();
            }
            
            var profile = manager.createProfile(UUID.fromString(fetchProfile.getUuid()), fetchProfile.getName());

            profile.loadFromSql(fetchProfile);
            
            return Optional.ofNullable(profile);
        });
    }

    @Override
    public Promise<Boolean> save(UUID uuid, Profile profile) {
        return Promise.supplyingAsync(() -> {
            return saveSync(uuid,profile);
        });
    }

    @Override
    public boolean saveSync(UUID uuid, Profile profile) {
        var connection = getConnector().open();

        connection.insertInto(Tables.MCCOSMETICS_PROFILE, 
                                Tables.MCCOSMETICS_PROFILE.UUID, 
                                Tables.MCCOSMETICS_PROFILE.NAME)
                .values(uuid.toString(), profile.getName())
                .onDuplicateKeyUpdate()
                .set(Tables.MCCOSMETICS_PROFILE.NAME, profile.getName())
                .returning()
                .execute();

        return true; 
    }

    public Promise<Boolean> saveCosmeticEquipped(io.lumine.cosmetics.players.Profile profile, String slot, ProfileCosmeticData cosmeticData) {
        
        return Promise.supplyingAsync(() -> {
            var connection = getConnector().open();

            if(cosmeticData != null) {
                connection.insertInto(Tables.MCCOSMETICS_PROFILE_EQUIPPED, 
                        Tables.MCCOSMETICS_PROFILE_EQUIPPED.PROFILE_UUID,
                        Tables.MCCOSMETICS_PROFILE_EQUIPPED.SLOT,
                        Tables.MCCOSMETICS_PROFILE_EQUIPPED.COSMETIC_ID,
                        Tables.MCCOSMETICS_PROFILE_EQUIPPED.COSMETIC_DATA)
                .values(profile.getUniqueId().toString(), slot, cosmeticData.getId(), cosmeticData.getVariant())
                .onDuplicateKeyUpdate()
                .set(Tables.MCCOSMETICS_PROFILE_EQUIPPED.COSMETIC_ID, cosmeticData.getId())
                .set(Tables.MCCOSMETICS_PROFILE_EQUIPPED.COSMETIC_DATA, cosmeticData.getVariant())
                .execute();
            } else {
                connection.deleteFrom(Tables.MCCOSMETICS_PROFILE_EQUIPPED)
                    .where(Tables.MCCOSMETICS_PROFILE_EQUIPPED.PROFILE_UUID.eq(profile.getUniqueId().toString()),
                           Tables.MCCOSMETICS_PROFILE_EQUIPPED.SLOT.eq(slot))
                    .execute();
            }

            return true;
        });
    }
    
}
