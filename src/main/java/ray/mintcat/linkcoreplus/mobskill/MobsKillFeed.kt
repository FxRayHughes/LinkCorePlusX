package ray.mintcat.linkcoreplus.mobskill

import io.lumine.xikage.mythicmobs.MythicMobs
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitEntity
import io.lumine.xikage.mythicmobs.mobs.MythicMob
import org.bukkit.entity.Entity

object MobsKillFeed {

    fun Entity.toMythicMobs(): MythicMob? {
        return MythicMobs.inst().mobManager.getMythicMobByDisplayCompat(BukkitEntity(this))
    }

    fun List<DamagerMK>.sorted() : List<DamagerMK>{
        return this.sortedByDescending { it.damage }
    }

}