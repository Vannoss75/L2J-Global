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
package handlers.targethandlers.affectscope;

import java.util.function.Consumer;

import com.l2jglobal.gameserver.handler.IAffectScopeHandler;
import com.l2jglobal.gameserver.model.L2Object;
import com.l2jglobal.gameserver.model.actor.L2Character;
import com.l2jglobal.gameserver.model.skills.Skill;
import com.l2jglobal.gameserver.model.skills.targets.AffectScope;

/**
 * TODO: Wyvern affect scope.
 * @author Nik
 */
public class WyvernScope implements IAffectScopeHandler
{
	@Override
	public void forEachAffected(L2Character activeChar, L2Object target, Skill skill, Consumer<? super L2Object> action)
	{
		// TODO Unknown affect scope.
	}
	
	@Override
	public Enum<AffectScope> getAffectScopeType()
	{
		return AffectScope.WYVERN_SCOPE;
	}
}
