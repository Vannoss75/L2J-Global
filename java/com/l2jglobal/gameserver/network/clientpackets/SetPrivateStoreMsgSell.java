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

import com.l2jglobal.Config;
import com.l2jglobal.commons.network.PacketReader;
import com.l2jglobal.gameserver.model.actor.instance.L2PcInstance;
import com.l2jglobal.gameserver.network.client.L2GameClient;
import com.l2jglobal.gameserver.network.serverpackets.PrivateStoreMsgSell;
import com.l2jglobal.gameserver.util.Util;

/**
 * This class ...
 * @version $Revision: 1.2.4.2 $ $Date: 2005/03/27 15:29:30 $
 */
public class SetPrivateStoreMsgSell implements IClientIncomingPacket
{
	private static final int MAX_MSG_LENGTH = 29;
	
	private String _storeMsg;
	
	@Override
	public boolean read(L2GameClient client, PacketReader packet)
	{
		_storeMsg = packet.readS();
		return true;
	}
	
	@Override
	public void run(L2GameClient client)
	{
		final L2PcInstance player = client.getActiveChar();
		if ((player == null) || (player.getSellList() == null))
		{
			return;
		}
		
		if ((_storeMsg != null) && (_storeMsg.length() > MAX_MSG_LENGTH))
		{
			Util.handleIllegalPlayerAction(player, "Player " + player.getName() + " tried to overflow private store sell message", Config.DEFAULT_PUNISH);
			return;
		}
		
		player.getSellList().setTitle(_storeMsg);
		client.sendPacket(new PrivateStoreMsgSell(player));
	}
}
