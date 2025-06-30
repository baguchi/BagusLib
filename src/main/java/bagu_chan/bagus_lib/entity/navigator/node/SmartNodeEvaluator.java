package bagu_chan.bagus_lib.entity.navigator.node;

import bagu_chan.bagus_lib.entity.ISmartJump;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class SmartNodeEvaluator extends WalkNodeEvaluator {
    private final Object2BooleanMap<AABB> collisionCache = new Object2BooleanOpenHashMap<>();

    @Override
    public void done() {
        this.collisionCache.clear();
        super.done();
    }

    /*
     * this method include jump check with support any height. when 1 block height is failed check 2 block height
     */
    @Nullable
    protected Node findAcceptedNode(int x, int y, int z, int verticalDeltaLimit, double nodeFloorLevel, Direction direction, BlockPathTypes pathType) {
        Node node = null;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        double d0 = this.getFloorLevel(blockpos$mutableblockpos.set(x, y, z));
        if (d0 - nodeFloorLevel > this.getMobJumpHeight()) {
            return null;
        } else {
            BlockPathTypes pathtype = this.getCachedBlockType(this.mob, x, y, z);
            float f = this.mob.getPathfindingMalus(pathtype);
            if (f >= 0.0F) {
                node = this.getNodeAndUpdateCostToMax(x, y, z, pathtype, f);
            }

            if (doesBlockHavePartialCollision(pathType) && node != null && node.costMalus >= 0.0F && !this.canReachWithoutCollision(node)) {
                node = null;
            }

            if (pathtype != BlockPathTypes.WALKABLE && (!this.isAmphibious() || pathtype != BlockPathTypes.WATER)) {
                if ((node == null || node.costMalus < 0.0F)
                        && verticalDeltaLimit > 0
                        && (pathtype != BlockPathTypes.FENCE || this.canWalkOverFences())
                        && pathtype != BlockPathTypes.UNPASSABLE_RAIL
                        && pathtype != BlockPathTypes.TRAPDOOR
                        && pathtype != BlockPathTypes.POWDER_SNOW) {
                    //node = this.tryJumpOn(x, y, z, verticalDeltaLimit, nodeFloorLevel, direction, pathType, blockpos$mutableblockpos);

                    //second jump start
                    for (int height = 0; height < Mth.floor(this.getMobJumpHeight()); height++) {
                        if (node == null) {
                            node = this.tryJumpOn(x, y + height, z, verticalDeltaLimit, nodeFloorLevel, direction, pathType, blockpos$mutableblockpos);
                        }
                    }
                } else if (!this.isAmphibious() && pathtype == BlockPathTypes.WATER && !this.canFloat()) {
                    node = this.tryFindFirstNonWaterBelow(x, y, z, node);
                } else if (pathtype == BlockPathTypes.OPEN) {
                    node = this.tryFindFirstGroundNodeBelow(x, y, z);
                } else if (doesBlockHavePartialCollision(pathtype) && node == null) {
                    node = this.getClosedNode(x, y, z, pathtype);
                }

                return node;
            } else {
                return node;
            }
        }
    }

    private Node getNodeAndUpdateCostToMax(int x, int y, int z, BlockPathTypes pathType, float malus) {
        Node node = this.getNode(x, y, z);
        node.type = pathType;
        node.costMalus = Math.max(node.costMalus, malus);
        return node;
    }

    private Node getBlockedNode(int x, int y, int z) {
        Node node = this.getNode(x, y, z);
        node.type = BlockPathTypes.BLOCKED;
        node.costMalus = -1.0F;
        return node;
    }

    private Node getClosedNode(int x, int y, int z, BlockPathTypes pathType) {
        Node node = this.getNode(x, y, z);
        node.closed = true;
        node.type = pathType;
        node.costMalus = pathType.getMalus();
        return node;
    }

    @Nullable
    private Node tryJumpOn(
            int x,
            int y,
            int z,
            int verticalDeltaLimit,
            double nodeFloorLevel,
            Direction direction,
            BlockPathTypes pathType,
            BlockPos.MutableBlockPos pos
    ) {
        Node node = this.findAcceptedNode(x, y + 1, z, verticalDeltaLimit - 1, nodeFloorLevel, direction, pathType);
        if (node == null) {
            return null;
        } else if (this.mob.getBbWidth() >= 1.0F) {
            return node;
        } else if (node.type != BlockPathTypes.OPEN && node.type != BlockPathTypes.WALKABLE) {
            return node;
        } else {
            double d0 = (double) (x - direction.getStepX()) + 0.5;
            double d1 = (double) (z - direction.getStepZ()) + 0.5;
            double d2 = (double) this.mob.getBbWidth() / 2.0;
            AABB aabb = new AABB(
                    d0 - d2,
                    this.getFloorLevel(pos.set(d0, (double) (y + 1), d1)) + 0.001,
                    d1 - d2,
                    d0 + d2,
                    (double) this.mob.getBbHeight() + this.getFloorLevel(pos.set((double) node.x, (double) node.y, (double) node.z)) - 0.002,
                    d1 + d2
            );
            return this.hasCollisions(aabb) ? null : node;
        }
    }

    @Nullable
    private Node tryFindFirstNonWaterBelow(int x, int y, int z, @Nullable Node node) {
        y--;

        while (y > this.mob.level().getMinBuildHeight()) {
            BlockPathTypes pathtype = this.getCachedBlockType(this.mob, x, y, z);
            if (pathtype != BlockPathTypes.WATER) {
                return node;
            }

            node = this.getNodeAndUpdateCostToMax(x, y, z, pathtype, this.mob.getPathfindingMalus(pathtype));
            y--;
        }

        return node;
    }

    private Node tryFindFirstGroundNodeBelow(int x, int y, int z) {
        for (int i = y - 1; i >= this.mob.level().getMinBuildHeight(); i--) {
            if (y - i > this.mob.getMaxFallDistance()) {
                return this.getBlockedNode(x, i, z);
            }

            BlockPathTypes pathtype = this.getCachedBlockType(this.mob, x, i, z);
            float f = this.mob.getPathfindingMalus(pathtype);
            if (pathtype != BlockPathTypes.OPEN) {
                if (f >= 0.0F) {
                    return this.getNodeAndUpdateCostToMax(x, i, z, pathtype, f);
                }

                return this.getBlockedNode(x, i, z);
            }
        }

        return this.getBlockedNode(x, y, z);
    }

    private boolean hasCollisions(AABB boundingBox) {
        return this.collisionCache.computeIfAbsent(boundingBox, p_330163_ -> !this.level.noCollision(this.mob, boundingBox));
    }

    private double getMobJumpHeight() {
        //smart jump
        if (this.mob instanceof ISmartJump smartJump) {
            return Math.max(smartJump.getSuppportJump(), (double) this.mob.maxUpStep());
        }

        return Math.max(1.125, (double) this.mob.maxUpStep());
    }

    private static boolean doesBlockHavePartialCollision(BlockPathTypes pathType) {
        return pathType == BlockPathTypes.FENCE || pathType == BlockPathTypes.DOOR_WOOD_CLOSED || pathType == BlockPathTypes.DOOR_IRON_CLOSED;
    }

    private boolean canReachWithoutCollision(Node p_77625_) {
        AABB aabb = this.mob.getBoundingBox();
        Vec3 vec3 = new Vec3((double) p_77625_.x - this.mob.getX() + aabb.getXsize() / (double) 2.0F, (double) p_77625_.y - this.mob.getY() + aabb.getYsize() / (double) 2.0F, (double) p_77625_.z - this.mob.getZ() + aabb.getZsize() / (double) 2.0F);
        int i = Mth.ceil(vec3.length() / aabb.getSize());
        vec3 = vec3.scale((double) (1.0F / (float) i));

        for (int j = 1; j <= i; ++j) {
            aabb = aabb.move(vec3);
            if (this.hasCollisions(aabb)) {
                return false;
            }
        }

        return true;
    }
}