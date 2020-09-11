package draylar.gateofbabylon.item;

import draylar.gateofbabylon.GateOfBabylon;
import draylar.gateofbabylon.registry.GOBEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RapierItem extends SwordItem {

    public RapierItem(ToolMaterial material, int attackDamage, float attackSpeed) {
        super(material, attackDamage, attackSpeed, new Item.Settings().group(GateOfBabylon.GROUP).maxCount(1));
    }

    public RapierItem(ToolMaterial material, float effectiveDamage, float effectiveSpeed) {
        super(material, (int) (effectiveDamage - material.getAttackDamage() - 1), -4 + effectiveSpeed, new Item.Settings().group(GateOfBabylon.GROUP).maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.NEUTRAL, 0.05F, 1.75F / (RANDOM.nextFloat() * 0.4F + 0.8F));
        user.getItemCooldownManager().set(this, 40);

        if (!world.isClient) {
            // get Lunging bonus (1 = 2x, 2 = 3x, 3, = 4x)
            int enchantmentBonus = EnchantmentHelper.getEquipmentLevel(GOBEnchantments.LUNGING, user) + 1;

            // move player
            Vec3d rotation = user.getRotationVector().multiply(enchantmentBonus);
            user.addVelocity(rotation.x, rotation.y, rotation.z);
            user.velocityModified = true;
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        return TypedActionResult.success(itemStack);
    }
}