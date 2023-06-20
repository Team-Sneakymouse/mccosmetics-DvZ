package io.lumine.cosmetics.managers.pets;

import io.lumine.cosmetics.api.cosmetics.pets.PetHook;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.config.Scope;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.cosmetics.managers.AbstractCosmetic;
import io.lumine.utils.config.properties.Property;
import io.lumine.utils.config.properties.types.StringProp;
import io.lumine.utils.menu.Icon;
import lombok.Getter;

import java.io.File;

public class Pet extends AbstractCosmetic {

    private static final StringProp TYPE = Property.String(Scope.NONE, "Type", "MCPETS");
	private static final StringProp PET = Property.String(Scope.NONE, "Id");

	@Getter private final String petId;
    @Getter private final PetType petType;
	
	@Getter private PetHook spawner;

	public Pet(PetManager manager, File file, String key) {
		super(manager, file, CosmeticType.type(Pet.class), key);

		petId = PET.fget(file, this);
		petType = PetType.parse(TYPE.fget(file, this));

	    if(petType == PetType.MCPETS && manager.getPlugin().getCompatibility().getMcpets().isPresent()) {
	        spawner = manager.getPlugin().getCompatibility().getMcpets().get().get(petId);
	    } else {
	        
	    }
	}

	@Override
	public String getPropertyNode() {
		return key;
	}

	@Override
	public Icon<CosmeticProfile> getIcon() {
		return buildIcon("pet");
	}
}
