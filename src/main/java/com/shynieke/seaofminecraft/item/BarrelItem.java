package com.shynieke.seaofminecraft.item;

import com.shynieke.seaofminecraft.entity.placeable.GunpowderBarrelEntity;
import com.shynieke.seaofminecraft.registry.SoMCRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BarrelItem extends Item {
	public BarrelItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		HitResult traceResult = getPlayerPOVHitResult(level, playerIn, ClipContext.Fluid.NONE);
		if (traceResult == null) {
			return InteractionResultHolder.pass(stack);
		} else if (traceResult.getType() != HitResult.Type.BLOCK) {
			return InteractionResultHolder.pass(stack);
		} else {
			BlockHitResult blockTraceResult = (BlockHitResult) traceResult;
			Vec3 pos = blockTraceResult.getLocation();
			GunpowderBarrelEntity gunpowderBarrel = new GunpowderBarrelEntity(SoMCRegistry.GUNPOWDER_BARREL.get(), level,
					pos.x, pos.y, pos.z);
			gunpowderBarrel.setYRot(playerIn.getYRot());

			if (!level.isClientSide) {
				level.addFreshEntity(gunpowderBarrel);

				if (!playerIn.isCreative()) {
					stack.shrink(1);
				}
			}
			return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
		}
	}
}
