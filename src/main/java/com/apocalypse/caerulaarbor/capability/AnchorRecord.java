package com.apocalypse.caerulaarbor.capability;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.ArrayList;
import java.util.List;

@AutoRegisterCapability
public class AnchorRecord {
    private final Long2ObjectMap<List<BlockPos>> section2anchorPosMap = new Long2ObjectOpenHashMap<>();
    public static ResourceLocation ID = CaerulaArborMod.loc("anchor_record");
    private static Iterable<SectionPos> sectionsInAnchorRange(BlockPos anchorPos) {
        int x1 = SectionPos.blockToSectionCoord(anchorPos.getX() - 24);
        int y1 = SectionPos.blockToSectionCoord(anchorPos.getY() - 8);
        int z1 = SectionPos.blockToSectionCoord(anchorPos.getZ() - 24);
        int x2 = SectionPos.blockToSectionCoord(anchorPos.getX() + 24);
        int y2 = SectionPos.blockToSectionCoord(anchorPos.getY() + 8);
        int z2 = SectionPos.blockToSectionCoord(anchorPos.getZ() + 24);
        return () -> SectionPos.betweenClosedStream(x1, y1, z1, x2, y2, z2).iterator();
    }
    public void addAnchor(BlockPos anchorPos) {
        for (SectionPos sectionPos : sectionsInAnchorRange(anchorPos)) {
            section2anchorPosMap.computeIfAbsent(sectionPos.asLong(), l -> new ArrayList<>()).add(anchorPos);
        }
    }
    public void removeAnchor(BlockPos anchorPos) {
        for (SectionPos sectionPos : sectionsInAnchorRange(anchorPos)) {
            List<BlockPos> anchorList = section2anchorPosMap.get(sectionPos.asLong());
            anchorList.remove(anchorPos);
        }
    }
    public boolean affectedByAnchor(BlockPos blockPos) {
        long sectionIn = SectionPos.asLong(blockPos);
        if (!section2anchorPosMap.containsKey(sectionIn)) return false;
        for (BlockPos anchorPos : section2anchorPosMap.get(sectionIn)) {
            if (Math.abs(anchorPos.getX() - blockPos.getX()) > 24) continue;
            if (Math.abs(anchorPos.getY() - blockPos.getY()) > 8) continue;
            if (Math.abs(anchorPos.getZ() - blockPos.getZ()) > 24) continue;
            return true;
        }
        return false;
    }
}
