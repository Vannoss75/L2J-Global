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
import com.l2jglobal.gameserver.model.effects.AbstractEffect;
import com.l2jglobal.gameserver.model.skills.BuffInfo;
import com.l2jglobal.gameserver.model.skills.Skill;

/**
 * Talisman Slot effect implementation.
 * @author Adry_85
 */
public final class TalismanSlot extends AbstractEffect
{
	private final int _slots;
	
	public TalismanSlot(StatsSet params)
	{
		_slots = params.getInt("slots", 0);
	}
	
	@Override
	public boolean canStart(BuffInfo info)
	{
		return (info.getEffector() != null) && (info.getEffected() != null) && info.getEffected().isPlayer();
	}
	
	@Override
	public void onStart(L2Character effector, L2Character effected, Skill skill)
	{
		effected.getActingPlayer().getStat().addTalismanSlots(_slots);
	}
	
	@Override
	public void onExit(BuffInfo info)
	{
		info.getEffected().getActingPlayer().getStat().addTalismanSlots(-_slots);
	}
}
