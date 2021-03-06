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
package quests.Q10282_ToTheSeedOfAnnihilation;

import com.l2jglobal.gameserver.enums.QuestType;
import com.l2jglobal.gameserver.model.actor.L2Npc;
import com.l2jglobal.gameserver.model.actor.instance.L2PcInstance;
import com.l2jglobal.gameserver.model.quest.Quest;
import com.l2jglobal.gameserver.model.quest.QuestState;
import com.l2jglobal.gameserver.model.quest.State;

/**
 * To the Seed of Destruction (10269)
 * @author nonom
 */
public final class Q10282_ToTheSeedOfAnnihilation extends Quest
{
	// NPCs
	private static final int KBALDIR = 32733;
	private static final int KLEMIS = 32734;
	// Item
	private static final int SOA_ORDERS = 15512;
	private static final int EAR = 17527; // Scroll: Enchant Armor (R-grade)
	// Misc
	private static final int MIN_LV = 85;
	
	public Q10282_ToTheSeedOfAnnihilation()
	{
		super(10282);
		addStartNpc(KBALDIR);
		addTalkId(KBALDIR, KLEMIS);
		registerQuestItems(SOA_ORDERS);
		addCondMinLevel(MIN_LV, "32733-00.htm");
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		final String htmltext = event;
		final QuestState st = getQuestState(player, false);
		if (st == null)
		{
			return htmltext;
		}
		
		switch (event)
		{
			case "32733-07.htm":
			{
				st.startQuest();
				giveItems(player, SOA_ORDERS, 1);
				break;
			}
			case "32734-02.htm":
			{
				giveAdena(player, 212182, true);
				giveItems(player, EAR, 5);
				addExpAndSp(player, 1148480, 275);
				st.exitQuest(QuestType.ONE_TIME);
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg(player);
		final QuestState st = getQuestState(player, true);
		if (st == null)
		{
			return htmltext;
		}
		
		final int npcId = npc.getId();
		switch (st.getState())
		{
			case State.COMPLETED:
			{
				if (npcId == KBALDIR)
				{
					htmltext = "32733-09.htm";
				}
				else if (npcId == KLEMIS)
				{
					htmltext = "32734-03.htm";
				}
				break;
			}
			case State.CREATED:
			{
				if (npcId == KBALDIR)
				{
					htmltext = "32733-01.htm";
				}
				break;
			}
			case State.STARTED:
			{
				if (st.isCond(1))
				{
					if (npcId == KBALDIR)
					{
						htmltext = "32733-08.htm";
					}
					else if (npcId == KLEMIS)
					{
						htmltext = "32734-01.htm";
					}
				}
			}
				break;
		}
		return htmltext;
	}
}
