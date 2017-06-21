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
package com.l2jglobal.gameserver.model.instancezone.conditions;

import com.l2jglobal.gameserver.model.StatsSet;
import com.l2jglobal.gameserver.model.actor.L2Npc;
import com.l2jglobal.gameserver.model.actor.instance.L2PcInstance;
import com.l2jglobal.gameserver.model.instancezone.InstanceTemplate;
import com.l2jglobal.gameserver.network.SystemMessageId;

/**
 * Instance item condition
 * @author malyelfik
 */
public final class ConditionItem extends Condition
{
	private final int _itemId;
	private final long _count;
	private final boolean _take;
	
	public ConditionItem(InstanceTemplate template, StatsSet parameters, boolean onlyLeader, boolean showMessageAndHtml)
	{
		super(template, parameters, onlyLeader, showMessageAndHtml);
		// Load params
		_itemId = parameters.getInt("id");
		_count = parameters.getLong("count");
		_take = parameters.getBoolean("take", false);
		// Set message
		setSystemMessage(SystemMessageId.C1_S_ITEM_REQUIREMENT_IS_NOT_SUFFICIENT_AND_CANNOT_BE_ENTERED, (msg, player) -> msg.addCharName(player));
	}
	
	@Override
	protected boolean test(L2PcInstance player, L2Npc npc)
	{
		return player.getInventory().getInventoryItemCount(_itemId, -1) >= _count;
	}
	
	@Override
	protected void onSuccess(L2PcInstance player)
	{
		if (_take)
		{
			player.destroyItemByItemId("InstanceConditionDestroy", _itemId, _count, null, true);
		}
	}
}