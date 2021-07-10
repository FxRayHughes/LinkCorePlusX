package ray.mintcat.linkcoreplus.utils

import io.izzel.taboolib.kotlin.kether.KetherShell
import io.izzel.taboolib.kotlin.kether.common.util.LocalizedException
import io.izzel.taboolib.util.Coerce
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture

object Utils {

    fun toInt(m: String): Int {
        val graph = IntArray(400)
        graph['I'.toInt()] = 1
        graph['V'.toInt()] = 5
        graph['X'.toInt()] = 10
        graph['L'.toInt()] = 50
        graph['C'.toInt()] = 100
        graph['D'.toInt()] = 500
        graph['M'.toInt()] = 1000
        val num = m.toCharArray()
        var sum = graph[num[0].toInt()]
        for (i in 0 until num.size - 1) {
            if (graph[num[i].toInt()] >= graph[num[i + 1].toInt()]) {
                sum += graph[num[i + 1].toInt()]
            } else {
                sum = sum + graph[num[i + 1].toInt()] - 2 * graph[num[i].toInt()]
            }
        }
        return sum
    }

    fun eval(player: Player, action: List<String>) {
        try {
            KetherShell.eval(action) {
                sender = player
            }
        } catch (e: LocalizedException) {
            e.print()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun check(player: Player, condition: List<String>): CompletableFuture<Boolean> {
        return if (condition.isEmpty()) {
            CompletableFuture.completedFuture(true)
        } else {
            try {
                KetherShell.eval(condition) {
                    sender = player
                }.thenApply {
                    Coerce.toBoolean(it)
                }
            } catch (e: LocalizedException) {
                e.print()
                CompletableFuture.completedFuture(false)
            } catch (e: Throwable) {
                e.printStackTrace()
                CompletableFuture.completedFuture(false)
            }
        }
    }

    fun LocalizedException.print() {
        println("[Kether] Unexpected exception while parsing kether shell:")
        localizedMessage.split("\n").forEach {
            println("[Kether] $it")
        }
    }

}