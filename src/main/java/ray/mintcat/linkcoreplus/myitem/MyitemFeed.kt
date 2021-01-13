package ray.mintcat.linkcoreplus.myitem

import io.izzel.taboolib.TabooLibAPI
import io.izzel.taboolib.util.Features
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import ray.mintcat.linkcoreplus.LinkCorePlus
import ray.mintcat.linkcoreplus.myitem.MyitemFeed.get
import java.util.concurrent.ConcurrentHashMap

object MyitemFeed {

    private val playerDataDefault = ConcurrentHashMap<Player, MyitemData>()
    private val playerDataAbs = ConcurrentHashMap<Player, MyitemDataAbs>()
    private val playerDataExc = ConcurrentHashMap<Player, MyitemDataExc>()

    //初始化玩家数据
    fun createPlayerData(player: Player) {
        playerDataDefault[player] = createData()
        playerDataAbs[player] = createDataAbs()
        playerDataExc[player] = createDataExc()
    }

    //删除玩家数据
    fun removePlayerData(player: Player) {
        playerDataDefault.remove(player)
        playerDataAbs.remove(player)
        playerDataExc.remove(player)
    }

    //读取数据
    fun getAttribute(player: Player, key: String): Double {
        return playerDataDefault[player]?.attributes?.get(key) ?: 0.0
    }

    fun getAbsAttribute(player: Player, key: String): Double {
        return playerDataAbs[player]?.attributesAbs?.getAbs(key) ?: 0.0
    }

    fun getExcAttribute(player: Player, key: String): Double {
        return playerDataExc[player]?.attributes?.getExc(key) ?: 0.0
    }

    //绝对增加
    fun setAttributeData(player: Player, key: String, value: Double) {
        val info = playerDataDefault[player]?.attributes!!
        if (info.has(key)){
            val inform = info[key]
            playerDataDefault[player]?.attributes?.remove(key)
            playerDataDefault[player]?.attributes?.add(MyitemInfo(key, value + inform))
        }else{
            playerDataDefault[player]?.attributes?.remove(key)
            playerDataDefault[player]?.attributes?.add(MyitemInfo(key, value))
        }
    }

    fun setAbsAttributeData(player: Player, key: String, value: Double) {
        val info = playerDataAbs[player]?.attributesAbs!!
        if (info.hasabs(key)){
            val inform = info.getAbs(key)
            playerDataAbs[player]?.attributesAbs?.removes(key)
            playerDataAbs[player]?.attributesAbs?.add(MyitemInfoAbs(key, value + inform))
        }else{
            playerDataAbs[player]?.attributesAbs?.removes(key)
            playerDataAbs[player]?.attributesAbs?.add(MyitemInfoAbs(key, value))
        }
    }

    fun setExcAttributeData(player: Player, key: String, value: Double) {
        val info = playerDataExc[player]?.attributes!!
        if (info.hasesc(key)){
            val inform = info.getExc(key)
            playerDataExc[player]?.attributes?.removee(key)
            playerDataExc[player]?.attributes?.add(MyitemInfoExc(key, value + inform))
        }else{
            playerDataExc[player]?.attributes?.removee(key)
            playerDataExc[player]?.attributes?.add(MyitemInfoExc(key, value))
        }
    }

    //写入属性
    //重点: 查重并合并/
    //xxx xx data*10+2
    fun editAttribute(player: Player, key: String, value: String, time: Long) {
        val info = getValues(player, value, key)
        val data = (Features.compileScript(info)?.eval() ?: "0.00").toString().toDouble()
        val a = data
        setAttributeData(player, key, data)
        Bukkit.getScheduler().runTaskLater(LinkCorePlus.plugin, Runnable {
            if (playerDataDefault[player] != null){
                setAttributeData(player, key, -a)
            }
        }, time)
    }

    fun editAbsAttribute(player: Player, key: String, value: String, time: Long) {
        val info = getValues(player, value, key)
        val data = (Features.compileScript(info)?.eval() ?: "0.00").toString().toDouble()
        val a = data
        setAbsAttributeData(player, key, data)
        Bukkit.getScheduler().runTaskLater(LinkCorePlus.plugin, Runnable {
            if (playerDataAbs[player] != null){
                setAbsAttributeData(player, key, -a)
            }
        }, time)
    }

