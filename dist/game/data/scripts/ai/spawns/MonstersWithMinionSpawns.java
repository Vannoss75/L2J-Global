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
package ai.spawns;

import com.l2jglobal.gameserver.data.xml.impl.NpcData;
import com.l2jglobal.gameserver.model.actor.L2Npc;
import com.l2jglobal.gameserver.model.actor.instance.L2MonsterInstance;

import ai.AbstractNpcAI;

/**
 * Spawn AI for monsters that spawn minions.
 * @author Global
 */
public final class MonstersWithMinionSpawns extends AbstractNpcAI
{
	private MonstersWithMinionSpawns()
	{
		addSpawnId(NpcData.getMasterMonsterIDs());
	}
	
	@Override
	public String onSpawn(L2Npc npc)
	{
		if (npc.isMonster())
		{
			((L2MonsterInstance) npc).getMinionList().spawnMinions(npc.getParameters().getMinionList("Privates"));
		}
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new MonstersWithMinionSpawns();
	}
}