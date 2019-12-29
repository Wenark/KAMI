package me.zeroeightsix.kami.module.modules.movement;

import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.math.MathHelper;

/**
 * Created by 086 on 11/04/2018.
 * Edited by Itistheend on 11/30/19.
 * IITE on top uwu 12/28/19.
 */
 
@Module.Info(name = "ElytraFlight", description = "Allows infinite elytra flying", category = Module.Category.MOVEMENT)
public class ElytraFlight extends Module {

    private Setting<ElytraFlightMode> mode = register(Settings.e("Mode", ElytraFlightMode.BOOST));
	/* private Setting<Float> fallspeed = register(Settings.f("fallspeed", .000050000002f)); */
	private Float fallspeed = .000050000002f;
	private Setting<Float> speed = register(Settings.f("speed", 1.8f));

    @Override
    public void onUpdate() {
		
		if(mc.player.capabilities.isFlying){
				mc.player.setVelocity(0, 0, 0);
				mc.player.setPosition(mc.player.posX, mc.player.posY - fallspeed/*.getValue()*/, mc.player.posZ);
				mc.player.capabilities.setFlySpeed(speed.getValue());
				mc.player.setSprinting(false);
		}else{
			mc.player.capabilities.allowFlying = false;
		}
		
		
		
        if (!mc.player.isElytraFlying()) return;
        switch (mode.getValue()) {
            case BOOST:
                if(mc.player.isInWater())
                {
                    mc.getConnection()
                            .sendPacket(new CPacketEntityAction(mc.player,
                                    CPacketEntityAction.Action.START_FALL_FLYING));
                    return;
                }

                if(mc.gameSettings.keyBindJump.isKeyDown())
                    mc.player.motionY += 0.08;
                else if(mc.gameSettings.keyBindSneak.isKeyDown())
                    mc.player.motionY -= 0.04;

                if(mc.gameSettings.keyBindForward.isKeyDown()) {
                    float yaw = (float)Math
                            .toRadians(mc.player.rotationYaw);
                    mc.player.motionX -= MathHelper.sin(yaw) * 0.05F;
                    mc.player.motionZ += MathHelper.cos(yaw) * 0.05F;
                }else if(mc.gameSettings.keyBindBack.isKeyDown()) {
                    float yaw = (float)Math
                            .toRadians(mc.player.rotationYaw);
                    mc.player.motionX += MathHelper.sin(yaw) * 0.05F;
                    mc.player.motionZ -= MathHelper.cos(yaw) * 0.05F;
                }
                break;
            case FLY:
                mc.player.capabilities.setFlySpeed(.915f);
                mc.player.capabilities.isFlying = true;
				
                if (mc.player.capabilities.isCreativeMode) return;
                mc.player.capabilities.allowFlying = true;
                break;
        }
    }

    @Override
    protected void onDisable() {
		mc.player.capabilities.isFlying = false;
		mc.player.capabilities.setFlySpeed(0.05f);
		if (mc.player.capabilities.isCreativeMode) return;
		mc.player.capabilities.allowFlying = false;
    }

    private enum ElytraFlightMode {
        FLY, BOOST,
    }

}