    fun editExcAttribute(player: Player, key: String, value: String, time: Long) {
        val info = getValues(player, value, key)
        val data = (Features.compileScript(info)?.eval() ?: "0.00").toString().toDouble()
        val a = data
        setExcAttributeData(player, key, data)
        Bukkit.getScheduler().runTaskLater(LinkCorePlus.plugin, Runnable {
            setExcAttributeData(player, key, -a)
        }, time)
    }

    fun getValues(player: Player, value: String, key: String): String {
        val attribute = getAttribute(player, key)
        val attributeAbs = getAbsAttribute(player, key)
        val attributeExc = getExcAttribute(player,key)
        val armor = MyitemAttribute.getAttribute(player, key)
        return TabooLibAPI.getPluginBridge().setPlaceholders(player, value
                .replace("__", "")
                .replace("{armor}", armor.toString())
                .replace("{data}", attribute.toString())
                .replace("{dataexc}", attributeExc.toString())
                .replace("{dataabs}", attributeAbs.toString()))
    }

    fun getValueNull(player: Player, value: String): String {
        return TabooLibAPI.getPluginBridge().setPlaceholders(player, value
                .replace("__", "")
                .replace("{armor}", "0.0")
                .replace("{data}", "0.0")
                .replace("{dataexc}", "0.0")
                .replace("{dataabs}", "0.0"))
    }

    //获取完整参数
    fun getAttribueInfo(player: Player, key: String): Double {
        val abs = getAbsAttribute(player, key)
        val data = getAttribute(player, key)
        if (abs != 0.0) {
            return abs
        }
        val armor = MyitemAttribute.getAttribute(player, key)
        return armor + data
    }

    fun loadModel(){
        Bukkit.getOnlinePlayers().forEach{
            createPlayerData(it)
        }
    }

    private fun createData(): MyitemData {
        return MyitemData(mutableSetOf())
    }

    private fun createDataAbs():MyitemDataAbs{
        return MyitemDataAbs(mutableSetOf())
    }

    private fun createDataExc(): MyitemDataExc {
        return MyitemDataExc(mutableSetOf())
    }

    private operator fun MutableSet<MyitemInfo>.get(key: String): Double {
        return this.firstOrNull { it.attribute == key }?.value ?: 0.0
    }

    private fun MutableSet<MyitemInfoAbs>.getAbs(key: String): Double {
        return this.firstOrNull { it.attribute == key }?.absValue ?: 0.0
    }

    private fun MutableSet<MyitemInfoExc>.getExc(key: String): Double {
        return this.firstOrNull { it.attribute == key }?.value ?: 0.0
    }

    private fun MutableSet<MyitemInfo>.remove(key: String) {
        val map = HashMap<String, MyitemInfo>()
        for (i in this) {
            map[i.attribute] = i
        }
        this.remove(map[key])
    }

    private fun MutableSet<MyitemInfoAbs>.removes(key: String) {
        val map = HashMap<String, MyitemInfoAbs>()
        for (i in this) {
            map[i.attribute] = i
        }
        this.remove(map[key])
    }

    private fun MutableSet<MyitemInfoExc>.removee(key: String) {
        val map = HashMap<String, MyitemInfoExc>()
        for (i in this) {
            map[i.attribute] = i
        }
        this.remove(map[key])
    }

    private fun MutableSet<MyitemInfo>.has(key: String):Boolean {
        for (i in this) {
            if (i.attribute == key){
                return true
            }
        }
        return false
    }

    private fun MutableSet<MyitemInfoAbs>.hasabs(key: String):Boolean {
        for (i in this) {
            if (i.attribute == key){
                return true
            }
        }
        return false
    }

    private fun MutableSet<MyitemInfoExc>.hasesc(key: String):Boolean {
        for (i in this) {
            if (i.attribute == key){
                return true
            }
        }
        return false
    }

}
