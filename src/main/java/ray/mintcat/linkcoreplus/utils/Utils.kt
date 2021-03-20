package ray.mintcat.linkcoreplus.utils

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

}