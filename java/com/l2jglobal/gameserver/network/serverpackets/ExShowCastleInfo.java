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

import java.util.Collection;

import com.l2jglobal.commons.network.PacketWriter;
import com.l2jglobal.gameserver.data.sql.impl.ClanTable;
import com.l2jglobal.gameserver.enums.TaxType;
import com.l2jglobal.gameserver.instancemanager.CastleManager;
import com.l2jglobal.gameserver.model.entity.Castle;
import com.l2jglobal.gameserver.network.client.OutgoingPackets;

/**
 * @author KenM
 */
public class ExShowCastleInfo implements IClientOutgoingPacket
{
	public static final ExShowCastleInfo STATIC_PACKET = new ExShowCastleInfo();
	
	private ExShowCastleInfo()
	{
		
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_SHOW_CASTLE_INFO.writeId(packet);
		
		final Collection<Castle> castles = CastleManager.getInstance().getCastles();
		packet.writeD(castles.size());
		for (Castle castle : castles)
		{
			packet.writeD(castle.getResidenceId());
			if (castle.getOwnerId() > 0)
			{
				if (ClanTable.getInstance().getClan(castle.getOwnerId()) != null)
				{
					packet.writeS(ClanTable.getInstance().getClan(castle.getOwnerId()).getName());
				}
				else
				{
					_log.warning("Castle owner with no name! Castle: " + castle.getName() + " has an OwnerId = " + castle.getOwnerId() + " who does not have a  name!");
					packet.writeS("");
				}
			}
			else
			{
				packet.writeS("");
			}
			packet.writeD(castle.getTaxPercent(TaxType.BUY));
			packet.writeD((int) (castle.getSiege().getSiegeDate().getTimeInMillis() / 1000));
		}
		return true;
	}
}
