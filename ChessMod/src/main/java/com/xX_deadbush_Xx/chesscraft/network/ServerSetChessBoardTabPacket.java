package com.xX_deadbush_Xx.chesscraft.network;

import java.util.function.Supplier;

import com.xX_deadbush_Xx.chesscraft.client.ChessBoardScreen;
import com.xX_deadbush_Xx.chesscraft.game_logic.ChessBoardContainer;
import com.xX_deadbush_Xx.chesscraft.game_logic.ChessBoardContainer.Mode;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class ServerSetChessBoardTabPacket {
	private int tab;
	
	public ServerSetChessBoardTabPacket(int tab) {
		this.tab = tab;
	}

	public static void encode(ServerSetChessBoardTabPacket msg, PacketBuffer buf) {
		buf.writeInt(msg.tab);
	}

	public static ServerSetChessBoardTabPacket decode(PacketBuffer buf) {
		return new ServerSetChessBoardTabPacket(buf.readInt());
	}

	public static void handle(ServerSetChessBoardTabPacket msg, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		context.enqueueWork(new Runnable() {

			@SuppressWarnings("resource")
			@Override
			public void run() {
				PlayerEntity player = Minecraft.getInstance().player;
				if(player.openContainer instanceof ChessBoardContainer && player.openContainer != null) {
					((ChessBoardContainer)player.openContainer).setMode(Mode.values()[msg.tab]);
					
					if(Minecraft.getInstance().currentScreen instanceof ChessBoardScreen) {
						((ChessBoardScreen)Minecraft.getInstance().currentScreen).updateMode(Mode.values()[msg.tab]);
					}
				}
			}
		});
		
		context.setPacketHandled(true);
	}
}
