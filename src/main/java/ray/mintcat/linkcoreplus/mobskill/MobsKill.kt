package ray.mintcat.linkcoreplus.mobskill

import io.izzel.taboolib.module.inject.TListener
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent
import org.bukkit.entity.Entity
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import ray.mintcat.linkcoreplus.mobskill.MobsKillFeed.toMythicMobs
import ray.mintcat.linkcoreplus.myitem.MyitemInfoExc
import java.util.*

class MobsKill {

    @EventHandler
    fun onMythicMobDeathEvent(event: MythicMobDeathEvent){

    }


    private val DamageTable = mutableMapOf<Entity,MutableList<DamagerMK>>()

    @EventHandler
    fun onPlayerDamageMob(event:EntityDamageByEntityEvent){
        val mob = event.entity.toMythicMobs() ?: return
        if (!mob.config.getBoolean("Ranking.enable")){
            return
        }
        val list = DamageTable[event.entity ?: return] ?: mutableListOf()
        if (list.getDamage(event.damager.uniqueId) == 0.0){
            list.add(DamagerMK(event.damager.uniqueId,event.damage))
        }else{
            val value = list.getDamage(event.damager.uniqueId) + event.damage
            list.removes(event.damager.uniqueId)
            list.add(DamagerMK(event.damager.uniqueId,value))
        }
        DamageTable[event.entity] = list

    }

    private fun List<DamagerMK>.getDamage(uuid: UUID): Double {
        return this.firstOrNull {it.target == uuid}?.damage ?: 0.0
    }

    private fun List<DamagerMK>.removes(key: UUID) {
        val map = mutableMapOf<UUID,Double>()
        for (i in this) {
            map[i.target] = i.damage
        }
        this.toMutableList().remove(DamagerMK(key,map[key] ?: 0.0))
    }
}
