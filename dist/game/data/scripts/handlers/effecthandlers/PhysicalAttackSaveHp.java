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
import com.l2jglobal.gameserver.model.effects.AbstractEffect;
import com.l2jglobal.gameserver.model.effects.L2EffectType;
import com.l2jglobal.gameserver.model.items.instance.L2ItemInstance;
import com.l2jglobal.gameserver.model.skills.Skill;
import com.l2jglobal.gameserver.model.stats.BaseStats;
import com.l2jglobal.gameserver.model.stats.Formulas;
import com.l2jglobal.gameserver.model.stats.Stats;

/**
 * Physical Attack effect implementation. <br>
 * <b>Note</b>: Initial formula taken from PhysicalAttack.
 * @author Adry_85, Nik
 */
public final class PhysicalAttackSaveHp extends AbstractEffect
{
	private final double _power;
	private final double _criticalChance;
	private final boolean _ignoreShieldDefence;
	private final boolean _overHit;
	private final double _saveHp;
	
	public PhysicalAttackSaveHp(StatsSet params)
	{
		_power = params.getDouble("power", 0);
		_criticalChance = params.getDouble("criticalChance", 0);
		_ignoreShieldDefence = params.getBoolean("ignoreShieldDefence", false);
		_overHit = params.getBoolean("overHit", false);
		_saveHp = params.getDouble("saveHp", 0);
	}
	
	@Override
	public boolean calcSuccess(L2Character effector, L2Character effected, Skill skill)
	{
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
		if (effector.isAlikeDead())
		{
			return;
		}
		
		if (effected.isPlayer() && effected.getActingPlayer().isFakeDeath())
		{
			effected.stopFakeDeath(true);
		}
		
		if (_overHit && effected.isAttackable())
		{
			((L2Attackable) effected).overhitEnabled(true);
		}
		
		final double attack = effector.getPAtk();
		double defence = effected.getPDef();
		
		if (!_ignoreShieldDefence)
		{
			switch (Formulas.calcShldUse(effector, effected))
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
		final boolean critical = (_criticalChance > 0) && ((BaseStats.STR.calcBonus(effector) * _criticalChance) > (Rnd.nextDouble() * 100));
		
		if (defence != -1)
		{
			// Trait, elements
			final double weaponTraitMod = Formulas.calcWeaponTraitBonus(effector, effected);
			final double generalTraitMod = Formulas.calcGeneralTraitBonus(effector, effected, skill.getTraitType(), false);
			final double attributeMod = Formulas.calcAttributeBonus(effector, effected, skill);
			final double pvpPveMod = Formulas.calculatePvpPveBonus(effector, effected, skill, true);
			final double randomMod = effector.getRandomDamageMultiplier();
			
			// Skill specific mods.
			final double wpnMod = effector.getAttackType().isRanged() ? 70 : (70 * 1.10113);
			final double rangedBonus = effector.getAttackType().isRanged() ? (attack + _power) : 0;
			final double critMod = critical ? Formulas.calcCritDamage(effector, effected, skill) : 1;
			final double ssmod = (skill.useSoulShot() && effector.isChargedShot(ShotType.SOULSHOTS)) ? effector.getStat().getValue(Stats.SHOTS_BONUS, 2) : 1; // 2.04 for dual weapon?
			
			// ...................____________Melee Damage_____________......................................___________________Ranged Damage____________________
			// ATTACK CALCULATION 77 * ((pAtk * lvlMod) + power) / pdef            RANGED ATTACK CALCULATION 70 * ((pAtk * lvlMod) + power + patk + power) / pdef
			// ```````````````````^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^``````````````````````````````````````^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
			final double baseMod = (wpnMod * ((attack * effector.getLevelMod()) + _power + rangedBonus)) / defence;
			damage = baseMod * ssmod * critMod * weaponTraitMod * generalTraitMod * attributeMod * pvpPveMod * randomMod;
			damage = effector.getStat().getValue(Stats.PHYSICAL_SKILL_POWER, damage);
		}
		
		// Check if damage should be reflected
		Formulas.calcDamageReflected(effector, effected, skill, critical);
		
		final double damageCap = effected.getStat().getValue(Stats.DAMAGE_LIMIT);
		if (damageCap > 0)
		{
			damage = Math.min(damage, damageCap);
		}
		
		final double minHp = (effected.getMaxHp() * _saveHp) / 100;
		
		if ((effected.getCurrentHp() - damage) < minHp)
		{
			damage = effected.getCurrentHp() - minHp;
		}
		
		effected.reduceCurrentHp(damage, effector, skill, false, false, critical, false);
		effector.sendDamageMessage(effected, skill, (int) damage, critical, false);
	}
}
