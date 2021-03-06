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
package com.l2jglobal.gameserver.network.clientpackets;

import com.l2jglobal.commons.network.PacketReader;
import com.l2jglobal.gameserver.enums.MatchingRoomType;
import com.l2jglobal.gameserver.model.L2Party;
import com.l2jglobal.gameserver.model.L2World;
import com.l2jglobal.gameserver.model.actor.instance.L2PcInstance;
import com.l2jglobal.gameserver.model.matching.MatchingRoom;
import com.l2jglobal.gameserver.network.SystemMessageId;
import com.l2jglobal.gameserver.network.client.L2GameClient;

/**
 * format (ch) d
 * @author -Wooden-
 */
public final class RequestOustFromPartyRoom implements IClientIncomingPacket
{
	private int _charObjId;
	
	@Override
	public boolean read(L2GameClient client, PacketReader packet)
	{
		_charObjId = packet.readD();
		return true;
	}
	
	@Override
	public void run(L2GameClient client)
	{
		final L2PcInstance player = client.getActiveChar();
		if (player == null)
		{
			return;
		}
		
		final L2PcInstance member = L2World.getInstance().getPlayer(_charObjId);
		if (member == null)
		{
			return;
		}
		
		final MatchingRoom room = player.getMatchingRoom();
		if ((room == null) || (room.getRoomType() != MatchingRoomType.PARTY) || (room.getLeader() != player) || (player == member))
		{
			return;
		}
		
		final L2Party playerParty = player.getParty();
		final L2Party memberParty = player.getParty();
		
		if ((playerParty != null) && (memberParty != null) && (playerParty.getLeaderObjectId() == memberParty.getLeaderObjectId()))
		{
			client.sendPacket(SystemMessageId.YOU_CANNOT_DISMISS_A_PARTY_MEMBER_BY_FORCE);
		}
		else
		{
			room.deleteMember(member, true);
		}
	}
}
