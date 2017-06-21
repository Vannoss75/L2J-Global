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
import com.l2jglobal.gameserver.model.TradeItem;
import com.l2jglobal.gameserver.model.actor.instance.L2PcInstance;
import com.l2jglobal.gameserver.network.client.OutgoingPackets;

public class PrivateStoreManageListSell extends AbstractItemPacket
{
	private final int _objId;
	private final long _playerAdena;
	private final boolean _packageSale;
	private final Collection<TradeItem> _itemList;
	private final TradeItem[] _sellList;
	
	public PrivateStoreManageListSell(L2PcInstance player, boolean isPackageSale)
	{
		_objId = player.getObjectId();
		_playerAdena = player.getAdena();
		player.getSellList().updateItems();
		_packageSale = isPackageSale;
		_itemList = player.getInventory().getAvailableItems(player.getSellList());
		_sellList = player.getSellList().getItems();
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.PRIVATE_STORE_MANAGE_LIST.writeId(packet);
		
		packet.writeD(_objId);
		packet.writeD(_packageSale ? 1 : 0); // Package sell
		packet.writeQ(_playerAdena);
		
		packet.writeD(_itemList.size()); // for potential sells
		for (TradeItem item : _itemList)
		{
			writeItem(packet, item);
			packet.writeQ(item.getItem().getReferencePrice() * 2);
		}
		
		packet.writeD(_sellList.length); // count for any items already added for sell
		for (TradeItem item : _sellList)
		{
			writeItem(packet, item);
			packet.writeQ(item.getPrice());
			packet.writeQ(item.getItem().getReferencePrice() * 2);
		}
		return true;
	}
}
