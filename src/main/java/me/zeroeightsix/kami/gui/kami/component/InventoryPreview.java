package me.zeroeightsix.kami.gui.kami.component;

import me.zeroeightsix.kami.gui.rgui.component.listen.RenderListener;
import me.zeroeightsix.kami.gui.rgui.component.use.Label;
import me.zeroeightsix.kami.module.modules.bewwawho.render.InvPreview;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class InventoryPreview extends Label {

    private Minecraft mc = Minecraft.getMinecraft();

    public InventoryPreview() {
        super("");

        addRenderListener(new RenderListener() {
            @Override
            public void onPreRender() {
//                Frame parentFrame = ContainerHelper.getFirstParent(Frame.class, ActiveModules.this);
//                if (parentFrame == null) return;
//                Docking docking = parentFrame.getDocking();
                preBoxRender();
                preItemRender();
            }

            @Override
            public void onPostRender() {
                postBoxRender();
                postItemRender();
            }

            public void onRender() {
                final NonNullList<ItemStack> items = (NonNullList<ItemStack>) mc.player.inventory.mainInventory;
                boxRender();
                itemRender(items, 0, 0);
            }

            public void preBoxRender() {
                GL11.glPushMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.disableAlpha();
                GlStateManager.clear(256);
                GlStateManager.enableBlend();
            }

            public void postBoxRender() {
                GlStateManager.disableBlend();
                GlStateManager.disableDepth();
                GlStateManager.disableLighting();
                GlStateManager.enableDepth();
                GlStateManager.enableAlpha();
                GlStateManager.popMatrix();
                GL11.glPopMatrix();
            }

            public void preItemRender() {
                GL11.glPushMatrix();
                GL11.glDepthMask(true);
                GlStateManager.clear(256);
                GlStateManager.disableDepth();
                GlStateManager.enableDepth();
                RenderHelper.enableStandardItemLighting();
                GlStateManager.scale(1.0f, 1.0f, 0.01f);
            }

            public void postItemRender() {
                GlStateManager.scale(1.0f, 1.0f, 1.0f);
                RenderHelper.disableStandardItemLighting();
                GlStateManager.enableAlpha();
                GlStateManager.disableBlend();
                GlStateManager.disableLighting();
                GlStateManager.scale(0.5, 0.5, 0.5);
                GlStateManager.disableDepth();
                GlStateManager.enableDepth();
                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                GL11.glPopMatrix();
            }

            private void boxRender() {
                preBoxRender();
                InvPreview invPreview = new InvPreview();
                ResourceLocation box = invPreview.getBox();
                mc.renderEngine.bindTexture(box);
                mc.ingameGUI.drawTexturedModalRect(0, 0, 7, 17, 162, 54); // 56 136 1296 432
                postBoxRender();
            }

            private void itemRender(final NonNullList<ItemStack> items, final int x, final int y) {
                for (int size = items.size(), item = 9; item < size; ++item) {
                    final int slotx = x + 1 + item % 9 * 18;
                    final int sloty = y + 1 + (item / 9 - 1) * 18;
                    preItemRender();
                    mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack) items.get(item), slotx, sloty);
                    mc.getRenderItem().renderItemOverlays(mc.fontRenderer, (ItemStack) items.get(item), slotx, sloty);
                    postItemRender();
                }
            }
        });
    }
};