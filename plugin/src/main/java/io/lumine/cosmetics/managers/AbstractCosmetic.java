package io.lumine.cosmetics.managers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.api.cosmetics.CosmeticVariant;
import io.lumine.cosmetics.api.cosmetics.EquippedCosmetic;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.commands.CommandHelper;
import io.lumine.cosmetics.config.Scope;
import io.lumine.cosmetics.constants.Permissions;
import io.lumine.cosmetics.menus.CosmeticMenu;
import io.lumine.utils.Schedulers;
import io.lumine.utils.config.properties.Property;
import io.lumine.utils.config.properties.types.*;
import io.lumine.utils.items.ItemFactory;
import io.lumine.utils.menu.Icon;
import io.lumine.utils.menu.IconBuilder;
import io.lumine.utils.text.Text;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractCosmetic extends Cosmetic {

    protected static final StringProp NAMESPACE = Property.String(Scope.NONE, "Namespace", null);
    protected static final StringProp PERMISSION = Property.String(Scope.NONE, "Permission", null);
	protected static final EnumProp<Material> MATERIAL = Property.Enum(Scope.NONE, Material.class, "Material", Material.EMERALD);
	protected static final IntProp MODEL = Property.Int(Scope.NONE, "Model");
	protected static final LangProp DISPLAY = Property.Lang(Scope.NONE, "Display");
	protected static final LangListProp DESCRIPTION = Property.LangList(Scope.NONE, "Description");
	protected static final StringProp TEXTURE = Property.String(Scope.NONE, "SkullTexture");

    protected static final StringProp COLOR = Property.String(Scope.NONE, "Color");
    protected static final BooleanProp COLORABLE = Property.Boolean(Scope.NONE, "Colorable", false);

    protected static final NodeListProp VARIANTS = Property.NodeList(Scope.NONE, "Variants");

    private final MCCosmeticsManager manager;
    
	protected final File file;
    @Getter protected final String id;
	@Getter protected final String key;
	@Getter protected final String namespace;
	@Getter protected final String permission;
	@Getter protected final List<String> sources = Lists.newArrayList();

	// Menu Item
	protected Material material;
	protected int model;
	@Getter protected String display;
	protected List<String> description;
    protected String color;
    
	@Getter protected ItemStack menuItem;
	
	@Getter protected boolean colorable;
	protected Map<String,CosmeticVariant> variants = Maps.newConcurrentMap();

	public AbstractCosmetic(MCCosmeticsManager manager, File file, String type, String key) {
		super(manager, type, key);
		this.manager = manager;
		this.file = file;
		this.key = key;
		this.namespace = NAMESPACE.fget(file,this);
		
		if(namespace == null) {
		    this.id = key;
		} else {
		    this.id = namespace + "." + key;
		}
		
		String perm = PERMISSION.fget(file,this);
		
		if(perm == null) {
		    if(namespace == null) {
                this.permission = Permissions.COSMETIC_PERMISSION_PREFIX + key.toLowerCase();
		    } else {
		        this.permission = Permissions.COSMETIC_PERMISSION_PREFIX + namespace.toLowerCase() + "." + key.toLowerCase();
		    }
		} else {
		    this.permission = perm;
		}
		
		this.material = MATERIAL.fget(file,this);
		this.model = MODEL.fget(file,this);
		this.display = DISPLAY.fget(file,this);
		this.description = DESCRIPTION.fget(file,this);
        this.color = COLOR.fget(file,this);
		this.colorable = COLORABLE.fget(file,this);

		for(String node : VARIANTS.fget(file,this)) {
		    var variant = new AbstractCosmeticVariant(this,file,node);
		    variants.put(variant.getKey(), variant);
		}
		
		if(material == Material.PLAYER_HEAD) {
			this.menuItem = ItemFactory.of(this.material)
					.name(Text.colorize(this.getDisplay()))
					.model(model)
					.hideAttributes()
					.lore(description)
					.skullTexture(TEXTURE.get(this))
					.build();
		} else {
			this.menuItem = ItemFactory.of(this.material)
					.name(Text.colorize(this.getDisplay()))
					.model(model)
					.hideAttributes()
					.lore(description)
					.build();
		}
	}

	protected Icon<CosmeticProfile> buildIcon(String type) {
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
				.click((prof,player) -> {
					if(prof.getPlayer().isOp() || prof.has(this)) {
					    CosmeticMenu.playMenuClick(player);
					    if(manager.getWardrobeManager().isInWardrobe(player)) {
					        var mannequin = manager.getWardrobeManager().getMannequin(player);
					        
					        manager.equipMannequin(mannequin, new EquippedCosmetic(this));
                            CommandHelper.sendSuccess(player, "Set wardrobe " + type + " to " + getDisplay());
    					} else {
    					    if(MCCosmeticsPlugin.inst().getConfiguration().getEquipDelay() > 0) {
    					        Schedulers.sync().runLater(() -> {
    					            prof.equip(this);
    					        }, MCCosmeticsPlugin.inst().getConfiguration().getEquipDelay());
    					    } else {
    					        prof.equip(this);
    					    }
    						CommandHelper.sendSuccess(player, "Set your " + type + " to " + getDisplay());
    					}
						player.closeInventory();
					} else {
						CommandHelper.sendError(player, Property.String(Scope.CONFIG,
								"Configuration.Language.Cosmetic-Not-Unlocked","You haven't unlocked that cosmetic!").get());
					}
				})
				.rightClick((prof,player) -> {
                    if(prof.getPlayer().isOp() || prof.has(this)) {
                        
                    } else {
                        CommandHelper.sendError(player, Property.String(Scope.CONFIG,
                                "Configuration.Language.Cosmetic-Not-Unlocked","You haven't unlocked that cosmetic!").get());
                    }
                }).build();
	}

	public boolean hasVariants() {
	    return variants.isEmpty() == false;
	}
	
	public Optional<CosmeticVariant> getVariant(String id) {
	    return Optional.ofNullable(variants.getOrDefault(id, null));
	}

}
