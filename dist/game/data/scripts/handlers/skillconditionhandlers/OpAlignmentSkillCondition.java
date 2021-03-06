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
package handlers.skillconditionhandlers;

import com.l2jglobal.gameserver.enums.SkillConditionAffectType;
import com.l2jglobal.gameserver.enums.SkillConditionAlignment;
import com.l2jglobal.gameserver.model.L2Object;
import com.l2jglobal.gameserver.model.StatsSet;
import com.l2jglobal.gameserver.model.actor.L2Character;
import com.l2jglobal.gameserver.model.skills.ISkillCondition;
import com.l2jglobal.gameserver.model.skills.Skill;

/**
 * @author Sdw
 */
public class OpAlignmentSkillCondition implements ISkillCondition
{
	private final SkillConditionAffectType _affectType;
	private final SkillConditionAlignment _alignment;
	
	public OpAlignmentSkillCondition(StatsSet params)
	{
		_affectType = params.getEnum("affectType", SkillConditionAffectType.class);
		_alignment = params.getEnum("alignment", SkillConditionAlignment.class);
	}
	
	@Override
	public boolean canUse(L2Character caster, Skill skill, L2Object target)
	{
		switch (_affectType)
		{
			case CASTER:
			{
				return _alignment.test(caster.getActingPlayer());
			}
			case TARGET:
			{
				if ((target != null) && target.isPlayer())
				{
					return _alignment.test(target.getActingPlayer());
				}
				break;
			}
		}
		return false;
	}
}
