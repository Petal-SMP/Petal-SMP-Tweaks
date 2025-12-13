package com.macuguita.petal_smp.injected_interfaces;

import com.mojang.serialization.DataResult;

import net.minecraft.nbt.CompoundTag;

import java.util.UUID;
import java.util.function.Consumer;

public interface CustomPlayerDataStorage {

	DataResult<CompoundTag> petal$edit(UUID uuid, Consumer<CompoundTag> editor);

	CompoundTag petal$getNbt(UUID uuid);
}
