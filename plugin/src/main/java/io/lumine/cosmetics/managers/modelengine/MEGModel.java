package io.lumine.cosmetics.managers.modelengine;

public class MEGModel /*extends AbstractModeledEntity*/ {

	/*private final Player player;
	private final Query queries = new Query(this);
	private final IMountHandler mountHandler;
	private final INametagHandler nametagHandler;

	private final boolean isInit;

	public MEGModel(Player player, BaseEntity<?> entity) {
		super(entity);
		this.player = player;
		mountHandler = new EmptyMountHandler();
		nametagHandler = new EmptyNametagHandler();
		isInit = true;
	}

	@Override
	public void clearModels() {
		super.clearModels();
		updateIDList();
	}

	@Override
	public void removeModel(String modelId) {
		super.removeModel(modelId);
		updateIDList();
	}

	@Override
	public void setWalking(boolean walking) {
		moveEventFired = walking;
	}

	@Override
	public boolean tick() {
		if(!isInit)
			return true;

		if(!deathState) {
			if (getEntity().isDead()) {
				mountHandler.dismountAll();
				for (ActiveModel model : models.values())
					model.setState(ActiveModel.ModelState.DEATH);
				deathState = true;
			}else if (isWalking()) {
				bodyAngle = calBodyAngle();
			}
		}

		getNametagHandler().updateTags();

		double x = getEntity().getLastX();
		double y = getEntity().getLastY();
		double z = getEntity().getLastZ();
		Location loc = getEntity().getLocation();
		boolean moving = x != loc.getX() || y != loc.getY() || z != loc.getZ();
		setWalking(moving);

		for(ActiveModel model : models.values()) {
			model.tick();
		}

		if(hurtTick >= 0) {
			hurtTick--;
		}

		return !(deathState && canRemove());
	}

	@Override
	public boolean addActiveModel(ActiveModel model) {
		if(super.addActiveModel(model)) {
			updateIDList();
			return true;
		}
		return false;
	}

	@Override
	public List<Player> getPlayerInRange() {
		final var list = super.getPlayerInRange();
		list.add(player);
		return list;
	}

	@Override
	public IMountHandler getMountHandler() {
		return mountHandler;
	}

	@Override
	public INametagHandler getNametagHandler() {
		return nametagHandler;
	}

	private void updateIDList() {
		getEntity().saveModelInfo(this);
	}

	private double calBodyAngle() {
		Vector nV = new Vector(getEntity().getLocation().getX() - getEntity().getLastX(), 0, getEntity().getLocation().getZ() - getEntity().getLastZ());
		// Jitter: getLastX/Z() is returning same value as getX/Z(), making nV = 0 0
		if(nV.getX() == 0 && nV.getZ() == 0) {
			nV = new Vector(getEntity().getLocation().getX() - getEntity().getWantedX(), 0, getEntity().getLocation().getZ() - getEntity().getWantedZ());
			if(nV.getX() == 0 && nV.getZ() == 0)
				return getBodyAngle();
		}
		Vector iV = nV.clone().multiply(-1);
		Vector norm = getEntity().getLocation().getDirection().setY(0).normalize();
		Vector dNV = nV.clone().add(norm);
		Vector dIV = iV.clone().add(norm);
		double dN = dNV.lengthSquared();
		double dI = dIV.lengthSquared();
		if(dN > dI || Math.abs(dI - dN) <= 0.05f) {
			return Math.atan2(-nV.getX(), nV.getZ());
		}else {
			return Math.atan2(-iV.getX(), iV.getZ());
		}
	}

	private boolean canRemove() {
		for (ActiveModel model : models.values()) {
			if (!model.canRemove())
				return false;
		}
		return true;
	}

	// Query
	@Override
	public Query getQuery() {
		return queries;
	}

	@Override
	public double getValue(int ix, String[] id) {
		if(id.length <= ix)
			return 0;
		String v = id[ix].toLowerCase();
		return switch (v) {
			case "walk_speed", "speed" -> getWalkSpeedSquared();
			case "walk_speed_real", "speed_real" -> getWalkSpeed();
			case "health", "hp" -> getHealth();
			case "max_health", "max_hp" -> getMaxHealth();
			default -> 0;
		};
	}

	private double getWalkSpeedSquared() {
		if(!getEntity().isLivingEntity())
			return 0;
		return getEntity().getVelocity().setY(0).lengthSquared();
	}

	private double getWalkSpeed() {
		return Math.sqrt(getWalkSpeedSquared());
	}

	private double getHealth() {
		if(!getEntity().isLivingEntity())
			return 0;
		return getEntity().getHealth();
	}

	private double getMaxHealth() {
		if(!getEntity().isLivingEntity())
			return 0;
		return getEntity().getMaxHealth();
	}*/
}
