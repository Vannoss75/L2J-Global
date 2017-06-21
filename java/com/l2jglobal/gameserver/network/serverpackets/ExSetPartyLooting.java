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
import com.l2jglobal.gameserver.enums.PartyDistributionType;
import com.l2jglobal.gameserver.network.client.OutgoingPackets;

/**
 * @author JIV
 */
public class ExSetPartyLooting implements IClientOutgoingPacket
{
	private final int _result;
	private final PartyDistributionType _partyDistributionType;
	
	public ExSetPartyLooting(int result, PartyDistributionType partyDistributionType)
	{
		_result = result;
		_partyDistributionType = partyDistributionType;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_SET_PARTY_LOOTING.writeId(packet);
		
		packet.writeD(_result);
		packet.writeD(_partyDistributionType.getId());
		return true;
	}
}
