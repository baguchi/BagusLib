package bagu_chan.bagus_lib.client.test;

//@EventBusSubscriber(value = Dist.CLIENT, modid = BagusLib.MODID)
public class ClientTestEventHandler {
/*
    @SubscribeEvent
    public static void modelEventInit(BagusModelEvent.Init event) {
        //IRootModel is working when that model extend HierarchicalModel, HumanoidMode or QuadrupedModel
        if (event.getModel() instanceof IRootModel rootModel) {
            rootModel.getBagusRoot().getAllParts().forEach(ModelPart::resetPose);
        }
    }

    @SubscribeEvent
    public static void modelEventInit(BagusModelEvent.PostAnimate event) {
        if (event.getModel() instanceof IRootModel rootModel) {
            rootModel.animateWalkBagu(TestAnimations.test, event.getAgeInTick(), 1.0F, 1.0F, 1.0F);
            if(event.getModel() instanceof PlayerModel<?> playerModel){
                playerModel.rightSleeve.copyFrom(playerModel.rightArm);
                playerModel.leftSleeve.copyFrom(playerModel.leftArm);
            }
        }
    }*/
}
