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
package com.l2jglobal.gameserver.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.l2jglobal.Config;
import com.l2jglobal.commons.database.DatabaseFactory;
import com.l2jglobal.gameserver.enums.ClanRewardType;
import com.l2jglobal.gameserver.instancemanager.SiegeManager;
import com.l2jglobal.gameserver.model.actor.instance.L2PcInstance;
import com.l2jglobal.gameserver.model.variables.PlayerVariables;

/**
 * This class holds the clan members data.
 */
public class L2ClanMember
{
	private static final Logger LOGGER = Logger.getLogger(L2ClanMember.class.getName());
	
	private final L2Clan _clan;
	private int _objectId;
	private String _name;
	private String _title;
	private int _powerGrade;
	private int _level;
	private int _classId;
	private boolean _sex;
	private int _raceOrdinal;
	private L2PcInstance _player;
	private int _pledgeType;
	private int _apprentice;
	private int _sponsor;
	private long _onlineTime;
	
	/**
	 * Used to restore a clan member from the database.
	 * @param clan the clan where the clan member belongs.
	 * @param clanMember the clan member result set
	 * @throws SQLException if the columnLabel is not valid or a database error occurs
	 */
	public L2ClanMember(L2Clan clan, ResultSet clanMember) throws SQLException
	{
		if (clan == null)
		{
			throw new IllegalArgumentException("Cannot create a Clan Member with a null clan.");
		}
		_clan = clan;
		_name = clanMember.getString("char_name");
		_level = clanMember.getInt("level");
		_classId = clanMember.getInt("classid");
		_objectId = clanMember.getInt("charId");
		_pledgeType = clanMember.getInt("subpledge");
		_title = clanMember.getString("title");
		_powerGrade = clanMember.getInt("power_grade");
		_apprentice = clanMember.getInt("apprentice");
		_sponsor = clanMember.getInt("sponsor");
		_sex = clanMember.getInt("sex") != 0;
		_raceOrdinal = clanMember.getInt("race");
	}
	
	/**
	 * Creates a clan member from a player instance.
	 * @param clan the clan where the player belongs
	 * @param player the player from which the clan member will be created
	 */
	public L2ClanMember(L2Clan clan, L2PcInstance player)
	{
		if (clan == null)
		{
			throw new IllegalArgumentException("Cannot create a Clan Member if player has a null clan.");
		}
		_player = player;
		_clan = clan;
		_name = player.getName();
		_level = player.getLevel();
		_classId = player.getClassId().getId();
		_objectId = player.getObjectId();
		_pledgeType = player.getPledgeType();
		_powerGrade = player.getPowerGrade();
		_title = player.getTitle();
		_sponsor = 0;
		_apprentice = 0;
		_sex = player.getAppearance().getSex();
		_raceOrdinal = player.getRace().ordinal();
	}
	
	/**
	 * Sets the player instance.
	 * @param player the new player instance
	 */
	public void setPlayerInstance(L2PcInstance player)
	{
		if ((player == null) && (_player != null))
		{
			// this is here to keep the data when the player logs off
			_name = _player.getName();
			_level = _player.getLevel();
			_classId = _player.getClassId().getId();
			_objectId = _player.getObjectId();
			_powerGrade = _player.getPowerGrade();
			_pledgeType = _player.getPledgeType();
			_title = _player.getTitle();
			_apprentice = _player.getApprentice();
			_sponsor = _player.getSponsor();
			_sex = _player.getAppearance().getSex();
			_raceOrdinal = _player.getRace().ordinal();
		}
		
		if (player != null)
		{
			_clan.addSkillEffects(player);
			if ((_clan.getLevel() > 3) && player.isClanLeader())
			{
				SiegeManager.getInstance().addSiegeSkills(player);
			}
			if (player.isClanLeader())
			{
				_clan.setLeader(this);
			}
		}
		_player = player;
	}
	
	/**
	 * Gets the player instance.
	 * @return the player instance
	 */
	public L2PcInstance getPlayerInstance()
	{
		return _player;
	}
	
