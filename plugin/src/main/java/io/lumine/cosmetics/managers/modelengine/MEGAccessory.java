package io.lumine.cosmetics.managers.modelengine;

import com.ticxo.modelengine.api.model.AnimationMode;
import io.lumine.cosmetics.api.cosmetics.ColorableCosmetic;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.config.Scope;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.cosmetics.managers.AbstractCosmetic;
import io.lumine.utils.config.properties.Property;
import io.lumine.utils.config.properties.types.EnumProp;
import io.lumine.utils.config.properties.types.OrientProp;
import io.lumine.utils.config.properties.types.StringProp;
import io.lumine.utils.menu.Icon;
import io.lumine.utils.serialize.Orient;
import lombok.Getter;

import java.io.File;

public class MEGAccessory extends AbstractCosmetic implements ColorableCosmetic {

	private static final StringProp MODEL = Property.String(Scope.NONE, "ModelId");
	private static final StringProp STATE = Property.String(Scope.NONE, "State");
	private static final OrientProp OFFSET = Property.Orient(Scope.NONE, "Offset");
	private static final EnumProp<AnimationMode> MODELMODE = Property.Enum(Scope.NONE, AnimationMode.class, "Mode", AnimationMode.B);
	private static final EnumProp<ModelAnchor> MODELANCHOR = Property.Enum(Scope.NONE, ModelAnchor.class, "Anchor", ModelAnchor.BODY);

	@Getter private final String modelId;
	@Getter private final String state;
	@Getter private final Orient offset;
	@Getter private final AnimationMode mode;
	@Getter private final ModelAnchor anchor;

	public MEGAccessory(MEGManager manager, File file, String key) {
		super(manager, file, CosmeticType.type(MEGAccessory.class), key);

		modelId = MODEL.fget(file, this);
		state = STATE.fget(file, this);
		offset = OFFSET.fget(file, this);
		mode = MODELMODE.fget(file, this);
		anchor = MODELANCHOR.fget(file, this);

	}

	@Override
	public String getPropertyNode() {
		return key;
	}

	@Override
	public Icon<CosmeticProfile> getIcon() {
		return buildIcon("model engine accessory");
	}

}
