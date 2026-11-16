package com.rwtema.funkylocomotion.api;

import com.mojang.authlib.GameProfile;
import javax.annotation.Nullable;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IMoveCheck {
    EnumActionResult canMove(World worldObj, BlockPos pos, @Nullable GameProfile profile);
}