	/**
	 * Checks if is online.
	 * @return true, if is online
	 */
	public boolean isOnline()
	{
		if ((_player == null) || !_player.isOnline())
		{
			return false;
		}
		if ((_player.getClient() == null) || _player.getClient().isDetached())
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the class id.
	 * @return the classId
	 */
	public int getClassId()
	{
		return _player != null ? _player.getClassId().getId() : _classId;
	}
	
	/**
	 * Gets the level.
	 * @return the level
	 */
	public int getLevel()
	{
		return _player != null ? _player.getLevel() : _level;
	}
	
	/**
	 * Gets the name.
	 * @return the name
	 */
	public String getName()
	{
		return _player != null ? _player.getName() : _name;
	}
	
	/**
	 * Gets the object id.
	 * @return Returns the objectId.
	 */
	public int getObjectId()
	{
		return _player != null ? _player.getObjectId() : _objectId;
	}
	
	/**
	 * Gets the title.
	 * @return the title
	 */
	public String getTitle()
	{
		return _player != null ? _player.getTitle() : _title;
	}
	
	/**
	 * Gets the pledge type.
	 * @return the pledge type
	 */
	public int getPledgeType()
	{
		return _player != null ? _player.getPledgeType() : _pledgeType;
	}
	
	/**
	 * Sets the pledge type.
	 * @param pledgeType the new pledge type
	 */
	public void setPledgeType(int pledgeType)
	{
		_pledgeType = pledgeType;
		if (_player != null)
		{
			_player.setPledgeType(pledgeType);
		}
		else
		{
			// db save if char not logged in
			updatePledgeType();
		}
	}
	
	/**
	 * Update pledge type.
	 */
	public void updatePledgeType()
	{
		try (Connection con = DatabaseFactory.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE characters SET subpledge=? WHERE charId=?"))
		{
			ps.setLong(1, _pledgeType);
			ps.setInt(2, getObjectId());
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.log(Level.WARNING, "Could not update pledge type: " + e.getMessage(), e);
		}
	}
	
	/**
	 * Gets the power grade.
	 * @return the power grade
	 */
	public int getPowerGrade()
	{
		if (_player != null)
		{
			return _player.getPowerGrade();
		}
		return _powerGrade;
	}
	
	/**
	 * Sets the power grade.
	 * @param powerGrade the new power grade
	 */
	public void setPowerGrade(int powerGrade)
	{
		_powerGrade = powerGrade;
		if (_player != null)
		{
			_player.setPowerGrade(powerGrade);
		}
		else
		{
			// db save if char not logged in
			updatePowerGrade();
		}
	}
	
	/**
	 * Update the characters table of the database with power grade.
	 */
	public void updatePowerGrade()
	{
		try (Connection con = DatabaseFactory.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE characters SET power_grade=? WHERE charId=?"))
		{
			ps.setLong(1, _powerGrade);
			ps.setInt(2, getObjectId());
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.log(Level.WARNING, "Could not update power _grade: " + e.getMessage(), e);
		}
	}
	
	/**
	 * Sets the apprentice and sponsor.
	 * @param apprenticeID the apprentice id
	 * @param sponsorID the sponsor id
	 */
	public void setApprenticeAndSponsor(int apprenticeID, int sponsorID)
	{
		_apprentice = apprenticeID;
		_sponsor = sponsorID;
	}
	
	/**
	 * Gets the player's race ordinal.
	 * @return the race ordinal
	 */
	public int getRaceOrdinal()
	{
		if (_player != null)
		{
			return _player.getRace().ordinal();
		}
		return _raceOrdinal;
	}
	
	/**
	 * Gets the player's sex.
	 * @return the sex
	 */
	public boolean getSex()
	{
		if (_player != null)
		{
			return _player.getAppearance().getSex();
		}
		return _sex;
	}
	
	/**
	 * Gets the sponsor.
	 * @return the sponsor
	 */
	public int getSponsor()
	{
		if (_player != null)
		{
			return _player.getSponsor();
		}
		return _sponsor;
	}
	
	/**
	 * Gets the apprentice.
	 * @return the apprentice
	 */
	public int getApprentice()
	{
		if (_player != null)
		{
			return _player.getApprentice();
		}
		return _apprentice;
	}
	
	/**
	 * Gets the apprentice or sponsor name.
	 * @return the apprentice or sponsor name
	 */
	public String getApprenticeOrSponsorName()
	{
		if (_player != null)
		{
			_apprentice = _player.getApprentice();
			_sponsor = _player.getSponsor();
		}
		
		if (_apprentice != 0)
		{
			final L2ClanMember apprentice = _clan.getClanMember(_apprentice);
			if (apprentice != null)
			{
				return apprentice.getName();
			}
			return "Error";
		}
		if (_sponsor != 0)
		{
			final L2ClanMember sponsor = _clan.getClanMember(_sponsor);
			if (sponsor != null)
			{
				return sponsor.getName();
			}
			return "Error";
		}
		return "";
	}
	
	/**
	 * Gets the clan.
	 * @return the clan
	 */
	public L2Clan getClan()
	{
		return _clan;
	}
	
	/**
	 * Calculate pledge class.
	 * @param player the player
	 * @return the int
	 */
	public static int calculatePledgeClass(L2PcInstance player)
	{
		int pledgeClass = 0;
		if (player == null)
		{
			return pledgeClass;
		}
		
		final L2Clan clan = player.getClan();
		if (clan != null)
		{
			switch (clan.getLevel())
			{
				case 4:
					if (player.isClanLeader())
					{
						pledgeClass = 3;
					}
					break;
				case 5:
					if (player.isClanLeader())
					{
						pledgeClass = 4;
					}
					else
					{
						pledgeClass = 2;
					}
					break;
				case 6:
					switch (player.getPledgeType())
					{
						case -1:
							pledgeClass = 1;
							break;
						case 100:
						case 200:
							pledgeClass = 2;
							break;
						case 0:
							if (player.isClanLeader())
							{
								pledgeClass = 5;
							}
							else
							{
								switch (clan.getLeaderSubPledge(player.getObjectId()))
								{
									case 100:
									case 200:
										pledgeClass = 4;
										break;
									case -1:
									default:
										pledgeClass = 3;
										break;
								}
							}
							break;
					}
					break;
				case 7:
					switch (player.getPledgeType())
					{
						case -1:
							pledgeClass = 1;
							break;
						case 100:
						case 200:
							pledgeClass = 3;
							break;
						case 1001:
						case 1002:
						case 2001:
						case 2002:
							pledgeClass = 2;
							break;
						case 0:
							if (player.isClanLeader())
							{
								pledgeClass = 7;
							}
							else
							{
								switch (clan.getLeaderSubPledge(player.getObjectId()))
								{
									case 100:
									case 200:
										pledgeClass = 6;
										break;
									case 1001:
									case 1002:
									case 2001:
									case 2002:
										pledgeClass = 5;
										break;
									case -1:
									default:
										pledgeClass = 4;
										break;
								}
							}
							break;
					}
					break;
				case 8:
					switch (player.getPledgeType())
					{
						case -1:
							pledgeClass = 1;
							break;
						case 100:
						case 200:
							pledgeClass = 4;
							break;
						case 1001:
						case 1002:
						case 2001:
						case 2002:
							pledgeClass = 3;
							break;
						case 0:
							if (player.isClanLeader())
							{
								pledgeClass = 8;
							}
							else
							{
								switch (clan.getLeaderSubPledge(player.getObjectId()))
								{
									case 100:
									case 200:
										pledgeClass = 7;
										break;
									case 1001:
									case 1002:
									case 2001:
									case 2002:
										pledgeClass = 6;
										break;
									case -1:
									default:
										pledgeClass = 5;
										break;
								}
							}
							break;
					}
					break;
				case 9:
					switch (player.getPledgeType())
					{
						case -1:
							pledgeClass = 1;
							break;
						case 100:
						case 200:
							pledgeClass = 5;
							break;
						case 1001:
						case 1002:
						case 2001:
						case 2002:
							pledgeClass = 4;
							break;
						case 0:
							if (player.isClanLeader())
							{
								pledgeClass = 9;
							}
							else
							{
								switch (clan.getLeaderSubPledge(player.getObjectId()))
								{
									case 100:
									case 200:
										pledgeClass = 8;
										break;
									case 1001:
									case 1002:
									case 2001:
									case 2002:
										pledgeClass = 7;
										break;
									case -1:
									default:
										pledgeClass = 6;
										break;
								}
							}
							break;
					}
					break;
				case 10:
					switch (player.getPledgeType())
					{
						case -1:
							pledgeClass = 1;
							break;
						case 100:
						case 200:
							pledgeClass = 6;
							break;
						case 1001:
						case 1002:
						case 2001:
						case 2002:
							pledgeClass = 5;
							break;
						case 0:
							if (player.isClanLeader())
							{
								pledgeClass = 10;
							}
							else
							{
								switch (clan.getLeaderSubPledge(player.getObjectId()))
								{
									case 100:
									case 200:
										pledgeClass = 9;
										break;
									case 1001:
									case 1002:
									case 2001:
									case 2002:
										pledgeClass = 8;
										break;
									case -1:
									default:
										pledgeClass = 7;
										break;
								}
							}
							break;
					}
					break;
				case 11:
					switch (player.getPledgeType())
					{
						case -1:
							pledgeClass = 1;
							break;
						case 100:
						case 200:
							pledgeClass = 7;
							break;
						case 1001:
						case 1002:
						case 2001:
						case 2002:
							pledgeClass = 6;
							break;
						case 0:
							if (player.isClanLeader())
							{
								pledgeClass = 11;
							}
							else
							{
								switch (clan.getLeaderSubPledge(player.getObjectId()))
								{
									case 100:
									case 200:
										pledgeClass = 10;
										break;
									case 1001:
									case 1002:
									case 2001:
									case 2002:
										pledgeClass = 9;
										break;
									case -1:
									default:
										pledgeClass = 8;
										break;
								}
							}
							break;
					}
					break;
				default:
					pledgeClass = 1;
					break;
			}
		}
		
		if (player.isNoble() && (pledgeClass < 5))
		{
			pledgeClass = 5;
		}
		
		if (player.isHero() && (pledgeClass < 8))
		{
			pledgeClass = 8;
		}
		return pledgeClass;
	}
	
	/**
	 * Save apprentice and sponsor.
	 * @param apprentice the apprentice
	 * @param sponsor the sponsor
	 */
	public void saveApprenticeAndSponsor(int apprentice, int sponsor)
	{
		try (Connection con = DatabaseFactory.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE characters SET apprentice=?,sponsor=? WHERE charId=?"))
		{
			ps.setInt(1, apprentice);
			ps.setInt(2, sponsor);
			ps.setInt(3, getObjectId());
			ps.execute();
		}
		catch (SQLException e)
		{
			LOGGER.log(Level.WARNING, "Could not save apprentice/sponsor: " + e.getMessage(), e);
		}
	}
	
	public long getOnlineTime()
	{
		return _onlineTime;
	}
	
	public void setOnlineTime(long onlineTime)
	{
		_onlineTime = onlineTime;
	}
	
	public void resetBonus()
	{
		_onlineTime = 0;
		final PlayerVariables vars = getVariables();
		vars.set("CLAIMED_CLAN_REWARDS", 0);
		vars.storeMe();
	}
	
	public int getOnlineStatus()
	{
		return !isOnline() ? 0 : _onlineTime >= (Config.ALT_CLAN_MEMBERS_TIME_FOR_BONUS) ? 2 : 1;
	}
	
	public boolean isRewardClaimed(ClanRewardType type)
	{
		final PlayerVariables vars = getVariables();
		final int claimedRewards = vars.getInt("CLAIMED_CLAN_REWARDS", ClanRewardType.getDefaultMask());
		return (claimedRewards & type.getMask()) == type.getMask();
	}
	
	public void setRewardClaimed(ClanRewardType type)
	{
		final PlayerVariables vars = getVariables();
		int claimedRewards = vars.getInt("CLAIMED_CLAN_REWARDS", ClanRewardType.getDefaultMask());
		claimedRewards |= type.getMask();
		vars.set("CLAIMED_CLAN_REWARDS", claimedRewards);
		vars.storeMe();
	}
	
	private PlayerVariables getVariables()
	{
		final L2PcInstance player = getPlayerInstance();
		return player != null ? player.getVariables() : new PlayerVariables(_objectId);
	}
}
