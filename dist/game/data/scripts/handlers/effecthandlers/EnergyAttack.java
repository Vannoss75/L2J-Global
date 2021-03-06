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

import com.l2jglobal.commons.util.Rnd;
import com.l2jglobal.gameserver.enums.ShotType;
import com.l2jglobal.gameserver.model.StatsSet;
import com.l2jglobal.gameserver.model.actor.L2Attackable;
import com.l2jglobal.gameserver.model.actor.L2Character;
import com.l2jglobal.gameserver.model.actor.instance.L2PcInstance;
import com.l2jglobal.gameserver.model.effects.AbstractEffect;
import com.l2jglobal.gameserver.model.effects.L2EffectType;
import com.l2jglobal.gameserver.model.items.instance.L2ItemInstance;
import com.l2jglobal.gameserver.model.skills.Skill;
import com.l2jglobal.gameserver.model.stats.BaseStats;
import com.l2jglobal.gameserver.model.stats.Formulas;
import com.l2jglobal.gameserver.model.stats.Stats;
import com.l2jglobal.gameserver.network.SystemMessageId;
import com.l2jglobal.gameserver.network.serverpackets.SystemMessage;

/**
 * Energy Attack effect implementation.
 * @author NosBit
 */
public final class EnergyAttack extends AbstractEffect
{
	private final double _power;
	private final int _chargeConsume;
	private final int _criticalChance;
	private final boolean _ignoreShieldDefence;
	private final boolean _overHit;
	
	public EnergyAttack(StatsSet params)
	{
		_power = params.getDouble("power", 0);
		_criticalChance = params.getInt("criticalChance", 0);
		_ignoreShieldDefence = params.getBoolean("ignoreShieldDefence", false);
		_overHit = params.getBoolean("overHit", false);
		_chargeConsume = params.getInt("chargeConsume", 0);
	}
	
	@Override
	public boolean calcSuccess(L2Character effector, L2Character effected, Skill skill)
	{
		// TODO: Verify this on retail
		return !Formulas.calcPhysicalSkillEvasion(effector, effected, skill);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.PHYSICAL_ATTACK;
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(L2Character effector, L2Character effected, Skill skill, L2ItemInstance item)
	{
		if (!effector.isPlayer())
		{
			return;
		}
		
		final L2PcInstance attacker = effector.getActingPlayer();
		
		final int charge = Math.min(_chargeConsume, attacker.getCharges());
		
		if (!attacker.decreaseCharges(charge))
		{
			final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS);
			sm.addSkillName(skill);
			attacker.sendPacket(sm);
			return;
		}
		
		final double distance = attacker.calculateDistance(effected, true, false);
		if (distance > effected.getStat().getValue(Stats.SPHERIC_BARRIER_RANGE, Integer.MAX_VALUE))
		{
			return;
		}
		
		if (_overHit && effected.isAttackable())
		{
			((L2Attackable) effected).overhitEnabled(true);
		}
		
		int defence = effected.getPDef();
		
		if (!_ignoreShieldDefence)
		{
			final byte shield = Formulas.calcShldUse(attacker, effected);
			switch (shield)
			{
				case Formulas.SHIELD_DEFENSE_SUCCEED:
				{
					defence += effected.getShldDef();
					break;
				}
				case Formulas.SHIELD_DEFENSE_PERFECT_BLOCK:
				{
					defence = -1;
					break;
				}
			}
		}
		
		double damage = 1;
		final boolean critical = (_criticalChance > 0) && ((BaseStats.STR.calcBonus(attacker) * _criticalChance) > (Rnd.nextDouble() * 100));
		
		if (defence != -1)
		{
			// Trait, elements
			final double weaponTraitMod = Formulas.calcWeaponTraitBonus(attacker, effected);
			final double generalTraitMod = Formulas.calcGeneralTraitBonus(attacker, effected, skill.getTraitType(), false);
			final double attributeMod = Formulas.calcAttributeBonus(attacker, effected, skill);
			final double pvpPveMod = Formulas.calculatePvpPveBonus(attacker, effected, skill, true);
			
			// Skill specific mods.
			final double energyChargesBoost = 1 + (charge * 0.1); // 10% bonus damage for each charge used.
			final double critMod = critical ? Formulas.calcCritDamage(attacker, effected, skill) : 1;
			final double ssmod = (skill.useSoulShot() && attacker.isChargedShot(ShotType.SOULSHOTS)) ? attacker.getStat().getValue(Stats.SHOTS_BONUS, 2) : 1; // 2.04 for dual weapon?
			
			// ...................________Initial Damage_________...__Charges Additional Damage__...____________________________________
			// ATTACK CALCULATION ((77 * ((pAtk * lvlMod) + power) * (1 + (0.1 * chargesConsumed)) / pdef) * skillPower) + skillPowerAdd
			// ```````````````````^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^```^^^^^^^^^^^^^^^^^^^^^^^^^^^^^```^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
			final double baseMod = (77 * ((attacker.getPAtk() * attacker.getLevelMod()) + _power)) / defence;
			damage = baseMod * ssmod * critMod * weaponTraitMod * generalTraitMod * attributeMod * energyChargesBoost * pvpPveMod;
			damage = attacker.getStat().getValue(Stats.PHYSICAL_SKILL_POWER, damage);
		}
		
		damage = Math.max(0, damage);
		
		// Check if damage should be reflected
		Formulas.calcDamageReflected(attacker, effected, skill, critical);
		
		final double damageCap = effected.getStat().getValue(Stats.DAMAGE_LIMIT);
		if (damageCap > 0)
		{
			damage = Math.min(damage, damageCap);
		}
		effected.reduceCurrentHp(damage, effector, skill, false, false, critical, false);
		attacker.sendDamageMessage(effected, skill, (int) damage, critical, false);
	}
}