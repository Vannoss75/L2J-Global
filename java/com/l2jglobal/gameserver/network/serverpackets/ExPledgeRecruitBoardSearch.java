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

import java.util.List;

import com.l2jglobal.commons.network.PacketWriter;
import com.l2jglobal.gameserver.model.L2Clan;
import com.l2jglobal.gameserver.model.clan.entry.PledgeRecruitInfo;
import com.l2jglobal.gameserver.network.client.OutgoingPackets;

/**
 * @author Sdw
 */
public class ExPledgeRecruitBoardSearch implements IClientOutgoingPacket
{
	final List<PledgeRecruitInfo> _clanList;
	final private int _currentPage;
	final private int _totalNumberOfPage;
	final private int _clanOnCurrentPage;
	final private int _startIndex;
	final private int _endIndex;
	
	final static int CLAN_PER_PAGE = 12;
	
	public ExPledgeRecruitBoardSearch(List<PledgeRecruitInfo> clanList, int currentPage)
	{
		_clanList = clanList;
		_currentPage = currentPage;
		_totalNumberOfPage = (int) Math.ceil((double) _clanList.size() / CLAN_PER_PAGE);
		_startIndex = (_currentPage - 1) * CLAN_PER_PAGE;
		_endIndex = (_startIndex + CLAN_PER_PAGE) > _clanList.size() ? _clanList.size() : _startIndex + CLAN_PER_PAGE;
		_clanOnCurrentPage = _endIndex - _startIndex;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_PLEDGE_RECRUIT_BOARD_SEARCH.writeId(packet);
		
		packet.writeD(_currentPage);
		packet.writeD(_totalNumberOfPage);
		packet.writeD(_clanOnCurrentPage);
		
		for (int i = _startIndex; i < _endIndex; i++)
		{
			packet.writeD(_clanList.get(i).getClanId());
			packet.writeD(_clanList.get(i).getClan().getAllyId());
		}
		for (int i = _startIndex; i < _endIndex; i++)
		{
			final L2Clan clan = _clanList.get(i).getClan();
			packet.writeD(clan.getAllyCrestId());
			packet.writeD(clan.getCrestId());
			packet.writeS(clan.getName());
			packet.writeS(clan.getLeaderName());
			packet.writeD(clan.getLevel());
			packet.writeD(clan.getMembersCount());
			packet.writeD(_clanList.get(i).getKarma());
			packet.writeS(_clanList.get(i).getInformation());
		}
		return true;
	}
}