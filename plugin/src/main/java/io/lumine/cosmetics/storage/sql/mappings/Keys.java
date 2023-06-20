/*
 * This file is generated by jOOQ.
 */
package io.lumine.cosmetics.storage.sql.mappings;


import io.lumine.cosmetics.storage.sql.mappings.tables.MccosmeticsProfile;
import io.lumine.cosmetics.storage.sql.mappings.tables.MccosmeticsProfileEquipped;
import io.lumine.cosmetics.storage.sql.mappings.tables.records.ProfileEquippedRecord;
import io.lumine.cosmetics.storage.sql.mappings.tables.records.ProfileRecord;

import io.lumine.utils.lib.jooq.ForeignKey;
import io.lumine.utils.lib.jooq.TableField;
import io.lumine.utils.lib.jooq.UniqueKey;
import io.lumine.utils.lib.jooq.impl.DSL;
import io.lumine.utils.lib.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in the
 * default schema.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<ProfileRecord> KEY_MCCOSMETICS_PROFILE_PRIMARY = Internal.createUniqueKey(MccosmeticsProfile.MCCOSMETICS_PROFILE, DSL.name("KEY_mccosmetics_profile_PRIMARY"), new TableField[] { MccosmeticsProfile.MCCOSMETICS_PROFILE.UUID }, true);
    public static final UniqueKey<ProfileEquippedRecord> KEY_MCCOSMETICS_PROFILE_EQUIPPED_PRIMARY = Internal.createUniqueKey(MccosmeticsProfileEquipped.MCCOSMETICS_PROFILE_EQUIPPED, DSL.name("KEY_mccosmetics_profile_equipped_PRIMARY"), new TableField[] { MccosmeticsProfileEquipped.MCCOSMETICS_PROFILE_EQUIPPED.PROFILE_UUID, MccosmeticsProfileEquipped.MCCOSMETICS_PROFILE_EQUIPPED.SLOT }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<ProfileEquippedRecord, ProfileRecord> MCCOSMETICS_PROFILE_EQUIPPED_PROFILE_FK = Internal.createForeignKey(MccosmeticsProfileEquipped.MCCOSMETICS_PROFILE_EQUIPPED, DSL.name("mccosmetics_profile_equipped_profile_fk"), new TableField[] { MccosmeticsProfileEquipped.MCCOSMETICS_PROFILE_EQUIPPED.PROFILE_UUID }, Keys.KEY_MCCOSMETICS_PROFILE_PRIMARY, new TableField[] { MccosmeticsProfile.MCCOSMETICS_PROFILE.UUID }, true);
}
