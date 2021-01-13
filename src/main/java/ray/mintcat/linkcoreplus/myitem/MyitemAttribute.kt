package ray.mintcat.linkcoreplus.myitem

import api.praya.myitems.builder.ability.AbilityWeapon
import api.praya.myitems.builder.element.ElementBoostStats
import api.praya.myitems.builder.item.ItemSetBonusEffectEntity
import api.praya.myitems.builder.item.ItemStatsArmor
import api.praya.myitems.builder.item.ItemStatsWeapon
import com.praya.agarthalib.utility.MathUtil
import com.praya.myitems.MyItems
import com.praya.myitems.manager.game.AbilityWeaponManager
import com.praya.myitems.manager.game.ElementManager
import com.praya.myitems.manager.game.GameManager
import com.praya.myitems.manager.game.ItemSetManager
import com.praya.myitems.manager.player.PlayerItemStatsManager
import com.praya.myitems.manager.player.PlayerManager
import core.praya.agarthalib.enums.main.SlotType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import java.util.HashMap

object MyitemAttribute {

    fun getAttribute(player: Player, string: String): Double {
        val plugin = MyItems.getPlugin(MyItems::class.java) as MyItems
        val gameManager: GameManager = plugin.gameManager
        val playerManager: PlayerManager = plugin.playerManager
        val abilityWeaponManager: AbilityWeaponManager = gameManager.abilityWeaponManager
        val elementManager: ElementManager = gameManager.elementManager
        val itemSetManager: ItemSetManager = gameManager.itemSetManager
        val playerItemStatsManager: PlayerItemStatsManager = playerManager.playerItemStatsManager
        val itemStatsWeapon: ItemStatsWeapon = playerItemStatsManager.getItemStatsWeapon(player)
        val itemStatsArmor: ItemStatsArmor = playerItemStatsManager.getItemStatsArmor(player)
        val mapAbilityWeapon: HashMap<AbilityWeapon, Int> = abilityWeaponManager.getMapAbilityWeapon(player as LivingEntity)
        val mapElementWeapon: HashMap<String, Double> = elementManager.getMapElement(player as LivingEntity, SlotType.WEAPON, false)
        val itemSetBonusEffectEntity: ItemSetBonusEffectEntity = itemSetManager.getItemSetBonusEffectEntity(player as LivingEntity, true, false)
        val itemSetBonusEffectStats = itemSetBonusEffectEntity.effectStats
        val elementWeapon: ElementBoostStats = elementManager.getElementBoostStats(mapElementWeapon)
        return when (string) {
            "stats_damage_max" -> {
                val totalDamageMax = (itemStatsWeapon.totalDamageMax + itemSetBonusEffectStats.additionalDamage) * (100.0 + itemSetBonusEffectStats.percentDamage) / 100.0
                MathUtil.roundNumber(totalDamageMax)

            }
            "stats_damage_min" -> {
                val totalDamageMin = (itemStatsWeapon.totalDamageMin + itemSetBonusEffectStats.additionalDamage) * (100.0 + itemSetBonusEffectStats.percentDamage) / 100.0
                MathUtil.roundNumber(totalDamageMin)
            }
            "stats_damage" -> {
                val totalDamageMax = (itemStatsWeapon.totalDamageMax + itemSetBonusEffectStats.additionalDamage) * (100.0 + itemSetBonusEffectStats.percentDamage) / 100.0
                val totalDamageMin = (itemStatsWeapon.totalDamageMin + itemSetBonusEffectStats.additionalDamage) * (100.0 + itemSetBonusEffectStats.percentDamage) / 100.0
                (totalDamageMin.toInt()..totalDamageMax.toInt()).random().toDouble()
            }
            "stats_penetration" -> {
                val totalPenetration = itemStatsWeapon.totalPenetration + itemSetBonusEffectStats.penetration
                MathUtil.roundNumber(totalPenetration)
            }
            "stats_pvp_damage" -> {
                val totalPvPDamage = itemStatsWeapon.totalPvPDamage + itemSetBonusEffectStats.pvPDamage
                MathUtil.roundNumber(totalPvPDamage)
            }
            "stats_pve_damage" -> {
                //都是在修以前的错误而已 555
                val totalPvEDamage = itemStatsWeapon.totalPvEDamage + itemSetBonusEffectStats.pvEDamage
                MathUtil.roundNumber(totalPvEDamage)
            }
            "stats_critical_chance" -> {
                val totalCriticalChance = itemStatsWeapon.totalCriticalChance + itemSetBonusEffectStats.criticalChance
                MathUtil.roundNumber(totalCriticalChance)
            }
            "stats_critical_damage" -> {
                val totalCriticalDamage = itemStatsWeapon.totalCriticalDamage + itemSetBonusEffectStats.criticalDamage
                MathUtil.roundNumber(totalCriticalDamage)
            }
            "stats_aoe_radius" -> {
                val totalAttackAoERadius = itemStatsWeapon.totalAttackAoERadius + itemSetBonusEffectStats.attackAoERadius
                MathUtil.roundNumber(totalAttackAoERadius)
            }
            "stats_aoe_damage" -> {
                val totalAttackAoEDamage = itemStatsWeapon.totalAttackAoEDamage + itemSetBonusEffectStats.attackAoEDamage
                MathUtil.roundNumber(totalAttackAoEDamage)
            }
            "stats_hit_rate" -> {
                val totalHitRate = itemStatsWeapon.totalHitRate + itemSetBonusEffectStats.hitRate
                MathUtil.roundNumber(totalHitRate)
            }
            "stats_defense" -> {
                val totalDefense = (itemStatsArmor.totalDefense + itemSetBonusEffectStats.additionalDefense) * (100.0 + itemSetBonusEffectStats.percentDefense) / 100.0
                MathUtil.roundNumber(totalDefense)
            }
            "stats_pvp_defense" -> {
                val totalPvPDefense = itemStatsArmor.totalPvPDefense + itemSetBonusEffectStats.pvPDefense
                MathUtil.roundNumber(totalPvPDefense)
            }
            "stats_pve_defense" -> {
                val totalPvEDefense = itemStatsArmor.totalPvEDefense + itemSetBonusEffectStats.pvEDefense
                MathUtil.roundNumber(totalPvEDefense)
            }
            "stats_health" -> {
                val totalHealth = itemStatsArmor.totalHealth + itemSetBonusEffectStats.health
                MathUtil.roundNumber(totalHealth)
            }
            "stats_health_regen" -> {
                val totalHealthRegen = itemStatsArmor.totalHealthRegen + itemSetBonusEffectStats.healthRegen
                MathUtil.roundNumber(totalHealthRegen)
            }
            "stats_stamina_max" -> {
                val totalStaminaMax = itemStatsArmor.totalStaminaMax + itemSetBonusEffectStats.staminaMax
                MathUtil.roundNumber(totalStaminaMax)
            }
            "stats_stamina_regen" -> {
                val totalStaminaRegen = itemStatsArmor.totalStaminaRegen + itemSetBonusEffectStats.staminaRegen
                MathUtil.roundNumber(totalStaminaRegen)
            }
            "stats_block_amount" -> {
                val totalBlockAmount = itemStatsArmor.totalBlockAmount + itemSetBonusEffectStats.blockAmount
                MathUtil.roundNumber(totalBlockAmount)
            }
            "stats_block_rate" -> {
                val totalBlockRate = itemStatsArmor.totalBlockRate + itemSetBonusEffectStats.blockRate
                MathUtil.roundNumber(totalBlockRate)
            }
            "stats_dodge_rate" -> {
                val totalDodgeRate = itemStatsArmor.totalBlockRate + itemSetBonusEffectStats.dodgeRate
                MathUtil.roundNumber(totalDodgeRate)
            }
            "ability_base_additional_damage" -> {
                val abilityBaseBonusDamage: Double = abilityWeaponManager.getTotalBaseBonusDamage(mapAbilityWeapon)
                MathUtil.roundNumber(abilityBaseBonusDamage)
            }
            "ability_base_percent_damage" -> {
                val abilityBasePercentDamage: Double = abilityWeaponManager.getTotalBasePercentDamage(mapAbilityWeapon)
                MathUtil.roundNumber(abilityBasePercentDamage)
            }
            "ability_cast_additional_damage" -> {
                val abilityCastBonusDamage: Double = abilityWeaponManager.getTotalCastBonusDamage(mapAbilityWeapon)
                MathUtil.roundNumber(abilityCastBonusDamage)
            }
            "ability_cast_percent_damage" -> {
                val abilityCastPercentDamage: Double = abilityWeaponManager.getTotalCastPercentDamage(mapAbilityWeapon)
                MathUtil.roundNumber(abilityCastPercentDamage)
            }
            "element_additional_damage" -> {
                val elementAdditionalDamage = elementWeapon.baseAdditionalDamage
                MathUtil.roundNumber(elementAdditionalDamage)
            }
            "element_percent_damage" -> {
                val elementPercentDamage = elementWeapon.basePercentDamage
                MathUtil.roundNumber(elementPercentDamage)
            }
            else -> 0.00
        }
    }
}