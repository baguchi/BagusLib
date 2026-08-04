package baguchi.bagus_lib.client;

import baguchi.bagus_lib.BagusConfigs;
import baguchi.bagus_lib.BagusLib;
import baguchi.bagus_lib.CommonEvent;
import baguchi.bagus_lib.animation.BaguAnimationController;
import baguchi.bagus_lib.animation.client.BaguKeyFrameController;
import baguchi.bagus_lib.client.event.BagusModelEvent;
import baguchi.bagus_lib.client.game.WaterMelonScreen;
import baguchi.bagus_lib.client.render.book.Book;
import baguchi.bagus_lib.client.render.book.component.BookComponentDefinition;
import baguchi.bagus_lib.client.render.book.component.DialogBookComponent;
import baguchi.bagus_lib.client.render.book.component.DisplayBookComponent;
import baguchi.bagus_lib.client.render.screen.BookScreen;
import baguchi.bagus_lib.util.DialogHandler;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.Date;

@EventBusSubscriber(value = Dist.CLIENT, modid = BagusLib.MODID)
public class ClientEventHandler {
    private static boolean initDate = false;

    private static boolean aprilFools = false;

    @SubscribeEvent
    public static void screenRender(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof TitleScreen titleScreen && isAprilFools()) {
            int l = titleScreen.height / 4 + 28;
            event.addListener(Button.builder(Component.translatable("bagus_lib.watermelon"), p_280785_ -> Minecraft.getInstance().setScreenAndShow(new WaterMelonScreen(Component.empty())))
                    .bounds(titleScreen.width / 2 + 100, l + 4, 100, 20)
                    .build());
        }
    }

    public static void handleOpenPageTest(Player player) {
        if (player.level().isClientSide() && player == Minecraft.getInstance().player) {
            DisplayBookComponent title = new DisplayBookComponent(121, 158)
                    .imageDisplay(BagusLib.prefix("textures/gui/sprites/test.png"), 4, 10, 100, 100)
                    .textDisplay(Component.literal("test"), 52, 115, 1.2f);

            DialogBookComponent test2 = new DialogBookComponent(Component.literal("test2. testetteafrafdadadsadadasad"), false, 100, 158);

            Book book = new Book(Lists.newArrayList(
                    new BookComponentDefinition(title, BagusLib.prefix("title"), 10, 10, 10, 10)
                    , new BookComponentDefinition(test2, BagusLib.prefix("test2"), 10, 10, 10, 10)

            ), 256, 182, 23, 13, 12, 27,
                    BagusLib.prefix("textures/gui/screen/book/book.png"),
                    BagusLib.prefix("textures/gui/screen/book/book_back.png"),
                    BagusLib.prefix("textures/gui/screen/book/book_back.png"),
                    Identifier.withDefaultNamespace("textures/gui/sprites/widget/page_backward.png"),
                    Identifier.withDefaultNamespace("textures/gui/sprites/widget/page_forward.png"));
            Minecraft.getInstance().setScreenAndShow(new BookScreen(book));
        }
    }

    public static boolean isAprilFools() {
        if (!initDate) {
            initDate = true;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            aprilFools = calendar.get(Calendar.MONTH) + 1 == 4 && calendar.get(Calendar.DATE) == 1;
        }
        return aprilFools && BagusConfigs.COMMON.aprilFool.get() || BagusConfigs.COMMON.alwayAplilFool.get();
    }

    @SubscribeEvent
    public static void clientLoggOut(PlayerEvent.PlayerLoggedOutEvent event) {
        DialogHandler.INSTANCE.removeAllDialogType();
    }

    @SubscribeEvent
    public static void clientRespawn(PlayerEvent.PlayerRespawnEvent event) {
        DialogHandler.INSTANCE.removeAllDialogType();
    }


    @SubscribeEvent
    public static void onAnimateModelEvent(BagusModelEvent.PostAnimate event) {
        BaguAnimationController animations = event.getBaguAnimationController();
        @Nullable BaguKeyFrameController keyFrames = event.getBaguKeyframeController();
        if (keyFrames != null) {
            KeyframeAnimation pat = keyFrames.getKeyframe(CommonEvent.PAT);
            if (animations != null) {
                if (pat != null) {
                    if (animations.getAnimationState(CommonEvent.PAT).isStarted()) {
                        event.getModel().root().getChild("right_arm").resetPose();
                        event.getModel().root().getChild("left_arm").resetPose();


                        pat.apply(animations.getAnimationState(CommonEvent.PAT), event.getEntityRenderState().ageInTicks);
                    }
                }
            }
        }
    }
}
