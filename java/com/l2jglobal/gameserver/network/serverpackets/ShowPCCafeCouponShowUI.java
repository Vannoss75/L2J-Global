/*
 * This file is part of the L2J Global project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jglobal.gameserver.network.serverpackets;

import com.l2jglobal.commons.network.PacketWriter;
import com.l2jglobal.gameserver.network.client.OutgoingPackets;

/**
 * @author -Wooden-
 */
public class ShowPCCafeCouponShowUI implements IClientOutgoingPacket
{
	public static final ShowPCCafeCouponShowUI STATIC_PACKET = new ShowPCCafeCouponShowUI();
	
	private ShowPCCafeCouponShowUI()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.SHOW_PCCAFE_COUPON_SHOW_UI.writeId(packet);
		
		return true;
	}
}
