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
package handlers.effecthandlers;

import com.l2jglobal.gameserver.model.StatsSet;
import com.l2jglobal.gameserver.model.actor.L2Character;
import com.l2jglobal.gameserver.model.actor.instance.L2PcInstance;
import com.l2jglobal.gameserver.model.effects.AbstractEffect;
import com.l2jglobal.gameserver.model.items.instance.L2ItemInstance;
import com.l2jglobal.gameserver.model.skills.Skill;

/**
 * Change Hair Color effect implementation.
 * @author Zoey76
 */
public final class ChangeHairColor extends AbstractEffect
{
	private final int _value;
	
	public ChangeHairColor(StatsSet params)
	{
		_value = params.getInt("value", 0);
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(L2Character effector, L2Character effected, Skill skill, L2ItemInstance item)
	{
		if (!effected.isPlayer())
		{
			return;
		}
		
		final L2PcInstance player = effected.getActingPlayer();
		player.getAppearance().setHairColor(_value);
		player.broadcastUserInfo();
	}
}
