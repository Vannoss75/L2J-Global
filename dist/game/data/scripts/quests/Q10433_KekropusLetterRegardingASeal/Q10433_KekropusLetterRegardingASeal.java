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
package quests.Q10433_KekropusLetterRegardingASeal;

import com.l2jglobal.gameserver.enums.CategoryType;
import com.l2jglobal.gameserver.model.Location;
import com.l2jglobal.gameserver.model.actor.L2Character;
import com.l2jglobal.gameserver.model.actor.L2Npc;
import com.l2jglobal.gameserver.model.actor.instance.L2PcInstance;
import com.l2jglobal.gameserver.model.quest.QuestState;
import com.l2jglobal.gameserver.network.NpcStringId;
import com.l2jglobal.gameserver.network.serverpackets.ExShowScreenMessage;

import quests.LetterQuest;

/**
 * Kekropus' Letter: Regarding a Seal (10433)
 * @author Stayway
 */
public final class Q10433_KekropusLetterRegardingASeal extends LetterQuest
{
	// NPCs
	private static final int MOUEN = 30196;
	private static final int RUA = 33841;
	private static final int INVISIBLE_NPC = 19543;
	// Items
	private static final int SOE_TOWN_OF_OREN = 37123; // Scroll of Escape: Town of Schuttgart
	private static final int EWS = 959; // Scroll: Enchant Weapon (S-grade)
	private static final int EAS = 960; // Scroll: Enchant Armor (S-grade)
	// Location
	private static final Location TELEPORT_LOC = new Location(80970, 56333, -1560);
	// Misc
	private static final int MIN_LEVEL = 81;
	private static final int MAX_LEVEL = 84;
	
	public Q10433_KekropusLetterRegardingASeal()
	{
		super(10433);
		addTalkId(MOUEN, RUA);
		addSeeCreatureId(INVISIBLE_NPC);
		setIsErtheiaQuest(false);
		setLevel(MIN_LEVEL, MAX_LEVEL);
		setStartQuestSound("Npcdialog1.kekrops_quest_9");
		setStartLocation(SOE_TOWN_OF_OREN, TELEPORT_LOC);
		registerQuestItems(SOE_TOWN_OF_OREN);
		addCondInCategory(CategoryType.WEAPON_MASTER, "nocond.html");
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		final QuestState qs = getQuestState(player, false);
		if (qs == null)
		{
			return null;
		}
		
		String htmltext = null;
		switch (event)
		{
			case "30196-02.html":
			{
				htmltext = event;
				break;
			}
			case "30196-03.html":
			{
				if (qs.isCond(1))
				{
					takeItems(player, SOE_TOWN_OF_OREN, -1);
					qs.setCond(2, true);
					htmltext = event;
				}
				break;
			}
			case "33841-02.html":
			{
				if (qs.isCond(2))
				{
					qs.exitQuest(false, true);
					giveItems(player, EWS, 1);
					giveItems(player, EAS, 10);
					giveStoryQuestReward(player, 235);
					if ((player.getLevel() >= MIN_LEVEL) && (player.getLevel() <= MAX_LEVEL))
					{
						addExpAndSp(player, 1_412_040, 338);
					}
					htmltext = event;
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg(player);
		final QuestState qs = getQuestState(player, false);
		
		if (qs == null)
		{
			return htmltext;
		}
		
		if (qs.isStarted())
		{
			if ((npc.getId() == MOUEN) && qs.isCond(1))
			{
				htmltext = "30196-01.html";
			}
			else if (qs.isCond(2))
			{
				htmltext = npc.getId() == MOUEN ? "30196-04.html" : "33841-01.html";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onSeeCreature(L2Npc npc, L2Character creature, boolean isSummon)
	{
		if (creature.isPlayer())
		{
			final L2PcInstance player = creature.getActingPlayer();
			final QuestState qs = getQuestState(player, false);
			
			if ((qs != null) && qs.isCond(2))
			{
				showOnScreenMsg(player, NpcStringId.SEL_MAHUM_TRAINING_GROUNDS_IS_A_GOOD_HUNTING_ZONE_FOR_LV_81_OR_ABOVE, ExShowScreenMessage.TOP_CENTER, 6000);
			}
		}
		return super.onSeeCreature(npc, creature, isSummon);
	}
	
	@Override
	public boolean canShowTutorialMark(L2PcInstance player)
	{
		return player.isInCategory(CategoryType.WEAPON_MASTER);
	}
}