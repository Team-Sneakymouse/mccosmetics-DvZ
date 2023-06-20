package io.lumine.cosmetics.managers.gestures;

import com.google.common.collect.Lists;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.commands.CommandHelper;
import io.lumine.cosmetics.config.Scope;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.cosmetics.managers.AbstractCosmetic;
import io.lumine.utils.config.properties.Property;
import io.lumine.utils.config.properties.types.BooleanProp;
import io.lumine.utils.config.properties.types.EnumProp;
import io.lumine.utils.config.properties.types.StringProp;
import io.lumine.utils.menu.Icon;
import io.lumine.utils.menu.IconBuilder;
import io.lumine.utils.text.Text;
import lombok.Getter;

import java.io.File;
import java.util.List;

public class Gesture extends AbstractCosmetic {


	private static final StringProp DEFAULT = Property.String(Scope.NONE, "Gesture");
	private static final StringProp SLIM = Property.String(Scope.NONE, "Slim");
	private static final BooleanProp CAN_MOVE = Property.Boolean(Scope.NONE, "CanMove", false);
	private static final BooleanProp CAN_LOOK = Property.Boolean(Scope.NONE, "CanLook", false);
	private static final EnumProp<QuitMethod> QUIT_CONTROL = Property.Enum(Scope.NONE, QuitMethod.class,"QuitControl", QuitMethod.SNEAK);

	@Getter private final String defaultGesture;
	@Getter private final String slimGesture;
	@Getter private final boolean canMove;
	@Getter private final boolean canLook;
	@Getter private final QuitMethod quitMethod;

	public Gesture(GestureManager manager, File file, String key) {
		super(manager, file, CosmeticType.type(Gesture.class), key);

		defaultGesture = DEFAULT.fget(file, this);

		String temp = SLIM.fget(file, this);
		if(temp == null || temp.isEmpty())
			temp = defaultGesture;
		slimGesture = temp;

		canMove = CAN_MOVE.fget(file, this);
		canLook = CAN_LOOK.fget(file, this);
		quitMethod = QUIT_CONTROL.fget(file, this);
	}

	@Override
	public String getPropertyNode() {
		return this.key;
	}

	@Override
	public Icon<CosmeticProfile> getIcon() {
		return IconBuilder.<CosmeticProfile>create()
				.name(Text.colorize(this.getDisplay()))
				.itemStack(this.menuItem)
				.hideFlags()
				.lore(prof -> {
					List<String> desc = Lists.newArrayList(description);
					if(!prof.has(this)) {
						desc.add("");
						desc.add(Text.colorizeLegacy("<red>Not Unlocked"));
					}
					return desc;
				})
				.click((prof,p) -> {
					if(prof.getPlayer().isOp() || prof.has(this)) {
						prof.equip(this);
						((GestureManager) getManager()).playGesture(prof);
						CommandHelper.sendSuccess(p, "Now playing gesture: " + getDisplay());
						p.closeInventory();
					} else {
						CommandHelper.sendError(p, "You haven't unlocked that gesture yet!");
					}
				}).build();
	}
}
