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

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

import com.l2jglobal.gameserver.model.CharEffectList;
import com.l2jglobal.gameserver.model.StatsSet;
import com.l2jglobal.gameserver.model.actor.L2Character;
import com.l2jglobal.gameserver.model.effects.AbstractEffect;
import com.l2jglobal.gameserver.model.effects.L2EffectType;
import com.l2jglobal.gameserver.model.items.instance.L2ItemInstance;
import com.l2jglobal.gameserver.model.skills.AbnormalType;
import com.l2jglobal.gameserver.model.skills.BuffInfo;
import com.l2jglobal.gameserver.model.skills.Skill;

/**
 * Dispel By Slot effect implementation.
 * @author Gnacik, Zoey76, Adry_85
 */
public final class DispelBySlot extends AbstractEffect
{
	private final String _dispel;
	private final Map<AbnormalType, Short> _dispelAbnormals;
	
	public DispelBySlot(StatsSet params)
	{
		_dispel = params.getString("dispel", null);
		if ((_dispel != null) && !_dispel.isEmpty())
		{
			_dispelAbnormals = new EnumMap<>(AbnormalType.class);
			for (String ngtStack : _dispel.split(";"))
			{
				final String[] ngt = ngtStack.split(",");
				_dispelAbnormals.put(AbnormalType.getAbnormalType(ngt[0]), Short.parseShort(ngt[1]));
			}
		}
		else
		{
			_dispelAbnormals = Collections.<AbnormalType, Short> emptyMap();
		}
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.DISPEL_BY_SLOT;
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(L2Character effector, L2Character effected, Skill skill, L2ItemInstance item)
	{
		if (_dispelAbnormals.isEmpty())
		{
			return;
		}
		
		final CharEffectList effectList = effected.getEffectList();
		// There is no need to iterate over all buffs,
		// Just iterate once over all slots to dispel and get the buff with that abnormal if exists,
		// Operation of O(n) for the amount of slots to dispel (which is usually small) and O(1) to get the buff.
		for (Entry<AbnormalType, Short> entry : _dispelAbnormals.entrySet())
		{
			// Dispel transformations (buff and by GM)
			if ((entry.getKey() == AbnormalType.TRANSFORM))
			{
				if ((entry.getValue() == effected.getTransformationId()) || (entry.getValue() < 0))
				{
					effected.stopTransformation(true);
					continue;
				}
			}
			
			final BuffInfo toDispel = effectList.getBuffInfoByAbnormalType(entry.getKey());
			if (toDispel == null)
			{
				continue;
			}
			
			if ((entry.getKey() == toDispel.getSkill().getAbnormalType()) && ((entry.getValue() < 0) || (entry.getValue() >= toDispel.getSkill().getAbnormalLvl())))
			{
				effectList.stopSkillEffects(true, entry.getKey());
			}
		}
	}
}
