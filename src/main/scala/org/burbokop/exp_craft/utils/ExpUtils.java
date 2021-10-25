package org.burbokop.exp_craft.utils;

import net.minecraft.entity.player.EntityPlayer;

public class ExpUtils {
	public static boolean canDecreaseExp(EntityPlayer player, float amount) {
		return player.experienceTotal >= amount;
	}
	
	public static boolean decreaseExp(EntityPlayer player, float amount) {
		if (player.experienceTotal - amount < 0) {
			return false;
		}

		player.experienceTotal -= amount;

		if (player.experience * (float)player.xpBarCap() <= amount) {
			amount -= player.experience * (float)player.xpBarCap();
			player.experience = 1.0f;
			player.experienceLevel--;
		}

		while (player.xpBarCap() < amount) {
			amount -= player.xpBarCap();
			player.experienceLevel--;
		}

		player.experience -= amount / (float)player.xpBarCap();
		return true;
	}
}
