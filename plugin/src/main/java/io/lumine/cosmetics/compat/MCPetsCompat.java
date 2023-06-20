package io.lumine.cosmetics.compat;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.cosmetics.pets.PetHook;

import org.bukkit.entity.Player;

import fr.nocsy.mcpets.MCPets;
import fr.nocsy.mcpets.api.MCPetsAPI;
import fr.nocsy.mcpets.data.Pet;

public class MCPetsCompat {

    private final MCCosmeticsPlugin plugin;
    private final MCPets mcpets;
    
    public MCPetsCompat(MCCosmeticsPlugin plugin) {
        this.plugin = plugin;
        this.mcpets = MCPets.getInstance();
    }
    
    public MCPet get(String id) {
        return new MCPet(id);
    }
    
    public class MCPet implements PetHook {

        public Pet pet;
        
        public MCPet(String id) {
            pet = MCPetsAPI.getObjectPet(id);
        }
        
        @Override
        public boolean isValid() {
            return pet != null;
        }
        
        @Override
        public void equip(Player player) {
            MCPetsAPI.setActivePet(pet, player, false);
        }
    }
}
