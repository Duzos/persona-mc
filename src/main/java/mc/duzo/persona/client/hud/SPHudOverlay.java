package mc.duzo.persona.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import mc.duzo.persona.PersonaMod;
import mc.duzo.persona.client.data.ClientData;
import mc.duzo.persona.data.PlayerData;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class SPHudOverlay implements HudRenderCallback {

    public static final Identifier SP_HUD = new Identifier(PersonaMod.MOD_ID, "textures/gui/sp_hud.png");
    public static final Identifier CHARACTER_ELEMENT = new Identifier(PersonaMod.MOD_ID, "textures/gui/character_element.png");

    public static final Identifier TARGET = new Identifier(PersonaMod.MOD_ID, "textures/gui/target_acquired.png");
    @Override
    public void onHudRender(DrawContext draw, float tickDelta) {
        // todo major code cleanup here :(
        // loqor please separate these by new lines + better variable names + comments

        int x = 0;
        int y = 0;
        MinecraftClient mc = MinecraftClient.getInstance();

        if (mc == null) return;

        int width = mc.getWindow().getScaledWidth();
        int height = mc.getWindow().getScaledHeight();

        int realWidth = mc.getWindow().getWidth();
        int realHeight = mc.getWindow().getHeight();

        x = width / 2;
        y = height;

        PlayerData data = ClientData.getPlayerState(mc.player);

        if (!data.isPersonaRevealed()) return;
        if (data.findPersona().isEmpty()) return;

        String spText =  data.getSP() < 100 ? data.getSP() < 10 ? "00" + data.getSP() : "0" + data.getSP() : "" + data.getSP();

        MatrixStack stack = draw.getMatrices();

        stack.push();
//        stack.scale(realWidth / 1920f, realHeight / 1080f, 1f);

        draw.drawTexture(SP_HUD, width - getScaled(362f, 1920, realWidth), 0,0,0, getScaled((227 / 1.25f), 1920, realWidth),(int) getScaled((101 / 1.25f), 1080, realHeight), (int) getScaled((227 / 1.25f), 1920, realWidth), (int) getScaled((324 / 1.25f), 1080, realHeight));
        draw.drawTexture(SP_HUD, width - getScaled(181f, 1920, realWidth), getScaled(19f, 1080, realHeight),0,getScaled(130f, 1080, realHeight),getScaled((227 / 1.25f), 1920, realWidth),(int) getScaled((101 / 1.25f), 1080, realHeight), (int) getScaled((227 / 1.25f), 1920, realWidth), (int) getScaled((324 / 1.25f), 1080, realHeight));
        if(data.getSP() > 100) {
            // @TODO this is temporary and it sucks so
            // TODO - scaling for whatever this is
            // its the bar that covers the SP blue bar, i just made it not render because yeah im lazy and didnt feel like making it move just yet
            draw.drawTexture(SP_HUD, (width - 337) + 22, (54) - 18, 25, 95, (int) (195 / 1.25f), (int) (43 / 1.25f), (int) (227 / 1.25f), (int) (324 / 1.25f));
            draw.drawTexture(SP_HUD, (width - 181) + 22, (72) - 18, 0, 224, (int) (195 / 1.25f), (int) (43 / 1.25f), (int) (227 / 1.25f), (int) (324 / 1.25f));
        }



        if(data.hasTarget()) {
            if (data.findTarget(mc.world).isPresent()) {
                LivingEntity found = data.findTarget(mc.world).get();
                String name = found.getDisplayName().getString().substring(1);
                String firstHighlight = found.getDisplayName().getString().substring(0,1).toUpperCase();
                Text level = Text.literal(String.valueOf(found instanceof PlayerEntity ? ((PlayerEntity) found).getNextLevelExperience() : 2)).formatted(Formatting.ITALIC);
                stack.push();
                int nameWidth = mc.textRenderer.getWidth(name) / 2;
                int levelWidth = mc.textRenderer.getWidth(level) / 2;
                int smt2 = mc.textRenderer.getWidth(firstHighlight) / 2;
                stack.translate(getScaled(240, 1920, realWidth), getScaled(47, 1080, realHeight), 0);
                stack.scale(getScaled(2.7f, 1920, realWidth), getScaled(2.7f, 1080, realHeight), 1);
                stack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(4.5f));
                draw.drawText(mc.textRenderer, name, -nameWidth, 0, 0xFFFFFF, true);
                stack.push();
                draw.drawText(mc.textRenderer, level, -levelWidth - 90, 1, 0xFFFFFF, true);
                stack.pop();
                stack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(3f));
                mc.textRenderer.draw(firstHighlight, -nameWidth -smt2 + -5, getScaled(-1, 1080, realHeight), 0, false, stack.peek().getPositionMatrix(), draw.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, Colors.WHITE, 0xF000F0, false);
                stack.translate(0, 0, 20);
                mc.textRenderer.draw(firstHighlight, -nameWidth -smt2 + -5, getScaled(-1, 1080, realHeight), Colors.BLACK, false, stack.peek().getPositionMatrix(), draw.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0, false);
                stack.pop();
            }
            draw.drawTexture(TARGET, 0, 0, 0, 0, getScaled(436, 1920, realWidth), getScaled(99, 1080, realHeight), getScaled(436, 1920, realWidth), getScaled(99, 1080, realHeight));
        }


        stack.push();
        stack.translate(0, 0, -300);
        draw.drawTexture(CHARACTER_ELEMENT, width - getScaledWidth(250), height - getScaledHeight(180) ,0,0,(int) (getScaledWidth(130 * 1.5f)),getScaledHeight(92 * 1.5f), getScaledWidth(130 * 1.5f),getScaledHeight(148 * 1.5f));
        stack.pop();

        this.drawEntity(draw, width + getScaledWidth(-200), height + getScaledHeight(70), getScaledHeight(110), 20, 0f, mc.player, tickDelta);

        draw.drawTexture(CHARACTER_ELEMENT, width + getScaledWidth(- 202), height + getScaledHeight(- 89),0,getScaledHeight(92 * 1.5f),getScaledWidth(130 * 1.5f),getScaledHeight(56 * 1.5f), getScaledWidth(130 * 1.5f),getScaledHeight(148 * 1.5f));

        stack.push();
        int scaledTextWidth = mc.textRenderer.getWidth(spText) / 2;
        stack.translate(width + getScaledWidth(- 270), getScaledHeight(15), 0);
        stack.scale(getScaledWidth(3.5f), getScaledHeight(3.5f), 1);
        stack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(6.5f));
        draw.drawText(mc.textRenderer, spText, -scaledTextWidth, 0, 0xFFFFFF, false);
        stack.pop();

        String skillText = data.findPersona().get().getSkillSet().getSelected().getName().getString().substring(1);
        String skillHighlight = data.findPersona().get().getSkillSet().getSelected().getName().getString().substring(0, 1).toUpperCase();

        stack.push();

        int skillTextWidth = (mc.textRenderer.getWidth(skillText) / 2);
        int skillHighlightWidth = (mc.textRenderer.getWidth(skillHighlight) / 2);

        stack.translate(width + getScaledWidth(- 115), height + getScaledHeight(- 36), 0);
        stack.scale(getScaledWidth(3.5f), getScaledHeight(3.5f), 1);
        stack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(12.5f));

        draw.drawText(mc.textRenderer, skillText, -skillTextWidth, getScaledHeight(-8), 0xFFFFFF, false);

        stack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(5f));
        mc.textRenderer.draw(skillHighlight, -skillTextWidth - skillHighlightWidth + -5, getScaledHeight(-9), 0, false, stack.peek().getPositionMatrix(), draw.getVertexConsumers(), TextRenderer.TextLayerType.POLYGON_OFFSET, Colors.WHITE, 0xF000F0, false);
        mc.textRenderer.draw(skillHighlight, -skillTextWidth - skillHighlightWidth + -4, getScaledHeight(-9), Colors.BLACK, false, stack.peek().getPositionMatrix(), draw.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0, false);

        stack.pop();

        stack.pop();

        //draw.drawText(mc.textRenderer, skillText , (x - (skillWidth)) - 94, y - 20, 0xFFFFFF, true);
    }

    private void drawEntity(DrawContext context, int x, int y, int size, float mouseX, float mouseY, LivingEntity entity, float delta) {
        float f = (float) Math.atan(mouseX / 40.0f);
        float g = (float) Math.atan(mouseY / 40.0f);
        Quaternionf quaternionf = new Quaternionf().rotateZ((float) Math.PI);
        Quaternionf quaternionf2 = new Quaternionf().rotateX(g * 20.0f * ((float) Math.PI / 180));
        quaternionf.mul(quaternionf2);
        float h = entity.bodyYaw;
        float i = entity.getYaw();
        float j = entity.getPitch();
        float k = entity.prevHeadYaw;
        float l = entity.headYaw;
        entity.bodyYaw = 180.0f + f * 20.0f;
        entity.setYaw(180.0f + f * 40.0f);
        entity.setPitch(-g * 20.0f);
        entity.headYaw = entity.getYaw();
        entity.prevHeadYaw = entity.getYaw();
        this.drawEntity(context, x, y, size, quaternionf, quaternionf2, entity, delta);
        entity.bodyYaw = h;
        entity.setYaw(i);
        entity.setPitch(j);
        entity.prevHeadYaw = k;
        entity.headYaw = l;
    }

    private void drawEntity(DrawContext context, int x, int y, int size, Quaternionf quaternionf, @Nullable Quaternionf quaternionf2, LivingEntity entity, float delta) {
        context.getMatrices().push();
        context.getMatrices().translate(x, y, -100);
        context.getMatrices().multiplyPositionMatrix(new Matrix4f().scaling(size, size, -size));
        context.getMatrices().multiply(quaternionf);

        DiffuseLighting.method_34742();

        EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        if (quaternionf2 != null) {
            quaternionf2.conjugate();
            entityRenderDispatcher.setRotation(quaternionf2);
        }

        entityRenderDispatcher.setRenderShadows(false);
        RenderSystem.runAsFancy(() ->
                entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0f, 1.0f, context.getMatrices(), context.getVertexConsumers(), 0xF000F0));
        context.draw();
        entityRenderDispatcher.setRenderShadows(true);
        //this.buffer.beginWrite(false);
        //this.buffer.draw(this.buffer.textureWidth, this.buffer.textureHeight, true);
        context.getMatrices().pop();
        DiffuseLighting.enableGuiDepthLighting();
    }

    /**
     * Returns a scaled value.
     * @param value the current value you are using
     * @param own your own screen dimension
     * @param dimension the current screen dimension
     * @return the scaled value
     */
    private static int getScaled(float value, int own, int dimension) {
        return (int) ((value / own) * dimension);
    }

    private static int getScaledHeight(float value) {
        return getScaled(value, 1080, MinecraftClient.getInstance().getWindow().getHeight());
    }
    private static int getScaledWidth(float value) {
        return getScaled(value, 1920, MinecraftClient.getInstance().getWindow().getWidth());
    }
}
